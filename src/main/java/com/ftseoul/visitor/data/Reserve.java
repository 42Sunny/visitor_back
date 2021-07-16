package com.ftseoul.visitor.data;

import java.time.LocalDateTime;
import javax.persistence.*;

import com.ftseoul.visitor.dto.ReserveUpdateDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Reserve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4)
    private String place;

    private Long targetStaff;

    @Column(length = 50)
    private String purpose;

    private LocalDateTime date;

    @Builder
    public Reserve(String place, Long targetStaff, String purpose, LocalDateTime date) {
        this.place = place;
        this.targetStaff = targetStaff;
        this.purpose = purpose;
        this.date = date;
    }

    public Reserve update(String place, Long targetStaff, String purpose, LocalDateTime date){
        this.place = place;
        this.targetStaff = targetStaff;
        this.purpose = purpose;
        this.date = date;
        return this;
    }

    public Reserve update(ReserveUpdateDto reserveUpdateDto) {
        this.place = reserveUpdateDto.getPlace();
        this.targetStaff = reserveUpdateDto.getTargetStaff();
        this.purpose = reserveUpdateDto.getPurpose();
        this.date = reserveUpdateDto.getDate();
        return this;
    }
}
