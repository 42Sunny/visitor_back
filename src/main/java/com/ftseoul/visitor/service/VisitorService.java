package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.data.VisitorRepository;
import com.ftseoul.visitor.dto.ReserveModifyDto;
import com.ftseoul.visitor.dto.VisitorDto;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitorService {

    private final VisitorRepository visitorRepository;

    public List<Visitor> saveVisitors(Long reserveId, List<VisitorDto> visitorDto) {
        List<Visitor> visitors = visitorDto
            .stream()
            .map(v -> new Visitor(reserveId, v.getName(), v.getPhone(), v.getOrganization()))
            .collect(Collectors.toList());
        return visitorRepository.saveAll(visitors);
    }

    @Transactional
    public boolean updateVisitors(ReserveModifyDto modifyDto) {
        visitorRepository.deleteAllByReserveId(modifyDto.getReserveId());
        saveVisitors(modifyDto.getReserveId(), modifyDto.getVisitor());
        return true;
    }
}