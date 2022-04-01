package com.ftseoul.visitor.mockService;

import com.ftseoul.visitor.data.*;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.service.VisitorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.MapKeyColumn;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class MockVisitorBaseTest {

    @Mock
    protected ReserveRepository reserveRepository;

    @Mock
    protected StaffRepository staffRepository;

    @Mock
    protected VisitorRepository visitorRepository;

    @InjectMocks
    protected VisitorService visitorService;

    @Spy
    Seed seed;

    protected Staff staff;
    protected Reserve reserve;
    @BeforeEach
    void init(){
        ReflectionTestUtils.setField(seed, "key", "visitorcrypt$#@!");
        ReflectionTestUtils.setField(seed, "IV", "visitor987654321");
        ReflectionTestUtils.setField(seed, "pbszUserKey", "visitor987654321".getBytes(StandardCharsets.UTF_8));
        ReflectionTestUtils.setField(seed, "pbszIV", "visitor987654321".getBytes(StandardCharsets.UTF_8));

        staff = Staff
                .builder()
                .name(seed.encrypt("abcde"))
                .phone(seed.encrypt("01012345678"))
                .department("시설관리")
                .build();
        ReflectionTestUtils.setField(staff, "id", 1L);
        reserve = Reserve
                .builder()
                .targetStaff(staff.getId())
                .purpose("방문")
                .date(LocalDateTime.now())
                .place("개포")
                .build();
        ReflectionTestUtils.setField(reserve, "id", 1L);
    }
}
