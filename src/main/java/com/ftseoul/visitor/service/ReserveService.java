package com.ftseoul.visitor.service;

import com.ftseoul.visitor.data.ReserveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReserveService {

    private final ReserveRepository repository;
}
