package com.ftseoul.visitor.dto.companyvisitor;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
public class CompanyVisitorSearchDto implements Serializable{

    @NotNull(message = "검색 시작 일자를 입력해주세요.")
    private LocalDate start;
    @NotNull(message = "검색 종료 일자를 입력해주세요")
    private LocalDate end;
    @NotNull(message = "페이징을 입력해주세요")
    private Pagination pagination;

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
}
