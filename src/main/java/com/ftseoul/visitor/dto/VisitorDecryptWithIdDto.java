package com.ftseoul.visitor.dto;

import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.data.visitor.VisitorStatus;
import com.ftseoul.visitor.encrypt.Seed;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VisitorDecryptWithIdDto implements Serializable {
    private Long visitorId;

    private Long reserveId;

    private String name;

    private String phone;

    private String organization;

    private VisitorStatus status;

    public VisitorDecryptWithIdDto decryptDto(Seed seed) {
        this.name = seed.decrypt(this.name);
        this.phone = seed.decrypt(this.phone);
        return this;
    }
}
