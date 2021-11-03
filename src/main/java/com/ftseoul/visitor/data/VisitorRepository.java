package com.ftseoul.visitor.data;

import com.ftseoul.visitor.dto.visitor.projection.CheckInVisitor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    List<Visitor> findAllByNameAndPhone(String name, String phone);
    Optional<Visitor> findByReserveIdAndPhone(Long reserveId, String phone);
    List<Visitor> findAllByReserveId(Long reserveId);
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

    @Query(nativeQuery = true,
        value = "SELECT DATE_FORMAT(v.check_in_time, '%Y-%m-%d') AS checkInDate, "
            + "v.check_in_time AS checkIn, "
            + "v.name AS name, "
            + "v.phone AS phone, "
            + "s.name AS staffName, "
            + "s.phone AS staffPhone, "
            + "s.department AS staffDepartment, "
            + "r.purpose AS purpose, "
            + "r.place AS place "
            + "FROM visitor v "
            + "INNER JOIN reserve r "
            + "ON v.reserve_id = r.id "
            + "INNER JOIN staff s "
            + "ON r.target_staff = s.id "
            + "WHERE v.check_in_time BETWEEN :start AND :end "
            + "ORDER BY checkInDate, checkIn DESC",
        countQuery = "SELECT COUNT(*) FROM visitor v "
            + "WHERE v.check_in_time IS NOT NULL "
            + "AND v.check_in_time BETWEEN :start AND :end")
    Page<CheckInVisitor> findCheckInBetweenDate(@Param(value = "start") LocalDate start,
        @Param(value = "end") LocalDate end, Pageable pageable);
}