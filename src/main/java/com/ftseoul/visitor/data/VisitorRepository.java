package com.ftseoul.visitor.data;

import com.ftseoul.visitor.dto.VisitorModifyDto;
import java.util.Collection;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    List<Visitor> findAllByNameAndPhone(String name, String phone);
    Optional<Visitor> findByReserveIdAndPhone(Long reserveId, String phone);
    List<Visitor> findAllByName(String name);
    List<Visitor> findAllByPhone(String phone);
    List<Visitor> findAllByReserveId(Long reserve_id);

    Optional<Visitor> findByNameAndPhoneAndReserveId(String name, String phone, Long reserveId);

    void deleteAllByReserveId(Long reserveId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Visitor v WHERE v.reserveId = :id AND v.phone NOT IN :toDelete")
    void updateDeletedVisitors(@Param("toDelete") Collection<String> toDelete, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Visitor v Set v.status = '만료' "
        + "WHERE current_date > (SELECT DISTINCT r.date FROM Reserve r WHERE r.id = v.reserveId) "
        + "AND v.status = '대기'")
    int updateExpiredVisitors();
}