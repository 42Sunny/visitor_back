package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.data.VisitorRepository;
import com.ftseoul.visitor.dto.reserve.ReserveModifyDto;
import com.ftseoul.visitor.dto.visitor.VisitorDto;
import com.ftseoul.visitor.dto.visitor.VisitorModifyDto;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VisitorService {

    private final VisitorRepository visitorRepository;

    public List<Visitor> saveVisitors(Long reserveId, List<VisitorDto> visitorDto) {
        log.info("Reserve Id is {}", reserveId);
        List<Visitor> visitors = visitorDto
            .stream()
            .map(v -> new Visitor(reserveId, v.getName(), v.getPhone(), v.getOrganization()))
            .collect(Collectors.toList());
        return visitorRepository.saveAll(visitors);
    }

    public List<Visitor> updateVisitors(ReserveModifyDto modifyDto) {
        Long reserveId = modifyDto.getReserveId();
        List<VisitorModifyDto> visitorList = modifyDto.getVisitor();
        updateDeletedVisitors(visitorList, reserveId);
        List<Visitor> newVisitors = updateNewVisitors(visitorList, reserveId);
        log.info("send message to: " + newVisitors);
        return newVisitors;
    }

    private void updateDeletedVisitors(List<VisitorModifyDto> visitors, Long reserveId) {
       List<String> phoneLists = visitors
           .stream()
           .map(VisitorModifyDto::getPhone)
           .collect(Collectors.toList());
       log.info("id: " + reserveId + "\nvisitor update: " + visitors);
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