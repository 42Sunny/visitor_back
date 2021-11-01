package com.ftseoul.visitor.data;

import com.ftseoul.visitor.dto.reserve.DateFoundResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {

    @Query("SELECT new com.ftseoul.visitor.dto.reserve.DateFoundResponseDto(r.id , r.place, s.id, s.name, s.phone, s.department, "
        + "r.purpose , r.date) "
        + "FROM Reserve r INNER JOIN FETCH Staff s ON r.targetStaff = s.id "
        + "WHERE r.date BETWEEN :from AND :to")
    List<DateFoundResponseDto> findAllReserveWithStaffByDate(@Param(value = "from") LocalDateTime from,
        @Param(value = "to") LocalDateTime to);

    List<Reserve> findAllByTargetStaff(Long targetStaff);
}
