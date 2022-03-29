package com.ftseoul.visitor.mockService;

import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.data.visitor.VisitorStatus;
import com.ftseoul.visitor.dto.payload.VisitorStatusInfo;
import com.ftseoul.visitor.dto.reserve.DateFoundResponseDto;
import com.ftseoul.visitor.dto.visitor.UpdateVisitorStatusDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class InfoServiceMockTest extends MockInfoBaseTest{

    @Test
    @DisplayName("날짜별_조회_빈리스트_반환")
    void searchByDateReturnEmptyList(){

        //given
        //Nothing

        //when
        doReturn(List.of()).when(reserveRepository).findAllReserveWithStaffByDate(any(), any());

        //then
        final List<DateFoundResponseDto> findList = infoService.findAllByDate(LocalDateTime.now().toLocalDate());
        Assertions.assertThat(findList.size()).isEqualTo(0);
    }


    @Test
    @DisplayName("방문자_상태_변경")
    void changeVisitorStatus(){

        //given
        UpdateVisitorStatusDto dto = new UpdateVisitorStatusDto(new VisitorStatusInfo(visitor.getId(), VisitorStatus.퇴실));
        Visitor checkoutVisitor = visitor;
        checkoutVisitor.checkOut();

        //when
        doReturn(Optional.of(visitor)).when(visitorRepository).findById(1L);
        doReturn(checkoutVisitor).when(visitorRepository).save(any());
        final VisitorStatusInfo visitorStatusInfo = infoService.changeVisitorStatus(dto);

        //then
        Assertions.assertThat(visitorStatusInfo.getStatus()).isEqualTo(VisitorStatus.퇴실);

        //verify
        verify(visitorRepository, times(1)).findById(anyLong());
        verify(visitorRepository, times(1)).save(any());
    }
}
