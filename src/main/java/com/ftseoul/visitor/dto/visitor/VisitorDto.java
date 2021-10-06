package com.ftseoul.visitor.dto.visitor;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import com.ftseoul.visitor.encrypt.Seed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VisitorDto implements Serializable {
    @NotBlank(message = "성함을 입력해주세요")
    private String name;
    @NotBlank(message = "전화번호를 입력해주세요")
    private String phone;
    @NotBlank(message = "소속을 입력해주세요")
    private String organization;

    @Override
    public String toString() {
        return "VisitorDto{" +
                " name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", organization='" + organization + '\'' +
                '}';
    }

    public VisitorDto encryptDto(Seed seed) {
        this.name = seed.encrypt(this.name);
        this.phone = seed.encrypt(this.phone);
        return this;
    }

}
