package com.ftseoul.visitor.mockService;

import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.dto.reserve.ReserveModifyDto;
import com.ftseoul.visitor.dto.visitor.VisitorDto;
import com.ftseoul.visitor.dto.visitor.VisitorModifyDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class VisitorServiceMockTest extends MockVisitorBaseTest {


    @Test
    @DisplayName("방문자_생성")
    void createVisitor(){

        //given
        List<VisitorDto> visitorDtoList = new ArrayList<>();
        VisitorDto visitorDto1 = new VisitorDto(seed.encrypt("김수환무1")
                , seed.encrypt("01011111111"), "42서울1");
        VisitorDto visitorDto2 = new VisitorDto(seed.encrypt("김수환무2")
                , seed.encrypt("01022222222"), "42서울2");

        visitorDtoList.add(visitorDto1);
        visitorDtoList.add(visitorDto2);

        int result = visitorDtoList.size();
        Visitor visitor = Visitor.builder()
                .name(seed.encrypt("김수환무1"))
                .phone(seed.encrypt("01011111111"))
                .organization("42서울1")
                .build();

        Visitor visitor2 = Visitor.builder()
                .name(seed.encrypt("김수환무1"))
                .phone(seed.encrypt("01022222222"))
                .organization("42서울2")
                .build();
        List<Visitor> visitors = new ArrayList<>();
        visitors.add(visitor);
        visitors.add(visitor2);

        //when
        doReturn(visitors).when(visitorRepository).saveAll(anyList());
        final List<Visitor> savedVisitor = visitorService.saveVisitors(reserve.getId(), visitorDtoList);

        //then
        Assertions.assertThat(savedVisitor.size()).isEqualTo(result);
        Assertions.assertThat(savedVisitor.get(0).getName()).isEqualTo(visitorDto1.getName());

        //verify
        verify(visitorRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("방문자 수정")
    void changeVisitorInformation(){

        //given
        List<VisitorModifyDto> visitors = new ArrayList<>();

        VisitorModifyDto v1 = new VisitorModifyDto(reserve.getId(),
                seed.encrypt("김수한무1"),
                seed.encrypt("01011111111"),
                "이노아카", false);
        VisitorModifyDto v2 = new VisitorModifyDto(reserve.getId(),
                seed.encrypt("김수한무2"),
                seed.encrypt("01022222222"), "이노아카",false);
        VisitorModifyDto v3 = new VisitorModifyDto(reserve.getId(),
                seed.encrypt("김수한무3"),
                seed.encrypt("01033333333"), "이노아카",true);
        visitors.add(v1);
        visitors.add(v2);
        visitors.add(v3);

        long result = visitors.stream()
                .filter(v -> v.isChanged())
                .count();

        ReserveModifyDto request = new ReserveModifyDto(reserve.getId(),
                reserve.getPlace(), staff.getName(), reserve.getPurpose(),
                LocalDateTime.now(), visitors);

        //when
        doNothing().when(visitorRepository).updateDeletedVisitors(any(), any());
        doReturn(visitors.stream()
                .filter(VisitorModifyDto::isChanged)
                .collect(Collectors.toList())).when(visitorRepository).saveAll(any());
        final List<Visitor> changeVisitors = this.visitorService.updateVisitors(request);

        //then
        Assertions.assertThat(result).isEqualTo(changeVisitors.size());

        //verify
        verify(visitorRepository, times(1)).updateDeletedVisitors(any(), any());
        verify(visitorRepository, times(1)).saveAll(any());
    }

}
