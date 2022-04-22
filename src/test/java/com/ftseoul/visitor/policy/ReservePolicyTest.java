package com.ftseoul.visitor.policy;

import com.ftseoul.visitor.data.*;
import com.ftseoul.visitor.dto.reserve.ReserveVisitorDto;
import com.ftseoul.visitor.dto.visitor.VisitorDto;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.service.ReserveService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Rollback(value = true)
class ReservePolicyTest {

    @Autowired
    ReservePolicyFactory reservePolicyFactory;

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

    @BeforeAll
    void setup() {
        init();
    }

    @AfterAll
    void cleanup() {
        finish();
    }

    Staff savedStaff;

    void init(){
        Staff staff = Staff
                .builder()
                .name(seed.encrypt("김길동"))
                .phone(seed.encrypt("01012345678"))
                .department("시설관리")
                .build();
        savedStaff = staffRepository.save(staff);
    }

    void finish(){
        reserveRepository.deleteAll();
        visitorRepository.deleteAll();
        staffRepository.delete(savedStaff);
    }


    @Test
    @DisplayName("예약_서비스_팩토리_대표자_테스트")
    void policyFactoryTest(){
        //given
        ReserveType defaultType = ReserveType.DEFAULT;
        ReserveType representativeType = ReserveType.REPRESENTATIVE;
        List<VisitorDto> mockVisitors = new ArrayList<>();
        VisitorDto visitor1 = new VisitorDto("visitor1", "00000000000", "집");
        VisitorDto visitor2 = new VisitorDto("visitor2", "00000000000", "집");

        mockVisitors.add(visitor1);
        mockVisitors.add(visitor2);


        //when
        ReserveVisitorDto temp = new ReserveVisitorDto("개포", "김길동", "테스트", LocalDateTime.now(), ReserveType.REPRESENTATIVE, mockVisitors);
        ReservePolicy representativePolicy = reservePolicyFactory.getPolicy(temp.getType());
        Reserve reserve = representativePolicy.saveReserve(temp);
        Reserve reserve1 = reserveRepository.findById(reserve.getId()).orElseThrow(() -> new EntityNotFoundException("해당 예약 없음"));

        //then
        List<Visitor> visitors = visitorRepository.findAllByReserveId(reserve.getId());
        visitors.forEach(
                visitor -> {
                    Assertions.assertThat(seed.decrypt(visitor.getPhone())).isEqualTo("00000000000");
                    Assertions.assertThat(visitor.getOrganization()).isEqualTo("집");
                    System.out.println("savedVisitor" + visitor.toString());
                }
        );
        Assertions.assertThat(reserve.getPurpose()).isEqualTo("테스트");
        System.out.println("savedReserve" + reserve1.toString());
    }

    @Test
    @DisplayName("예약_서비스_팩토리_기본_테스트")
    void defaultPolicyTest(){

        //given
        List<VisitorDto> mockVisitors = new ArrayList<>();
        VisitorDto visitor1 = new VisitorDto("visitor1", "0101", "집1");
        VisitorDto visitor2 = new VisitorDto("visitor2", "0102", "집2");
        mockVisitors.add(visitor1);
        mockVisitors.add(visitor2);

        //when
        ReserveVisitorDto temp = new ReserveVisitorDto("개포", "김길동", "테스트", LocalDateTime.now(), ReserveType.DEFAULT, mockVisitors);
        ReservePolicy defaultPolicy = reservePolicyFactory.getPolicy(temp.getType());
        Reserve reserve = defaultPolicy.saveReserve(temp);
        Reserve reserve1 = reserveRepository.findById(reserve.getId()).orElseThrow(() -> new EntityNotFoundException("해당 예약 없음"));
        AtomicInteger i = new AtomicInteger(1);
        //then
        List<Visitor> visitors = visitorRepository.findAllByReserveId(reserve.getId());
        visitors.forEach(
                visitor -> {
                    Assertions.assertThat(visitor.getOrganization()).isEqualTo("집" + i);
                    Assertions.assertThat(seed.decrypt(visitor.getPhone())).isEqualTo(String.valueOf("010" + i.getAndIncrement()));
                    System.out.println("savedVisitor" + visitor.toString());
                }
        );
    }

    @Test
    @DisplayName("대표자_예약_여러단체")
    void representativeReservations(){

        //given
        List<VisitorDto> mockVisitorsA = new ArrayList<>();
        VisitorDto visitor1 = new VisitorDto("visitorA1", "0101", "집1");
        VisitorDto visitor2 = new VisitorDto("visitorA2", "0102", "집2");
        mockVisitorsA.add(visitor1);
        mockVisitorsA.add(visitor2);

        List<VisitorDto> mockVisitorsB = new ArrayList<>();
        VisitorDto visitorB1 = new VisitorDto("visitorB1", "01095344150", "집11");
        VisitorDto visitorB2 = new VisitorDto("visitorB2", "00000000000", "집12");
        mockVisitorsB.add(visitorB1);
        mockVisitorsB.add(visitorB2);

        List<VisitorDto> mockVisitorsC = new ArrayList<>();
        VisitorDto visitorC1 = new VisitorDto("visitorB1", "01095344150", "집11");
        VisitorDto visitorC2 = new VisitorDto("visitorB2", "00000000000", "집12");
        mockVisitorsB.add(visitorC1);
        mockVisitorsB.add(visitorC2);

        //when
        ReserveVisitorDto tempA = new ReserveVisitorDto("개포", "김길동", "테스트", LocalDateTime.now(), ReserveType.REPRESENTATIVE, mockVisitorsA);
        ReserveVisitorDto tempB = new ReserveVisitorDto("개포", "김길동", "테스트", LocalDateTime.now(), ReserveType.REPRESENTATIVE, mockVisitorsB);
        ReserveVisitorDto tempC = new ReserveVisitorDto("개포", "김길동", "테스트", LocalDateTime.now(), ReserveType.REPRESENTATIVE, mockVisitorsC);
        ReservePolicy reservePolicyA = reservePolicyFactory.getPolicy(tempA.getType());
        ReservePolicy reservePolicyB = reservePolicyFactory.getPolicy(tempB.getType());
        ReservePolicy reservePolicyC = reservePolicyFactory.getPolicy(tempC.getType());
        Reserve reserve = reservePolicyA.saveReserve(tempA);
        Reserve reserve1 = reservePolicyB.saveReserve(tempB);
        Reserve reserve2 = reservePolicyC.saveReserve(tempC);
        Reserve findReserveA = reserveRepository.findById(reserve.getId()).orElseThrow(() -> new EntityNotFoundException("예약 없음"));
        Reserve findReserveB = reserveRepository.findById(reserve1.getId()).orElseThrow(() -> new EntityNotFoundException("예약 없음"));

        List<Visitor> visitorsA = visitorRepository.findAllByReserveId(findReserveA.getId());
        List<Visitor> visitorsB = visitorRepository.findAllByReserveId(findReserveB.getId());
        AtomicInteger i = new AtomicInteger(1);
        AtomicInteger j = new AtomicInteger(11);
        //then
        visitorsA.forEach(
                visitor -> {
                    Assertions.assertThat(visitor.getOrganization()).isEqualTo("집" + i);
                    Assertions.assertThat(seed.decrypt(visitor.getPhone())).isEqualTo(String.valueOf("010" + i.getAndIncrement()));
                    System.out.println("savedVisitor" + visitor.toString());
                }
        );

    }
}
