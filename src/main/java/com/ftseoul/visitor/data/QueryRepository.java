package com.ftseoul.visitor.data;

import com.ftseoul.visitor.data.visitor.ReservePlace;
import com.ftseoul.visitor.dto.payload.VisitorSearchCriteria;
import com.ftseoul.visitor.dto.visitor.projection.CheckInVisitorDecrypt;
import com.ftseoul.visitor.dto.visitor.projection.QCheckInVisitorDecrypt;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
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
            .select(checkInVisitorByDateColumns())
            .from(visitor)
            .join(reserve).on(visitor.reserveId.eq(reserve.id))
            .join(staff).on(reserve.targetStaff.eq(staff.id))
            .where(placeCondition(criteria.getPlace())
                ,searchCriteria(criteria),
                reserveDateCondition(criteria.getStart().atStartOfDay(), criteria.includeEnd().atStartOfDay()))
            .orderBy(formattedDate.desc(),reserve.date.desc())
            .offset(criteria.getPage().getOffset())
            .limit(criteria.getPage().getPageSize())
            .fetchResults();
        return new PageImpl<>(results.getResults(), criteria.getPage(), results.getTotal());
    }

    public BooleanBuilder searchCriteria(VisitorSearchCriteria criteria) {

        if (criteria.getSearchCriteria() == null) {
            return null;
        }

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

    private BooleanExpression placeCondition(ReservePlace place) {
        if (place == null) {
            return null;
        }
        return reserve.place.eq(place.toString());
    }

    private BooleanExpression reserveDateCondition(LocalDateTime start, LocalDateTime end) {
        return reserve.date.between(start, end);
    }

    private QCheckInVisitorDecrypt checkInVisitorByDateColumns() {
        DateTemplate<String> formattedDate = Expressions.dateTemplate(String.class, "DATE_FORMAT({0}, {1})", reserve.date, "%Y-%m-%d");
        return new QCheckInVisitorDecrypt(formattedDate,
            visitor.checkInTime, visitor.id, visitor.name, visitor.phone,
            visitor.organization, visitor.status,
            staff.name, staff.phone, staff.department,
            reserve.purpose, reserve.place);
    }
}
