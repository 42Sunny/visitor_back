package com.ftseoul.visitor.data;

import javax.persistence.*;

import com.ftseoul.visitor.encrypt.Seed;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String name;

    @Column(length = 200, nullable = false)
    private String phone;

    @Builder
    public Staff(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public void update(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public Staff encrypt(Seed seed) {
        this.name = seed.encrypt(this.name);
        this.phone = seed.encrypt(this.phone);
        return this;
    }

    public Staff decrypt(Seed seed) {
        this.name = seed.decrypt(this.name);
        this.phone = seed.decrypt(this.phone);
        return this;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
