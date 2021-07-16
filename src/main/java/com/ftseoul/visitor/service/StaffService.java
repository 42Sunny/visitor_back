package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.data.StaffRepository;
import com.ftseoul.visitor.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffService {
    private final StaffRepository staffRepository;

    public Staff findById(Long id) {
        return staffRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", id));
    }

    public Staff findByName(String name) {
        return staffRepository
                .findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", "name", name));
    }
}
