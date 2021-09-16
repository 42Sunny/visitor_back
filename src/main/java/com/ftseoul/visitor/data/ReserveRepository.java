package com.ftseoul.visitor.data;

import com.ftseoul.visitor.dto.DateFoundResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {

    @Query("SELECT new com.ftseoul.visitor.dto.DateFoundResponseDto(r.id , r.place, s.id, s.name, s.phone, r.purpose , r.date) "
        + "FROM Reserve r INNER JOIN FETCH Staff s ON r.targetStaff = s.id "
        + "WHERE r.date BETWEEN :from AND :to")
    List<DateFoundResponseDto> findAllReserveWithStaffByDate(LocalDateTime from, LocalDateTime to);

}
