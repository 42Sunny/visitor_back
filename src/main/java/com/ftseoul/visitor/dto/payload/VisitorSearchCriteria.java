package com.ftseoul.visitor.dto.payload;

import com.ftseoul.visitor.data.SearchCriteriaDto;
import com.ftseoul.visitor.data.visitor.ReservePlace;
import com.ftseoul.visitor.encrypt.Seed;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class VisitorSearchCriteria implements Serializable {
    private ReservePlace place;
    private List<SearchCriteriaDto> searchCriteria;
    private LocalDate start;
    private LocalDate end;
    private Pagination pagination;


    public LocalDate includeEnd() {
        return this.end.plusDays(1);
    }

    public Pageable getPage() {
        return PageRequest.of(this.pagination.getPage(), this.pagination.getSize());
    }

    public static class Pagination implements Serializable {
        private int page;
        private int size;

        public Pagination() {
        }

        public Pagination(int page, int size) {
            this.page = page;
            this.size = size;
        }


        public int getPage() {
            return this.page;
        }

        public int getSize() {
            return this.size;
        }
    }

    public void encrypt(Seed seed) {
        if (searchCriteria == null) {
            return ;
        }

        for (SearchCriteriaDto searchCriterion : this.searchCriteria) {
            if (searchCriterion.getCriteria().isName() || searchCriterion.getCriteria().isPhone()) {
                searchCriterion.encrypt(seed);
            }
        }
    }
}
