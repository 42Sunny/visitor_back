package com.ftseoul.visitor.repository;


import com.ftseoul.visitor.data.Reserve;
import com.ftseoul.visitor.data.ReserveRepository;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MockReserveRepositoryTest {

    @Mock
    private ReserveRepository reserveRepository;


    @Test
    @DisplayName("mock 예약레파지토리 테스트")
    void mockReserveRepositoryTest(){
        //given
        Reserve reserve = Reserve
                .builder()
                .targetStaff(1L)
                .purpose("방문")
                .date(LocalDateTime.now())
                .place("개포")
                .build();
        List<Reserve> reserveList = new ArrayList<>();
        reserveList.add(reserve);

        given(reserveRepository.findAll()).willReturn(reserveList);

        //when
        List<Reserve> findReserveList = reserveRepository.findAll();

        //then
        Assertions.assertThat(findReserveList.size()).isEqualTo(1);
        Assertions.assertThat(reserve.getDate()).isEqualTo(findReserveList.get(0).getDate());
    }

    @Test
    @DisplayName("예약조회_mock레파지토리_테스트")
    void findReserve(){
        //given

    }
}
