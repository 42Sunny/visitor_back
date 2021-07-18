package com.ftseoul.visitor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VisitorModifyDto implements Serializable {
    private Long reserve_id;

    private String name;

    private String phone;

    private String organization;

    @JsonProperty("isChanged")
    private boolean isChanged;

    @Override
    public String toString() {
        return "VisitorModifyDto{" +
            "reserve_id=" + reserve_id +
            ", name='" + name + '\'' +
            ", phone='" + phone + '\'' +
            ", organization='" + organization + '\'' +
            ", isChanged=" + isChanged +
            '}';
    }
}
