package com.ftseoul.visitor.mockService;


import com.ftseoul.visitor.data.QueryRepository;
import com.ftseoul.visitor.data.ReserveRepository;
import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.data.VisitorRepository;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.service.InfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.ref.Reference;
import java.nio.charset.StandardCharsets;

@ExtendWith(MockitoExtension.class)
public class InfoServiceMockTest {


    @InjectMocks
    private InfoService infoService;

    @Mock
    private ReserveRepository reserveRepository;

    @Mock
    private VisitorRepository visitorRepository;

    @Mock
    private QueryRepository queryRepository;

    @Spy
    Seed seed;

    @BeforeEach
    void init(){
        ReflectionTestUtils.setField(seed, "key", "visitorcrypt$#@!");
        ReflectionTestUtils.setField(seed, "IV", "visitor987654321");
        ReflectionTestUtils.setField(seed, "pbszUserKey", "visitor987654321".getBytes(StandardCharsets.UTF_8));
        ReflectionTestUtils.setField(seed, "pbszIV", "visitor987654321".getBytes(StandardCharsets.UTF_8));

        Staff staff = Staff
                .builder()
                .name(seed.encrypt("abcde"))
                .phone(seed.encrypt("01012345678"))
                .department("시설관리")
                .build();

    }
}
