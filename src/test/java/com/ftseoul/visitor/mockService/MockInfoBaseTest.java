package com.ftseoul.visitor.mockService;


import com.ftseoul.visitor.data.*;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.service.InfoService;
import org.aspectj.util.Reflection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class MockInfoBaseTest {
    @InjectMocks
    protected InfoService infoService;

    @Mock
    protected ReserveRepository reserveRepository;

    @Mock
    protected VisitorRepository visitorRepository;

    @Mock
    protected QueryRepository queryRepository;


    protected Staff staff;
    protected Reserve reserve;
    protected Visitor visitor;
    @Spy
    Seed seed;

    @BeforeEach
    void init() {
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
        ReflectionTestUtils.setField(staff, "id",1L);

        reserve = Reserve
                .builder()
                .targetStaff(1L)
                .purpose("방문")
                .date(LocalDateTime.now())
                .place("개포")
                .build();
        ReflectionTestUtils.setField(reserve, "id",1L);

        visitor = Visitor.builder()
                .name(seed.encrypt("지울이름"))
                .reserve_id(1L)
                .phone(seed.encrypt("01055555555"))
                .organization("이노베이션아카데미")
                .build();
        ReflectionTestUtils.setField(visitor, "id", 1L);
    }

    
}
