package com.project.sokdak2.api.repository;

import com.project.sokdak2.api.domain.common.QVisits;
import com.project.sokdak2.api.domain.common.Visits;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class VisitsRepositoryImpl implements VisitsRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public List<Visits> selectUselessVisits() {
        QVisits visits = QVisits.visits;
        return this.queryFactory.select(visits)
        .from(visits)
        .where(visits.createdTime.before(LocalDate.now().minusDays(1L).atStartOfDay())
                .and(visits.uri.notLike("/resume"))
                .and((visits.method.eq("GET"))).or(visits.method.eq("HEAD")))
        .fetch();
    }
    public long deleteUselessVisits() {
        QVisits visits = QVisits.visits;
        return this.queryFactory.delete(visits)
                .where(visits.createdTime.before(LocalDate.now().minusDays(1L).atStartOfDay())
                        .and(visits.uri.notLike("/resume"))
                        .and((visits.method.eq("GET"))).or(visits.method.eq("HEAD")))
                .execute();
    }
}
