package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.*;
import com.ftseoul.visitor.dto.*;
import com.ftseoul.visitor.exception.ResourceNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ReserveService {

    private final ReserveRepository reserveRepository;
    private final VisitorRepository visitorRepository;
    private final StaffRepository staffRepository;

    public ReserveListResponseDto findById(Long id) {
        Reserve reserve = reserveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserve", "id", id));
        return ReserveListResponseDto.builder()
                .staff(staffRepository.findById(reserve.getTargetStaff())
                        .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", reserve.getTargetStaff())))
                .place(reserve.getPlace())
                .date(reserve.getDate())
                .id(reserve.getId())
                .purpose(reserve.getPurpose())
                .visitor(visitorRepository.findAllByReserveId(id))
                .build();
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
        log.info("reserve delete");
        if (requestDto == null) {
            if (visitorRepository.findAllByReserveId(reserve_id).size() > 0) {
                reserveRepository.deleteById(reserve_id);
                visitorRepository.deleteAllByReserveId(reserve_id);
            }
            return true;
        }
        if (visitorRepository.findAllByName(requestDto.getName()).size() == 0)
        {
            throw new ResourceNotFoundException("Visitor", "name", requestDto.getName());
        }
        if (visitorRepository.findAllByPhone(requestDto.getPhone()).size() == 0)
        {
            throw new ResourceNotFoundException("Visitor", "phone", requestDto.getPhone());
        }
        List<Visitor> list = visitorRepository.findAllByReserveId(reserve_id);
        if (list.size() == 0) {
            throw new ResourceNotFoundException("Reserve", "id", reserve_id);
        }
        else {
            Visitor v = visitorRepository.findByNameAndPhoneAndReserveId(requestDto.getName(), requestDto.getPhone(), reserve_id)
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Visitor", "name", requestDto.getName())
                    );
            visitorRepository.delete(v);
        }
        if (list.size() == 1) {
            reserveRepository.delete(reserveRepository.findById(reserve_id).get());
        }
        return true;
    }

    public Reserve saveReserve(ReserveVisitorDto reserveVisitorDto){
        Reserve reserve = Reserve.builder()
                .targetStaff(staffRepository.findByName(reserveVisitorDto.getTargetStaffName())
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Staff", "name", reserveVisitorDto.getTargetStaffName())
                        ).getId())
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
        Staff staff = staffRepository.findByName(reserveModifyDto.getTargetStaffName())
            .orElseThrow(() -> new ResourceNotFoundException("Staff", "name", reserveModifyDto.getTargetStaffName()));
        reserve.update(reserveModifyDto.getPlace(), staff.getId(),
            reserveModifyDto.getPurpose(), reserveModifyDto.getDate());
        reserveRepository.save(reserve);
        return true;
    }

}
