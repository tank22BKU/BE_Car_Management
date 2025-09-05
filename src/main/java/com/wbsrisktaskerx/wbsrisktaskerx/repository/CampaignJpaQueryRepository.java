package com.wbsrisktaskerx.wbsrisktaskerx.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wbsrisktaskerx.wbsrisktaskerx.common.constants.CommonConstants;
import com.wbsrisktaskerx.wbsrisktaskerx.common.constants.StringConstants;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.Campaign;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.QCampaign;
import com.wbsrisktaskerx.wbsrisktaskerx.exception.AppException;
import com.wbsrisktaskerx.wbsrisktaskerx.exception.ErrorCode;
import com.wbsrisktaskerx.wbsrisktaskerx.mapper.CampaignMapper;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterCampaignRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CampaignResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.utils.SearchFilterUtils;
import io.micrometer.common.util.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CampaignJpaQueryRepository extends SearchFilterUtils<SearchFilterCampaignRequest, QCampaign, CampaignResponse> {

    private final JPAQueryFactory queryFactory;
    private final CampaignMapper campaignMapper;
    private final QCampaign campaign = QCampaign.campaign;

    public CampaignJpaQueryRepository(JPAQueryFactory queryFactory, CampaignMapper campaignMapper) {
        super(queryFactory);
        this.queryFactory = queryFactory;
        this.campaignMapper = campaignMapper;
    }

    public List<CampaignResponse> findCampaignsByFilterNoPaging(SearchFilterCampaignRequest filter) {
        BooleanBuilder builder = buildWhereExpression(filter, campaign);
        OrderSpecifier<?> order = new OrderSpecifier<>(Order.ASC, campaign.id);
        return getContents(campaign, order, builder);
    }

    public CampaignResponse findCampaignById(int id) {
        QCampaign campaign = QCampaign.campaign;

        Campaign result = queryFactory
                .selectFrom(campaign)
                .where(campaign.id.eq(id)
                        .and(campaign.isDeleted.isFalse()))
                .fetchOne();

        if (result == null) {
            throw new AppException(ErrorCode.CAMPAIGN_NOT_FOUND);
        }

        return campaignMapper.toResponse(result);
    }

    @Override
    public NumberExpression<Long> getCountExpression(QCampaign campaign) {
        return campaign.id.count();
    }

    @Override
    public BooleanBuilder buildWhereExpression(SearchFilterCampaignRequest filter, QCampaign campaign) {
        String searchKey = filter.getSearchKey();
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.isNotBlank(searchKey)) {
            BooleanBuilder searchBuilder = new BooleanBuilder();
            searchBuilder.or(campaign.campaignName.like(
                    String.format(StringConstants.PERCENT, CommonConstants.WILDCARD, searchKey, CommonConstants.WILDCARD)
            ));
            if (NumberUtils.isCreatable(searchKey)) {
                Integer idValue = Integer.valueOf(searchKey);
                searchBuilder.or(campaign.id.eq(idValue));
            }
            builder.and(searchBuilder);
        }

        if (filter.getStartDate() != null) {
            builder.and(campaign.startDate.goe(filter.getStartDate()));
        }

        // Filter by endDate
        if (filter.getEndDate() != null) {
            builder.and(campaign.endDate.loe(filter.getEndDate()));
        }

        if (!filter.getIsActive().isEmpty()) {
            builder.and(campaign.isActive.in(filter.getIsActive()));
        }

        builder.and(campaign.isDeleted.isFalse());

        return builder;
    }

    @Override
    public OrderSpecifier<?> getSortOrderSpecifier(String sortKey, Pageable pageable, QCampaign campaign) {
        Order direction = Optional.of(pageable.getSort())
                .map(sort -> sort.getOrderFor(sortKey))
                .map(order -> order.isDescending() ? Order.DESC : Order.ASC)
                .orElse(Order.ASC);
        return switch (sortKey) {
            case "startDate" -> new OrderSpecifier<>(direction, campaign.startDate);
            case "campaignName" -> new OrderSpecifier<>(direction, campaign.campaignName);
            case "budget" -> new OrderSpecifier<>(direction, campaign.budget);
            default -> new OrderSpecifier<>(direction, campaign.id);
        };
    }

    @Override
    public List<CampaignResponse> getPageContents(Pageable pageable, QCampaign campaign, OrderSpecifier<?> orderSpecifiers, BooleanBuilder builder) {
        return queryFactory.selectFrom(campaign)
                .where(builder)
                .orderBy(orderSpecifiers)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(campaignMapper::toResponse)
                .toList();
    }

    @Override
    public List<CampaignResponse> getContents(QCampaign campaign, OrderSpecifier<?> orderSpecifiers, BooleanBuilder builder) {
        return queryFactory.selectFrom(campaign)
                .where(builder)
                .orderBy(campaign.id.asc())
                .fetch()
                .stream()
                .map(campaignMapper::toResponse)
                .toList();
    }
}