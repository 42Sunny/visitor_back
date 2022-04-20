package com.ftseoul.visitor.mockService;


import com.ftseoul.visitor.data.*;
import com.ftseoul.visitor.dto.payload.Response;
import com.ftseoul.visitor.dto.reserve.*;
import com.ftseoul.visitor.dto.staff.StaffDecryptDto;
import com.ftseoul.visitor.dto.visitor.VisitorDto;
import com.ftseoul.visitor.dto.visitor.VisitorModifyDto;
import com.ftseoul.visitor.exception.error.PhoneDuplicatedException;
import com.ftseoul.visitor.exception.error.ResourceNotFoundException;
import com.ftseoul.visitor.policy.ReserveType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class ReserveServiceMockTest extends MockReserveBaseTest{


    @DisplayName("단순예약_성공테스트")
    @Test
    void reserveSuccessTest(){
        //given
        final List<VisitorDto> visitors = new ArrayList<>();
        visitors.add(new VisitorDto());
        ReserveVisitorDto reserveRequestDto = new ReserveVisitorDto("개포", "김길동", "테스트", LocalDateTime.now(), ReserveType.REPRESENTATIVE,visitors);

       //when

        doReturn(reserve).when(reserveRepository).save(any(Reserve.class));
        final Reserve reserve = reserveService.saveReserve(reserveRequestDto, 1L);


        //then
        Assertions.assertThat(reserve.getPurpose()).isEqualTo(reserve.getPurpose());
        Assertions.assertThat(reserve.getTargetStaff()).isEqualTo(1L);

        // verify
        verify(reserveRepository, times(1)).save(any(Reserve.class));
    }

    @DisplayName("단순조회_성공테스트")
    @Test
    void findReserveSuccessTest(){

        //given
        List<Visitor> visitors = new ArrayList<>();
        visitors.add(visitor);
        StaffDecryptDto staffDecryptDto = new StaffDecryptDto(1L, "abcde", "01012345678","시설관리");

        //when

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
    @DisplayName("예약수정_성공테스트")
    void changeReserveSuccessTest(){

        //given
            List<VisitorModifyDto> visitorModifyDtos = new ArrayList<>();
            visitorModifyDtos.add(new VisitorModifyDto());
            String modifiedPlace = "서초";
            long findId = 1L;

        ReserveModifyDto reserveModifyDto= new ReserveModifyDto(findId,
                modifiedPlace, seed.encrypt("jaehchoi"), "수정테스트", LocalDateTime.now(), visitorModifyDtos);

        //when
            doReturn(Optional.of(reserve)).when(reserveRepository).findById(findId);
            final Reserve updatedReserve = this.reserveService.updateReserve(reserveModifyDto, findId);

        //then
            Assertions.assertThat(updatedReserve.getPlace()).isEqualTo(modifiedPlace);

        //verify
            verify(reserveRepository, times(1)).save(any());
            verify(reserveRepository, times(1)).findById(any());
    }


    @Test
    @DisplayName("예약_수정_오류")
    void changeReserveThrowsException(){
        //when
        List<VisitorModifyDto> visitorModifyDtos = new ArrayList<>();
        visitorModifyDtos.add(new VisitorModifyDto());
        String modifiedPlace = "서초";
        long findId = 2L;

        ReserveModifyDto reserveModifyDto= new ReserveModifyDto(findId,
                modifiedPlace, seed.encrypt("jaehchoi"), "수정테스트", LocalDateTime.now(), visitorModifyDtos);

        //given
        doThrow(ResourceNotFoundException.class)
                .when(reserveRepository)
                .findById(any());

        //when && then
        Assertions.assertThatThrownBy(() -> this.reserveService.updateReserve(reserveModifyDto, findId))
                .isInstanceOf(ResourceNotFoundException.class);

        //verify
        verify(reserveRepository, times(1)).findById(any());
        verify(reserveRepository, times(0)).save(any());

    }

    @Test
    @DisplayName("핸드폰_번호_중복_처리")
    void checkDuplicatePhoneNumber() throws Exception{
        //when
        List<VisitorDto> visitors = new ArrayList<>();
        String phoneNumber = "01095432121";
        VisitorDto visitorDto1 = new VisitorDto("1", phoneNumber, "42");
        VisitorDto visitorDto2 = new VisitorDto("1", phoneNumber, "42");
        visitors.add(visitorDto1);
        visitors.add(visitorDto2);


        //when && then
        Assertions.assertThatThrownBy(() -> this.reserveService.checkDuplicatedPhone(visitors))
                .isInstanceOf(PhoneDuplicatedException.class);
    }

    @Test
    @DisplayName("예약_삭제_성공")
    void DeleteReserveReturnCode2000(){
        //given
        long saveId = 1L;
        List<Visitor> visitors = new ArrayList<>();
        visitors.add(new Visitor());

        //when
        doReturn(Optional.of(reserve)).when(reserveRepository).findById(saveId);
        final Response response = this.reserveService.deleteById(saveId);

        //then
        Assertions.assertThat(response.getCode()).isEqualTo("2000");
    }

    @Test
    @DisplayName("존재하지않는_예약_삭제_실패")
    void deleteReserveReturnCode4000(){
        //given
        long saveId = 2L;
        List<Visitor> visitors = new ArrayList<>();
        visitors.add(new Visitor());

        //when
        doReturn(Optional.empty()).when(reserveRepository).findById(saveId);
        final Response response = this.reserveService.deleteById(saveId);

        //then
        Assertions.assertThat(response.getCode()).isEqualTo("4000");
    }

    @Test
    @DisplayName("스태프_아이디로_예약_삭제")
    void deleteReserveStaff(){
        //given
        long saveId = 1L;
        List<Reserve> reserveList = new ArrayList<>();
        reserveList.add(reserve);

        //when
        doReturn(reserveList).when(reserveRepository).findAllByTargetStaff(1L);
        doNothing().when(visitorRepository).deleteAllByReserveId(1L);
        doNothing().when(reserveRepository).delete(reserve);
        this.reserveService.deleteAllByStaffId(1L);

        //verify
        verify(visitorRepository, times(1)).deleteAllByReserveId(any());
        verify(reserveRepository, times(1)).delete(any());
    }

    /**
     * Method Test : webSocketService doNothing();
     */
    @Test
    @DisplayName("예약_방문자_삭제")
    void deleteVisitorInReserve(){

        //when
        String deleteName = "지울이름";
        String deletePhoneNumber = "01055555555";

        ReserveRequestDto reserveRequestDto= new ReserveRequestDto(deletePhoneNumber, deleteName);
        List<Visitor> visitors = new ArrayList<>();
        visitors.add(visitor);

        //given
        doReturn(visitors).when(visitorRepository).findAllByReserveId(1L);
        doNothing().when(reserveRepository).delete(any());
        doReturn(Optional.of(reserve)).when(reserveRepository).findById(1L);;

        /**
         *
         */
        doNothing().when(webSocketService).sendMessageToSubscriber(any(), any());

        final boolean flag = this.reserveService.visitorReserveDelete(1L,reserveRequestDto);

        //then
        Assertions.assertThat(flag).isTrue();
    }
}
