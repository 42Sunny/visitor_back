package com.ftseoul.visitor.data;

import com.ftseoul.visitor.encrypt.Seed;
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

    @Builder
    public Visitor(Long reserve_id, String name, String phone, String organization) {
        this.reserveId = reserve_id;
        this.name = name;
        this.phone = phone;
        this.organization = organization;
    }

    public Visitor encrypt(Seed seed) {
        this.name = seed.encrypt(this.name);
        this.phone = seed.encrypt(this.phone);
        return this;
    }

    public Visitor decrypt(Seed seed) {
        this.name = seed.decrypt(this.name);
        this.phone = seed.decrypt(this.phone);
        return this;
    }

    @Override
    public String toString() {
        return "Visitor{" +
                "id=" + id +
                ", reserveId=" + reserveId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", organization='" + organization + '\'' +
                '}';
    }
}
