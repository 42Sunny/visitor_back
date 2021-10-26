package com.ftseoul.visitor.data;

import com.ftseoul.visitor.dto.visitor.CheckInVisitorDto;
import java.time.LocalDateTime;
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
    List<Visitor> findAllByReserveId(Long reserve_id);
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
            + "SUM(v.id) AS sum "
            + "FROM visitor v "
            + "WHERE v.check_in_time IS NOT NULL "
            + "AND v.check_in_time BETWEEN :start AND :end "
            + "GROUP BY checkInDate "
            + "ORDER BY checkInDate, checkIn DESC")
    List<CheckInVisitorDto> findCheckInBetweenDate(@Param(value = "start") LocalDateTime start, @Param(value = "end") LocalDateTime end);
}