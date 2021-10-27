package com.ftseoul.visitor.dto.payload;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DateRange {
    private LocalDate start;
    private LocalDate end;
    private Pagination pagination;

    public LocalDate includeEnd() {
        return this.end.plusDays(1);
    }

    public Pageable getPage() {
        return PageRequest.of(this.pagination.getPage(), this.pagination.getSize());
    }

    public static class Pagination {
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
}
