package com.ftseoul.visitor.data;

import com.ftseoul.visitor.data.visitor.VisitorStatus;
import com.ftseoul.visitor.encrypt.Seed;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reserveId;

    @Column(length = 200, nullable = false)
    private String name;

    @Column(length = 200, nullable = false)
    private String phone;

    @Column(length = 50)
    private String organization;

    @Column(columnDefinition = "varchar(10) default '대기'")
    @Enumerated(value = EnumType.STRING)
    private VisitorStatus status = VisitorStatus.대기;

    @Column
    private LocalDateTime checkInTime;

    @Column
    private LocalDateTime checkOutTime;

    @Builder
    public Visitor(Long reserve_id, String name, String phone, String organization) {
        this.reserveId = reserve_id;
        this.name = name;
        this.phone = phone;
        this.organization = organization;
    }

    public void updateStatus(VisitorStatus status) {
        this.status = status;
    }

    public void checkIn() {
        this.checkInTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }

    public void checkOut() {
        this.checkOutTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }

    @Override
    public String toString() {
        return "Visitor{" +
                "id=" + id +
                ", reserveId=" + reserveId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", organization='" + organization + '\'' +
                ", status'" + status + '\'' +
                '}';
    }
}
