package com.ftseoul.visitor.controller;

import com.ftseoul.visitor.data.Reserve;
import com.ftseoul.visitor.service.ReserveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReserveController {

    private final ReserveService reserveService;

    @GetMapping("/reserve/{id}")
    public Reserve findById(@PathVariable Long id) {
        return reserveService.findById(id);
    }
}
