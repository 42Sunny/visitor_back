package com.ftseoul.visitor.data;

import com.ftseoul.visitor.data.visitor.SearchCriteria;
import com.ftseoul.visitor.encrypt.Seed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchCriteriaDto {
    private SearchCriteria criteria;
    private String value;

    public void encrypt(Seed seed) {
        this.value = seed.encrypt(getValue());
    }
}
