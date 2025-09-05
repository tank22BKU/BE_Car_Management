package com.wbsrisktaskerx.wbsrisktaskerx.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wbsrisktaskerx.wbsrisktaskerx.common.constants.CommonConstants;
import com.wbsrisktaskerx.wbsrisktaskerx.common.constants.StringConstants;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.QSparepart;
import com.wbsrisktaskerx.wbsrisktaskerx.mapper.SparepartMapper;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterSparepartRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.SparepartResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.utils.SearchFilterUtils;
import io.micrometer.common.util.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SparepartJpaQueryRepository extends SearchFilterUtils<SearchFilterSparepartRequest, QSparepart, SparepartResponse> {
    private final JPAQueryFactory jpaQueryFactory;
    private final QSparepart sparepart = QSparepart.sparepart;
    private final SparepartMapper sparepartMapper;

    public SparepartJpaQueryRepository(JPAQueryFactory jpaQueryFactory, SparepartMapper sparepartMapper) {
        super(jpaQueryFactory);
        this.jpaQueryFactory = jpaQueryFactory;
        this.sparepartMapper = sparepartMapper;
    }

    public List<String> getAlLManufacturer() {
        return jpaQueryFactory.selectDistinct(sparepart.manufacturer)
                .from(sparepart)
                .where(sparepart.isDeleted.eq(false))
                .fetch();
    }

    public List<SparepartResponse> searchedAndFilteredSparepartNoPaging(SearchFilterSparepartRequest filter) {
        BooleanBuilder builder = buildWhereExpression(filter, sparepart);
        OrderSpecifier<?> order = new OrderSpecifier<>(Order.ASC, sparepart.id);
        return getContents(sparepart, order, builder);
    }

    @Override
    public NumberExpression<Long> getCountExpression(QSparepart sparePart) {
        return sparePart.id.count();
    }

    @Override
    public BooleanBuilder buildWhereExpression(SearchFilterSparepartRequest filter, QSparepart sparePart) {
        String searchKey = filter.getSearchKey();
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.isNotBlank(searchKey)) {
            BooleanBuilder searchBuilder = new BooleanBuilder();
            searchBuilder.or(sparePart.fullName.like(
                    String.format(StringConstants.PERCENT, CommonConstants.WILDCARD, searchKey, CommonConstants.WILDCARD)
            ));
            if (NumberUtils.isCreatable(searchKey)) {
                Integer idValue = Integer.valueOf(searchKey);
                searchBuilder.or(sparePart.id.eq(idValue));
            }
            builder.and(searchBuilder);
        }

        if (!filter.getManufacturers().isEmpty()) {
            builder.and(sparePart.manufacturer.in(filter.getManufacturers()));
        }

        if (!filter.getIsActive().isEmpty()) {
            builder.and(sparePart.isActive.in(filter.getIsActive()));
        }

        builder.and(sparePart.isDeleted.isFalse());

        return builder;
    }

    @Override
    public OrderSpecifier<?> getSortOrderSpecifier(String sortKey, Pageable pageable, QSparepart sparePart) {
        Order direction = Optional.of(pageable.getSort())
                .map(sort -> sort.getOrderFor(sortKey))
                .map(order -> order.isDescending() ? Order.DESC : Order.ASC)
                .orElse(Order.ASC);

        return switch (sortKey){
            case "price" -> new OrderSpecifier<>(direction, sparePart.price);
            case "manufacturer" -> new OrderSpecifier<>(direction, sparePart.manufacturer);
            case "quantity" -> new OrderSpecifier<>(direction, sparePart.quantity);
            default -> new OrderSpecifier<>(direction, sparePart.id);
        };
    }

    @Override
    public List<SparepartResponse> getPageContents(Pageable pageable, QSparepart sparePart, OrderSpecifier<?> orderSpecifiers, BooleanBuilder builder) {
        return jpaQueryFactory.selectFrom(sparePart)
                .where(builder)
                .orderBy(orderSpecifiers)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(sparepartMapper::toSparepartResponse)
                .toList();
    }

    @Override
    public List<SparepartResponse> getContents(QSparepart sparePart, OrderSpecifier<?> orderSpecifiers, BooleanBuilder builder) {
        return jpaQueryFactory.selectFrom(sparepart)
                .where(builder)
                .orderBy(sparepart.id.asc())
                .fetch()
                .stream()
                .map(sparepartMapper::toSparepartResponse)
                .toList();
    }
}
