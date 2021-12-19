package com.ftseoul.visitor.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface CompanyVisitorRepository extends JpaRepository<CompanyVisitor, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM company_visitor v WHERE v.check_in_time BETWEEN :start AND :end")
    Page<CompanyVisitor> findAllByCheckInTimeBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable);
}
