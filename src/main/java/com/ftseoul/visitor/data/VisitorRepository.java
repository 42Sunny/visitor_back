package com.ftseoul.visitor.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    List<Visitor> findAllByNameAndPhone(String name, String phone);
    Optional<Visitor> findByName(String name);
    Optional<Visitor> findByPhone(String phone);
    List<Visitor> findAllByName(String name);
    List<Visitor> findAllByPhone(String phone);

    List<Visitor> findAllByReserveId(Long reserve_id);

    Optional<Visitor> findByNameAndPhoneAndReserveId(String name, String phone, Long reserveId);

    void deleteAllByReserveId(Long reserveId);
}
