package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.*;
import com.ftseoul.visitor.dto.ReserveDeleteRequestDto;
import com.ftseoul.visitor.dto.ReserveModifyDto;
import com.ftseoul.visitor.dto.ReserveResponseDto;
import com.ftseoul.visitor.dto.ReserveVisitorDto;
import com.ftseoul.visitor.dto.SearchReserveRequestDto;
import com.ftseoul.visitor.exception.ResourceNotFoundException;
import java.util.Optional;
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
        if (visitorRepository.findAllByName(reserveRequestDto.getName()).size() == 0)
        {
            throw new ResourceNotFoundException("Visitor", "name", reserveRequestDto.getName());
        }
        if (visitorRepository.findAllByPhone(reserveRequestDto.getPhone()).size() == 0)
        {
            throw new ResourceNotFoundException("Visitor", "phone", reserveRequestDto.getPhone());
        }
        List<Visitor> visitorList = visitorRepository.findAllByNameAndPhone(reserveRequestDto.getName(),
                reserveRequestDto.getPhone());
        List<ReserveResponseDto> reserveList = new ArrayList<>();
        for (int i = 0; i < visitorList.size(); i++) {
            int finalI = i;
            Reserve reserve = reserveRepository.findById(visitorList.get(i).getReserveId())
                    .orElseThrow(() -> new ResourceNotFoundException("Reserve", "id", visitorList.get(finalI).getReserveId()));
            reserveList.add(ReserveResponseDto.builder()
                    .staff(staffRepository.findById(reserve.getTargetStaff())
                            .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", reserve.getTargetStaff())))
                    .visitor(visitorList.get(i))
                    .date(reserve.getDate())
                    .id(reserve.getId())
                    .purpose(reserve.getPurpose())
                    .place(reserve.getPlace())
                    .build());

        }
        return reserveList;
    }

    public boolean reserveDelete(Long reserve_id, ReserveDeleteRequestDto requestDto) {
        if (visitorRepository.findAllByName(requestDto.getName()).size() == 0)
        {
            throw new ResourceNotFoundException("Visitor", "name", requestDto.getName());
        }
        if (visitorRepository.findAllByPhone(requestDto.getPhone()).size() == 0)
        {
            throw new ResourceNotFoundException("Visitor", "phone", requestDto.getPhone());
        }
        List<Visitor> list = visitorRepository.findAllByReserveId(reserve_id);
        if (list.size() > 1) {
            for (Visitor v : list) {
            }
            Visitor v = visitorRepository.findByNameAndPhoneAndReserveId(requestDto.getName(), requestDto.getPhone(), reserve_id)
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Visitor", "name", requestDto.getName())
                    );
            visitorRepository.delete(v);
        } else {
            reserveRepository.delete(findById(reserve_id));
        }
        return true;
    }

    public Reserve saveReserve(ReserveVisitorDto reserveVisitorDto){
        Reserve reserve = Reserve.builder()
            .targetStaff(reserveVisitorDto.getTargetStaff())
            .place(reserveVisitorDto.getPlace())
            .purpose(reserveVisitorDto.getPurpose())
            .date(reserveVisitorDto.getDate())
            .build();
        return reserveRepository.save(reserve);
    }

    public boolean updateReserve(ReserveModifyDto reserveModifyDto) {
        Reserve reserve = reserveRepository
            .findById(reserveModifyDto.getReserveId())
            .orElseThrow(() -> new ResourceNotFoundException("Reserve", "id", reserveModifyDto.getReserveId()));
        reserve.update(reserveModifyDto.getPlace(), reserveModifyDto.getTargetStaff(),
            reserveModifyDto.getPurpose(), reserveModifyDto.getDate());
        reserveRepository.save(reserve);
        return true;
    }

}
