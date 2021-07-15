package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.*;
import com.ftseoul.visitor.dto.ReserveResponseDto;
import com.ftseoul.visitor.dto.SearchReserveRequestDto;
import com.ftseoul.visitor.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReserveService {

    private final ReserveRepository reserveRepository;
    private final VisitorRepository visitorRepository;
    private final StaffRepository staffRepository;

    public Reserve findById(Long id) {
        return reserveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserve", "id", id));
    }

    public List<ReserveResponseDto> findAllByNameAndPhone(SearchReserveRequestDto reserveRequestDto) {
        visitorRepository.findByName(reserveRequestDto.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Visitor", "name", reserveRequestDto.getName()));
        visitorRepository.findByPhone(reserveRequestDto.getPhone())
                .orElseThrow(() -> new ResourceNotFoundException("Visitor", "phone", reserveRequestDto.getPhone()));
        List<Visitor> visitorList = visitorRepository.findAllByNameAndPhone(reserveRequestDto.getName(),
                reserveRequestDto.getPhone());
        List<ReserveResponseDto> reserveList = new ArrayList<>();
        for (int i = 0; i < visitorList.size(); i++) {
            int finalI = i;
            Reserve reserve = reserveRepository.findById(visitorList.get(i).getReserve_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Reserve", "id", visitorList.get(finalI).getReserve_id()));
            ReserveResponseDto.builder()
                    .staff(staffRepository.findById(reserve.getTargetStaff())
                            .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", reserve.getTargetStaff())))
                    .visitor(visitorList.get(i))
                    .date(reserve.getDate())
                    .id(reserve.getId())
                    .purpose(reserve.getPurpose())
                    .place(reserve.getPlace())
                    .build();

        }
        return reserveList;
    }
}
