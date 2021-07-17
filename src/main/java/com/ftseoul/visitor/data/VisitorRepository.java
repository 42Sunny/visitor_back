package com.ftseoul.visitor.data;

import com.ftseoul.visitor.dto.VisitorModifyDto;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    List<Visitor> findAllByNameAndPhone(String name, String phone);
    Optional<Visitor> findByName(String name);
    Optional<Visitor> findByPhone(String phone);
    Optional<Visitor> findByReserveIdAndPhone(Long reserveId, String phone);
    List<Visitor> findAllByName(String name);
    List<Visitor> findAllByPhone(String phone);
    List<Visitor> findAllByReserveId(Long reserve_id);

    Optional<Visitor> findByNameAndPhoneAndReserveId(String name, String phone, Long reserveId);

    void deleteAllByReserveId(Long reserveId);

    @Query("DELETE FROM Visitor v WHERE v.id = :id AND v.phone NOT IN :toDelete")
    void updateDeletedVisitors(@Param("toDelete") Collection<String> toDelete, @Param("id") Long id);
}
