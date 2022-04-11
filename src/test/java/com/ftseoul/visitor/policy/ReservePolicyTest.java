package com.ftseoul.visitor.policy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ReservePolicyTest {

    @Autowired
    ReservePolicyFactory reservePolicyFactory;

    @Test
    @DisplayName("예약_서비스_팩토리_테스트")
    void policyFactoryTest(){
        //given
        ReserveType defaultType = ReserveType.NO_REPRESENTATIVE;
        ReserveType representativeType = ReserveType.REPRESENTATIVE;

        //when
        ReservePolicy defaultPolicy = reservePolicyFactory.getPolicy(defaultType);

        //then
        Assertions.assertThat(defaultPolicy.getClass()).isEqualTo(DefaultReservePolicy.class);
    }
}
