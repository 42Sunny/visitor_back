package com.ftseoul.visitor.data;

import com.ftseoul.visitor.dto.payload.VisitorSearchCriteria;
import com.ftseoul.visitor.dto.visitor.projection.CheckInVisitorDecrypt;
import com.ftseoul.visitor.dto.visitor.projection.QCheckInVisitorDecrypt;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

@Repository
public class QueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QReserve reserve = new QReserve("reserve");
    private final QVisitor visitor = new QVisitor("visitor");
    private final QStaff staff = new QStaff("staff");

    public QueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<CheckInVisitorDecrypt> findVisitorByCriteria(VisitorSearchCriteria criteria) {

        DateTemplate<String> formattedDate = Expressions.dateTemplate(String.class, "DATE_FORMAT({0}, {1})", reserve.date, "%Y-%m-%d");

        QueryResults<CheckInVisitorDecrypt> results = jpaQueryFactory
            .select(new QCheckInVisitorDecrypt(formattedDate,
                visitor.checkInTime, visitor.name, visitor.phone,
                staff.name, staff.phone, staff.department,
                reserve.purpose, reserve.place))
            .from(visitor)
            .join(reserve).on(visitor.reserveId.eq(reserve.id))
            .join(staff).on(reserve.targetStaff.eq(staff.id))
            .where(searchCriteria(criteria),
                reserve.date.between(criteria.getStart().atStartOfDay(), criteria.includeEnd().atStartOfDay()))
            .orderBy(reserve.date.desc())
            .offset(criteria.getPage().getOffset())
            .limit(criteria.getPage().getPageSize())
            .fetchResults();
        return new PageImpl<>(results.getResults(), criteria.getPage(), results.getTotal());

    }

    public BooleanBuilder searchCriteria(VisitorSearchCriteria criteria) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

         criteria
            .getSearchCriteria()
             .stream()
             .filter(c -> c.getCriteria().getEntityName().equals("Visitor"))
             .forEach(c -> {
                 PathBuilder<Visitor> entityPath = new PathBuilder<>(Visitor.class, "visitor");
                 PathBuilder<String> column = entityPath.get(c.getCriteria().getKey(), String.class);
                 booleanBuilder.and(column.eq(c.getValue()));
             });

        criteria
            .getSearchCriteria()
            .stream()
            .filter(c -> c.getCriteria().getEntityName().equals("Staff"))
            .forEach(c -> {
                PathBuilder<Staff> entityPath = new PathBuilder<>(Staff.class, "staff");
                PathBuilder<String> column = entityPath.get(c.getCriteria().getKey(), String.class);
                booleanBuilder.and(column.eq(c.getValue()));
            });
        return booleanBuilder;
    }
}
