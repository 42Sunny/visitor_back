package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Device;
import com.ftseoul.visitor.data.DeviceRepository;
import com.ftseoul.visitor.data.Reserve;
import com.ftseoul.visitor.data.ReserveRepository;
import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.data.StaffRepository;
import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.data.VisitorRepository;
import com.ftseoul.visitor.dto.qrcode.QRCheckResponseDto;
import com.ftseoul.visitor.encrypt.Seed;

import java.time.LocalDateTime;
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
class QRCodeServiceTest {
    @Autowired
    private QRcodeService qRcodeService;

    @Autowired
    private Seed seed;

    @Autowired
    private ReserveRepository reserveRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private DeviceRepository deviceRepository;

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
    void 방문자상태체크() {
        String original = savedVisitor.getId().toString();
        QRCheckResponseDto response1 = qRcodeService.checkQRCode(original);
        assertEquals(response1.getStatus(), "입실");
    }

    @Test
    @Transactional
    void 디바이스체크() {
        String tempDeviceId = "0000";
        deviceRepository.save(new Device(tempDeviceId));
        assertDoesNotThrow(() -> qRcodeService.checkAllowedDevice(tempDeviceId));
    }
}
