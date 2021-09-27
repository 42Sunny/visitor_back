package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Reserve;
import com.ftseoul.visitor.data.ReserveRepository;
import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.data.StaffRepository;
import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.data.VisitorRepository;
import com.ftseoul.visitor.dto.ReserveRequestDto;
import com.ftseoul.visitor.dto.ReserveListResponseDto;
import com.ftseoul.visitor.dto.ReserveModifyDto;
import com.ftseoul.visitor.dto.ReserveVisitorDto;
import com.ftseoul.visitor.dto.ShortUrlDto;
import com.ftseoul.visitor.dto.StaffDto;
import com.ftseoul.visitor.dto.VisitorDecryptDto;
import com.ftseoul.visitor.dto.VisitorDto;
import com.ftseoul.visitor.dto.payload.Response;
import com.ftseoul.visitor.encrypt.Seed;
import com.ftseoul.visitor.exception.PhoneDuplicatedException;
import com.ftseoul.visitor.exception.ResourceNotFoundException;
import com.ftseoul.visitor.service.sns.SMSService;
import com.ftseoul.visitor.websocket.WebSocketService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ReserveService {

    private final ReserveRepository reserveRepository;
    private final VisitorRepository visitorRepository;
    private final StaffService staffService;
    private final StaffRepository staffRepository;
    private final VisitorService visitorService;
    private final SMSService smsService;
    private final ShortUrlService shortUrlService;
    private final Seed seed;
    private final WebSocketService socketService;

    public ReserveListResponseDto findById(Long id) {
        Reserve reserve = reserveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserve", "id", id));
        List<VisitorDecryptDto> visitor = visitorRepository.findAllByReserveId(id)
                .stream().map(visitor1 -> VisitorDecryptDto.builder()
                .reserveId(visitor1.getReserveId())
                .phone(visitor1.getPhone())
                .name(visitor1.getName())
                .organization(visitor1.getOrganization())
                .build().decryptDto(seed)).collect(Collectors.toList());
        return ReserveListResponseDto.builder()
                .staff(staffService.decrypt(staffRepository.findById(reserve.getTargetStaff())
                        .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", reserve.getTargetStaff()))))
                .place(reserve.getPlace())
                .date(reserve.getDate())
                .id(reserve.getId())
                .purpose(reserve.getPurpose())
                .visitor(visitor)
                .build();
    }

    public List<ReserveListResponseDto> findReservesByNameAndPhone(ReserveRequestDto requestDto) {
        log.info("Search reserve lists by name and phone\nname: {}, phone: {}", seed.encrypt(requestDto.getName()), seed.encrypt(requestDto.getPhone()));
        List<Visitor> visitorList = visitorRepository.findAllByNameAndPhone(seed.encrypt(requestDto.getName()), seed.encrypt(requestDto.getPhone()));
        List<ReserveListResponseDto> response = new ArrayList<>();
        for (int i = 0; i < visitorList.size(); i++) {
            int finalI = i;
            Reserve reserve = reserveRepository.findById(visitorList.get(i).getReserveId())
                    .orElseThrow(() -> new ResourceNotFoundException("Reserve", "id", visitorList.get(finalI).getReserveId()));
            List<VisitorDecryptDto> visitors = visitorRepository.findAllByReserveId(reserve.getId())
                    .stream().map(v -> VisitorDecryptDto.builder()
                            .reserveId(v.getReserveId())
                            .name(v.getName())
                            .phone(v.getPhone())
                            .organization(v.getOrganization())
                            .build().decryptDto(seed)).collect(Collectors.toList());
            response
                    .add(ReserveListResponseDto.builder()
                            .id(reserve.getId())
                            .date(reserve.getDate())
                            .place(reserve.getPlace())
                            .purpose(reserve.getPurpose())
                            .staff(staffService.decrypt(staffRepository.findById(reserve.getTargetStaff())
                                    .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", reserve.getTargetStaff()))))
                            .visitor(visitors)
                            .build());
        }
        return response;
    }

    private boolean deleteVisitorInList(List<Visitor> list, ReserveRequestDto requestDto) {
        Optional<Visitor> toDeleteVisitor = list.stream().filter((visitor) ->
                (visitor.getName().equals(seed.encrypt(requestDto.getName())))
                    && (visitor.getPhone().equals(seed.encrypt(requestDto.getPhone()))))
            .findAny();
        if (toDeleteVisitor.isEmpty()) {
            log.error("입력한 정보와 일치하는 방문자가 해당예약에 존재하지 않습니다");
            return false;
        }
        visitorRepository.delete(toDeleteVisitor.get());
        log.info("Visitor delete: " + toDeleteVisitor.get());
        return true;
    }

    public boolean visitorReserveDelete(Long reserveId, ReserveRequestDto requestDto) {
        log.info("Delete Reserve Id: {}", reserveId);
        log.info("Delete Visitors: {}", requestDto);
        List<Visitor> list = visitorRepository.findAllByReserveId(reserveId);
        if (list.size() == 0) {
            log.error("예약번호 {}에 해당하는 방문자가 존재하지 않습니다", reserveId.toString());
            return false;
        }
        boolean result = deleteVisitorInList(list, requestDto);

        if (list.size() == 1) {
            log.info("Reserve delete: " + reserveId);
            reserveRepository.delete(reserveRepository.findById(reserveId).get());
            socketService.sendMessageToSubscriber("/visitor",
                "예약 번호: "+ reserveId + " 예약및 방문자가 삭제되었습니다");
        } else {
            socketService.sendMessageToSubscriber("/visitor", "예약 번호: "+ reserveId + " 에 해당하는 방문자를 삭제했습니다");
        }
        return result;
    }

    public Reserve saveReserve(ReserveVisitorDto reserveVisitorDto){
        checkDuplicatedPhone(reserveVisitorDto.getVisitor());
        Staff staff = staffService.findByName(reserveVisitorDto.getTargetStaffName());
        Reserve reserve = reserveRepository.save(Reserve.builder()
                .targetStaff(staff.getId())
                .place(reserveVisitorDto.getPlace())
                .purpose(reserveVisitorDto.getPurpose())
                .date(reserveVisitorDto.getDate())
                .build());
        log.info("Reserve Saved: {}", reserve);
        List<Visitor> visitors = visitorService.saveVisitors(reserve.getId(), reserveVisitorDto.getVisitor());
        log.info("Saved Visitors: {}", visitors);
        StaffDto staffReserveInfo = new StaffDto(reserve.getId(), seed.decrypt(staff.getPhone()),
            reserveVisitorDto.getPurpose(), reserveVisitorDto.getPlace(), reserveVisitorDto.getDate(),
            visitors);
        List<ShortUrlDto> shortUrlDtoList = shortUrlService.createShortUrlDtoList(visitors, staffReserveInfo);
        smsService.sendMessages(shortUrlDtoList, staffReserveInfo);
        log.info("Send text message to visitors and staff");
        return reserve;
    }

    public boolean updateReserve(ReserveModifyDto reserveModifyDto) {
        Reserve reserve = reserveRepository
            .findById(reserveModifyDto.getReserveId())
            .orElseThrow(() -> new ResourceNotFoundException("Reserve", "id", reserveModifyDto.getReserveId()));
        Staff staff = staffRepository.findByName(seed.encrypt(reserveModifyDto.getTargetStaffName()))
            .orElseThrow(() -> new ResourceNotFoundException("Staff", "name", reserveModifyDto.getTargetStaffName()));
        reserve.update(reserveModifyDto.getPlace(), staff.getId(),
            reserveModifyDto.getPurpose(), reserveModifyDto.getDate());
        log.info("Updated reserve: {}", reserve);
        reserveRepository.save(reserve);
        reserveModifyDto.encrypt(seed);
        List<Visitor> visitors = visitorService.updateVisitors(reserveModifyDto);
        log.info("Updated visitors: {}", visitors);
        StaffDto staffReserveInfo = new StaffDto(reserve.getId(), seed.decrypt(staff.getPhone()),
            reserveModifyDto.getPurpose(), reserveModifyDto.getPlace(),
            reserveModifyDto.getDate(), visitors);
        List<ShortUrlDto> shortUrlDtoList = shortUrlService.createShortUrlDtoList(visitors, staffReserveInfo);
        smsService.sendMessages(shortUrlDtoList, staffReserveInfo);
        log.info("Send text messages to visitors and staff");
        return true;
    }

    public void checkDuplicatedPhone(List<VisitorDto> visitorDto) {
        log.info("Check phone Duplication");
        Set<String> phones = new HashSet<>();
        List<VisitorDto> collected = visitorDto
            .stream()
            .filter(visitor -> !phones.add(visitor.getPhone()))
            .collect(Collectors.toList());
        if (collected.size() > 0) {
            throw new PhoneDuplicatedException("전화번호 중복");
        }
    }

    public Response deleteById(Long id) {
        Optional<Reserve> reserve = reserveRepository.findById(id);
        if (reserve.isEmpty()) {
            return new Response("4000", "예약정보가 존재하지 않습니다");
        }
        reserveRepository.deleteById(id);
        visitorRepository.deleteAllByReserveId(id);
        return new Response("2000", "예약이 삭제되었습니다");
    }

}
