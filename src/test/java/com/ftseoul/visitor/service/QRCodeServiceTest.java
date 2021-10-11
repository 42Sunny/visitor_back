package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Device;
import com.ftseoul.visitor.data.DeviceRepository;
import com.ftseoul.visitor.data.Reserve;
import com.ftseoul.visitor.data.ReserveRepository;
import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.data.StaffRepository;
import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.data.VisitorRepository;
import com.ftseoul.visitor.data.visitor.VisitorStatus;
import com.ftseoul.visitor.dto.qrcode.QRCheckResponseDto;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.exception.InvalidQRCodeException;
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
@Transactional
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
    void QR내용확인() {
        String original = savedVisitor.getId().toString();
        String qRText = seed.encryptUrl(original);
        String decodeQRText = qRcodeService.decodeQRText(qRText);

        assertEquals(original, decodeQRText);

        String invalidText = String.valueOf(Integer.MAX_VALUE);
        assertThrows(InvalidQRCodeException.class , () -> qRcodeService.decodeQRText(invalidText));
    }

    @Test
    void 방문자상태체크() {
        String original = savedVisitor.getId().toString();
        QRCheckResponseDto response1 = qRcodeService.checkQRCode(original);
        assertEquals(response1.getStatus(), "입실");

        savedVisitor.updateStatus(VisitorStatus.퇴실);
        visitorRepository.save(savedVisitor);
        QRCheckResponseDto response2 = qRcodeService.checkQRCode(original);
        assertEquals(response2.getStatus(), "퇴실");

        savedVisitor.updateStatus(VisitorStatus.만료);
        visitorRepository.save(savedVisitor);
        QRCheckResponseDto response3 = qRcodeService.checkQRCode(original);
        assertEquals(response3.getStatus(), "만료");
    }

    @Test
    void 디바이스체크() {
        String tempDeviceId = "0000";
        deviceRepository.save(new Device(tempDeviceId));
        assertDoesNotThrow(() -> qRcodeService.checkAllowedDevice(tempDeviceId));
    }
}
