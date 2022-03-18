package com.ftseoul.visitor.service;


import com.ftseoul.visitor.config.EncryptConfig;
import com.ftseoul.visitor.data.*;
import com.ftseoul.visitor.dto.reserve.ReserveListResponseDto;
import com.ftseoul.visitor.dto.reserve.ReserveRequestDto;
import com.ftseoul.visitor.dto.reserve.ReserveResponseDto;
import com.ftseoul.visitor.dto.reserve.ReserveVisitorDto;
import com.ftseoul.visitor.dto.staff.StaffDecryptDto;
import com.ftseoul.visitor.dto.visitor.VisitorDecryptDto;
import com.ftseoul.visitor.dto.visitor.VisitorDto;
import com.ftseoul.visitor.encrypt.Seed;
import org.apache.tomcat.jni.Local;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith({MockitoExtension.class})
public class ReserveServiceMockTest {


    @Mock
    private ReserveRepository reserveRepository;

    @InjectMocks
    private ReserveService reserveService;

    @Mock
    private VisitorRepository visitorRepository;

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private StaffService staffService;

    private Reserve reserve;
    private Staff staff;

    @Mock
    Seed seed;

    @BeforeEach
    void init(){
        reserve = Reserve
                .builder()
                .targetStaff(1L)
                .purpose("방문")
                .date(LocalDateTime.now())
                .place("개포")
                .build();
        staff = Staff
                .builder()
                .name(seed.encrypt("abcde"))
                .phone(seed.encrypt("01012345678"))
                .department("시설관리")
                .build();

        ReflectionTestUtils.setField(seed, "key", "visitorcrypt$#@!");
        ReflectionTestUtils.setField(seed, "IV", "visitor987654321");

    }

    @DisplayName("단순예약_성공테스트")
    @Test
    void reserveTest(){
        //given
        final List<VisitorDto> visitors = new ArrayList<>();
        visitors.add(new VisitorDto());
        ReserveVisitorDto reserveRequestDto = new ReserveVisitorDto("개포", "김길동", "테스트", LocalDateTime.now(), visitors);

       //when

        doReturn(reserve).when(reserveRepository).save(any(Reserve.class));
        final Reserve reserve = reserveService.saveReserve(reserveRequestDto, 1L);


        //then
        Assertions.assertThat(reserve.getPurpose()).isEqualTo(reserve.getPurpose());
        Assertions.assertThat(reserve.getTargetStaff()).isEqualTo(1L);

        // verify
        verify(reserveRepository, times(1)).save(any(Reserve.class));
    }

    @DisplayName("단순조회_테스트")
    @Test
    void findReserve(){

        //given
        List<Visitor> visitors = new ArrayList<>();
        visitors.add(new Visitor());
        StaffDecryptDto staffDecryptDto = new StaffDecryptDto(1L, "abcde", "01012345678","시설관리");

        //when
//        doReturn(reserve).when(reserveRepository).save(any(Reserve.class));
        doReturn(Optional.of(reserve)).when(reserveRepository).findById(any());
        doReturn(visitors).when(visitorRepository).findAllByReserveId(any());
        doReturn(Optional.of(staff)).when(staffRepository).findById(any());
        doReturn(staffDecryptDto).when(staffService).decryptStaff(any());
        final ReserveListResponseDto findReserve = reserveService.findById(1L);

        //then
        Assertions.assertThat(findReserve.getDate()).isEqualTo(reserve.getDate());

        //verify
    }

    @Test

}
