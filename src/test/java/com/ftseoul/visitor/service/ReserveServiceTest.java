package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Reserve;
import com.ftseoul.visitor.data.ReserveRepository;
import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.data.StaffRepository;
import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.data.VisitorRepository;
import com.ftseoul.visitor.dto.payload.Response;
import com.ftseoul.visitor.dto.reserve.ReserveListResponseDto;
import com.ftseoul.visitor.dto.reserve.ReserveModifyDto;
import com.ftseoul.visitor.dto.reserve.ReserveRequestDto;
import com.ftseoul.visitor.dto.reserve.ReserveVisitorDto;
import com.ftseoul.visitor.dto.visitor.VisitorDto;
import com.ftseoul.visitor.dto.visitor.VisitorModifyDto;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.exception.error.PhoneDuplicatedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Rollback(value = true)
class ReserveServiceTest {

    @Autowired
    private Seed seed;

    @Autowired
    private ReserveRepository reserveRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private ReserveService reserveService;

    @Autowired
    private EntityManager em;

    Staff savedStaff;

    Reserve savedReserve;

    Visitor savedVisitor;

    @BeforeAll
    void setup() {
        init();
    }

    @AfterAll
    void cleanup() {
        finish();
    }

    void init() {
        Staff staff = Staff
            .builder()
            .name(seed.encrypt("abcde"))
            .phone(seed.encrypt("01012345678"))
            .department("시설관리")
            .build();
        savedStaff = staffRepository.save(staff);

        Reserve reserve = Reserve
            .builder()
            .targetStaff(savedStaff.getId())
            .purpose("방문")
            .date(LocalDateTime.now())
            .place("개포")
            .build();
        savedReserve = reserveRepository.save(reserve);

        Visitor visitor = Visitor
            .builder()
            .name(seed.encrypt("홍길동님"))
            .organization("이노베이션아카데미")
            .reserve_id(savedReserve.getId())
            .phone(seed.encrypt("01011112222"))
            .build();
        savedVisitor = visitorRepository.save(visitor);
    }

    void finish() {
        reserveRepository.delete(savedReserve);
        visitorRepository.delete(savedVisitor);
        staffRepository.delete(savedStaff);
    }


    @Test
    @Transactional
    void 예약아이디_조회() {
        long id = savedReserve.getId();
        ReserveListResponseDto result = reserveService.findById(id);
        assertEquals(result.getId(), id);
    }

    @Test
    @Transactional
    void 핸드폰번호_중복처리() {
        List<VisitorDto> visitors = new ArrayList<>();
        String phoneNumber = "01012345678";
        VisitorDto v1 = new VisitorDto("방문자1", phoneNumber, "42");
        VisitorDto v2 = new VisitorDto("방문자2", phoneNumber, "42");
        visitors.add(v1);
        visitors.add(v2);
        assertThrows(PhoneDuplicatedException.class, () -> reserveService.checkDuplicatedPhone(visitors));
        visitors.clear();

        VisitorDto v3 = new VisitorDto("방문자3", "01099999999", "42");
        VisitorDto v4 = new VisitorDto("방문자4", "01088888888", "42");
        visitors.add(v3);
        visitors.add(v4);
        assertDoesNotThrow(() -> reserveService.checkDuplicatedPhone(visitors));


    }

    @Test
    @Transactional
    void 예약수정() {
        List<VisitorModifyDto> visitors = new ArrayList<>();
        visitors.add(new VisitorModifyDto());
        String place = "서초";
        long reserveId = savedReserve.getId();
        ReserveModifyDto reserveModifyDto= new ReserveModifyDto(reserveId,
            place, seed.encrypt("jaehchoi"), "수정테스트", LocalDateTime.now(), visitors);
        Reserve updateReserve = reserveService.updateReserve(reserveModifyDto, savedStaff.getId());
        em.flush();
        em.clear();
        assertEquals(updateReserve.getPlace(), reserveRepository.findById(reserveId).get().getPlace());
    }

    @Test
    @Transactional
    void 방문자_성함_핸드폰번호_예약조회() {
        ReserveRequestDto reserveRequestDto = new ReserveRequestDto("01011112222", "홍길동님");
        List<ReserveListResponseDto> reservesByNameAndPhone = reserveService.findReservesByNameAndPhone(reserveRequestDto);
        if (reservesByNameAndPhone.isEmpty()) {
            fail("조회되지 않음");
        }
        assertEquals(reservesByNameAndPhone.get(0).getId(), savedReserve.getId());
    }

    @Test
    @Transactional
    void 예약삭제() {
        Reserve temp = Reserve
            .builder()
            .targetStaff(savedStaff.getId())
            .purpose("방문")
            .date(LocalDateTime.now())
            .place("개포")
            .build();
        Reserve saved = reserveRepository.save(temp);
        long savedId = saved.getId();

        em.flush();
        Response response = reserveService.deleteById(savedId);
        assertEquals(response.getCode(), "2000");
        em.flush();
        Response failResponse = reserveService.deleteById(savedId);
        assertEquals(failResponse.getCode(), "4000");
    }

    @Test
    @Transactional
    void 예약신청() {
        List<VisitorDto> mockVisitors = new ArrayList<>();
        mockVisitors.add(new VisitorDto());
        ReserveVisitorDto temp = new ReserveVisitorDto("개포", "김길동", "테스트", LocalDateTime.now(), mockVisitors);
        Reserve result = reserveService.saveReserve(temp, savedStaff.getId());
        assertNotNull(result);
        reserveRepository.delete(result);
    }

    @Test
    @Transactional
    void 스테프아이디로_예약삭제() {

        Staff staff = Staff
            .builder()
            .name(seed.encrypt(seed.encrypt("abcde")))
            .phone(seed.encrypt(seed.encrypt("01012345678")))
            .department("시설관리")
            .build();
        Staff staffData = staffRepository.save(staff);

        Reserve temp = Reserve
            .builder()
            .targetStaff(staffData.getId())
            .purpose("방문")
            .date(LocalDateTime.now())
            .place("개포")
            .build();
        Reserve reserveData = reserveRepository.save(temp);

        reserveService.deleteAllByStaffId(staffData.getId());

        em.flush();

        Optional<Reserve> result = reserveRepository.findById(reserveData.getId());

        assertThrows(NoSuchElementException.class, ()-> result.get());
    }

    @Test
    @Transactional
    void 예약에서방문자삭제() {
        Reserve reserve = Reserve
            .builder()
            .targetStaff(savedStaff.getId())
            .purpose("방문")
            .date(LocalDateTime.now())
            .place("개포")
            .build();
        Reserve r1 = reserveRepository.save(reserve);

        String deleteVisitorName = "지울이름";
        String deleteVisitorPhone = "01055555555";

        Visitor visitor = Visitor
            .builder()
            .name(seed.encrypt(deleteVisitorName))
            .organization("이노베이션아카데미")
            .reserve_id(r1.getId())
            .phone(seed.encrypt(deleteVisitorPhone))
            .build();
        Visitor v1 = visitorRepository.save(visitor);

        ReserveRequestDto reserveRequestDto= new ReserveRequestDto(deleteVisitorPhone, deleteVisitorName);
        reserveService.visitorReserveDelete(r1.getId(), reserveRequestDto);

        Optional<Reserve> r2 = reserveRepository.findById(r1.getId());
        Optional<Visitor> v2 = visitorRepository.findById(v1.getId());

        assertThrows(NoSuchElementException.class, () -> r2.get());
        assertThrows(NoSuchElementException.class, () -> v2.get());
    }

}