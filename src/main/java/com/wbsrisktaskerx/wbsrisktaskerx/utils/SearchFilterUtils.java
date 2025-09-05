package com.wbsrisktaskerx.wbsrisktaskerx.utils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.PagingRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/*
 * Filter
 * QObject
 * Response
 */
public abstract class SearchFilterUtils<T, R, S> {
    private final JPAQueryFactory jpaQueryFactory;

    protected SearchFilterUtils(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public abstract NumberExpression<Long> getCountExpression(R object);
    public abstract BooleanBuilder buildWhereExpression(T filter, R object);
    public abstract OrderSpecifier<?> getSortOrderSpecifier(String sortKey, Pageable pageable, R object);
    public abstract List<S> getPageContents(Pageable pageable, R object, OrderSpecifier<?> orderSpecifiers, BooleanBuilder builder);
    public abstract List<S> getContents(R object, OrderSpecifier<?> orderSpecifiers, BooleanBuilder builder);

    public Page<S> getSearchFilterResult(PagingRequest<T> request, R object){
        T filter = request.getFilters();
        Pageable pageable = PageService.getPageRequest(request);

        BooleanBuilder builder = buildWhereExpression(filter, object);
        OrderSpecifier<?> orderSpecifier = getSortOrderSpecifier(request.getSortKey(), pageable, object);
        List<S> content = getPageContents(pageable, object, orderSpecifier, builder);

        long total = Optional.ofNullable(
                jpaQueryFactory.select(getCountExpression(object))
                        .from((EntityPath<?>) object)
                        .where(builder)
                        .fetchOne()
        ).orElse(0L);
        return new PageImpl<>(content, pageable, total);
    }
}
