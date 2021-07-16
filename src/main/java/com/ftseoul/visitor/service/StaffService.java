package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.Staff;
import com.ftseoul.visitor.data.StaffRepository;
import com.ftseoul.visitor.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;

    public Staff findById(Long id) {
        return staffRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Staff", "id", id));
    }
}
