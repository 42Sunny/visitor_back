package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.*;
import com.ftseoul.visitor.dto.*;
import com.ftseoul.visitor.exception.ResourceNotFoundException;
import com.ftseoul.visitor.service.sns.SMSService;
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
    private final SMSService smsService;
    private final QRcodeService qrCodeService;

    public ReserveListResponseDto findById(Long id) {
        log.info("findById: " + id.toString());
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
        checkExistVisitorName(reserveRequestDto.getName(), reserveRequestDto.getPhone());
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

    public List<ReserveListResponseDto> findReserveByVisitor(SearchReserveRequestDto requestDto) {
        checkExistVisitorName(requestDto.getName(), requestDto.getPhone());
        List<Visitor> visitorList = visitorRepository.findAllByNameAndPhone(requestDto.getName(), requestDto.getPhone());
        List<ReserveListResponseDto> responseDtos = new ArrayList<>();
        for (int i = 0; i < visitorList.size(); i++) {
            int finalI = i;
            Reserve reserve = reserveRepository.findById(visitorList.get(i).getReserveId())
                    .orElseThrow(() -> new ResourceNotFoundException("Reserve", "id", visitorList.get(finalI).getReserveId()));
            responseDtos
                    .add(ReserveListResponseDto.builder()
                            .id(reserve.getId())
                            .date(reserve.getDate())
                            .place(reserve.getPlace())
                            .purpose(reserve.getPurpose())
                            .staff(staffRepository.findById(reserve.getTargetStaff())
                                    .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", reserve.getTargetStaff())))
                            .visitor(visitorRepository.findAllByReserveId(reserve.getId()))
                            .build());
        }
        return responseDtos;
    }

    private void checkExistVisitorName(String name, String phone) {
        if (visitorRepository.findAllByName(name).size() == 0)
        {
            log.error("visitor name is not found");
            throw new ResourceNotFoundException("Visitor", "name", name);
        }
        if (visitorRepository.findAllByPhone(phone).size() == 0)
        {
            log.error("visitor phone is not phone");
            throw new ResourceNotFoundException("Visitor", "phone", phone);
        }
    }

    public boolean reserveDelete(Long reserve_id, ReserveDeleteRequestDto requestDto) {
        if (requestDto == null) {
            log.info("dto is null, reserve_id: " + reserve_id.toString());
            if (visitorRepository.findAllByReserveId(reserve_id).size() > 0) {
                reserveRepository.deleteById(reserve_id);
                visitorRepository.deleteAllByReserveId(reserve_id);
            }
            return true;
        }
        checkExistVisitorName(requestDto.getName(), requestDto.getPhone());
        List<Visitor> list = visitorRepository.findAllByReserveId(reserve_id);
        if (list.size() == 0) {
            log.error("reserve id is not found: " + reserve_id.toString());
            throw new ResourceNotFoundException("Reserve", "id", reserve_id);
        }
        else {
            Visitor v = visitorRepository.findByNameAndPhoneAndReserveId(requestDto.getName(), requestDto.getPhone(), reserve_id)
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Visitor", "name", requestDto.getName())
                    );
            visitorRepository.delete(v);
            log.info("Visitor delete: " + v);
        }
        if (list.size() == 1) {
            log.info("Reserve delete: " + reserve_id);
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
        log.info("save repository: " + reserve);
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
        log.info("reserve update: " + reserve);
        reserveRepository.save(reserve);
        smsService.sendMessage(new StaffDto(reserve.getId(), staff.getPhone()));
        return true;
    }

}
