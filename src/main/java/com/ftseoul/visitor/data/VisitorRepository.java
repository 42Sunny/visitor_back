package com.ftseoul.visitor.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    List<Visitor> findAllByNameAndPhone(String name, String phone);
    Optional<Visitor> findByName(String name);
    Optional<Visitor> findByPhone(String phone);
}
