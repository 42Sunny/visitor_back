package com.ftseoul.visitor.data;

import java.time.LocalDateTime;
import javax.persistence.*;

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

    @Column(length = 4, nullable = false)
    private String place;

    @Column(nullable = false)
    private Long targetStaff;

    @Column(length = 50, nullable = false)
    private String purpose;

    @Column(nullable = false)
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

    @Override
    public String toString() {
        return "Reserve{" +
                "id=" + id +
                ", place='" + place + '\'' +
                ", targetStaff=" + targetStaff +
                ", purpose='" + purpose + '\'' +
                ", date=" + date +
                '}';
    }
}
