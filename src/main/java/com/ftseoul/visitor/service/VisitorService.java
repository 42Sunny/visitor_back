package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.data.VisitorRepository;
import com.ftseoul.visitor.dto.ReserveModifyDto;
import com.ftseoul.visitor.dto.VisitorDto;
import com.ftseoul.visitor.dto.VisitorModifyDto;
import com.ftseoul.visitor.service.sns.SMSService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final SMSService smsService;
    private final QRcodeService qrcodeService;

    public List<Visitor> saveVisitors(Long reserveId, List<VisitorDto> visitorDto) {
        List<Visitor> visitors = visitorDto
            .stream()
            .map(v -> new Visitor(reserveId, v.getName(), v.getPhone(), v.getOrganization()))
            .collect(Collectors.toList());
        return visitorRepository.saveAll(visitors);
    }

    public boolean updateVisitors(ReserveModifyDto modifyDto) {
        Long reserveId = modifyDto.getReserveId();
        List<VisitorModifyDto> visitorList = modifyDto.getVisitor();
        updateDeletedVisitors(visitorList, reserveId);
        List<Visitor> newVisitors = updateNewVisitors(visitorList, reserveId);
        smsService.sendMessages(newVisitors);
        return true;
    }

    private void updateDeletedVisitors(List<VisitorModifyDto> visitors, Long reserveId) {
       List<String> phoneLists = visitors
           .stream()
           .map(VisitorModifyDto::getPhone)
           .collect(Collectors.toList());
       visitorRepository.updateDeletedVisitors(phoneLists, reserveId);
   }

    private List<Visitor> updateNewVisitors(List<VisitorModifyDto> visitors, Long reserveId) {
       List<Visitor> newVisitors = visitors
           .stream()
           .filter(VisitorModifyDto::isChanged)
           .map(v -> new Visitor(reserveId, v.getName(), v.getPhone(), v.getOrganization()))
           .collect(Collectors.toList());
       checkDuplicatedPhone(newVisitors);
       return visitorRepository.saveAll(newVisitors);
   }

    private void checkDuplicatedPhone(List<Visitor> newVisitors) {
       for (Visitor newVisitor : newVisitors) {
           Optional<Visitor> duplicatedVisitor = visitorRepository
               .findByReserveIdAndPhone(newVisitor.getReserveId(),
                   newVisitor.getPhone());
           duplicatedVisitor.ifPresent(visitorRepository::delete);
       }
   }
}