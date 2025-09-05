package com.wbsrisktaskerx.wbsrisktaskerx.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wbsrisktaskerx.wbsrisktaskerx.common.constants.CommonConstants;
import com.wbsrisktaskerx.wbsrisktaskerx.common.constants.StringConstants;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.*;
import com.wbsrisktaskerx.wbsrisktaskerx.exception.AppException;
import com.wbsrisktaskerx.wbsrisktaskerx.exception.ErrorCode;
import com.wbsrisktaskerx.wbsrisktaskerx.mapper.CarStructMapper;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterCarRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CarBranchResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CarDetailResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CarResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CarSpecificationResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.utils.SearchFilterUtils;
import io.micrometer.common.util.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CarJpaQueryRepository extends SearchFilterUtils<SearchFilterCarRequest, QCar, CarResponse> {
    private final JPAQueryFactory jpaQueryFactory;
    private final CarRepository carRepository;
    private final QCar qCar = QCar.car;
    private final CarStructMapper carStructMapper;
    private final QCarDetail qCarDetail = QCarDetail.carDetail;
    private final QCarBranch qCarBranch = QCarBranch.carBranch;

    public CarJpaQueryRepository(JPAQueryFactory jpaQueryFactory, CarRepository carRepository, CarStructMapper carStructMapper) {
        super(jpaQueryFactory);
        this.jpaQueryFactory = jpaQueryFactory;
        this.carRepository = carRepository;
        this.carStructMapper = carStructMapper;
    }

    public Car getCarById(int id) {
        return carRepository.findCarByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.CAR_NOT_FOUND));
    }

    public CarDetailResponse getCarDetail(int id) {
        Car car = getCarById(id);

        CarDetail specification = jpaQueryFactory.select(qCarDetail)
                .from(qCar)
                .join(qCar.carDetail, qCarDetail)
                .where(qCar.id.eq(id))
                .fetchOne();

        if (specification == null) {
            return carStructMapper.carDetailBranchMapper(car, new CarSpecificationResponse(), null);
        }

        CarSpecificationResponse carSpecificationResponse = carStructMapper.toCarSpecificationResponse(specification);

        List<CarBranchResponse> branches = jpaQueryFactory.select(qCarBranch)
                .from(qCarDetail)
                .join(qCarDetail.branches, qCarBranch)
                .where(qCarDetail.id.eq(specification.getId()))
                .fetch()
                .stream()
                .map(carStructMapper::toCarBranchResponse)
                .toList();

        return carStructMapper.carDetailBranchMapper(car, carSpecificationResponse, branches);
    }

    public List<String> getAllVehicleType() {
        return jpaQueryFactory.selectDistinct(qCar.vehicleType)
                .from(qCar)
                .where(qCar.isDeleted.eq(false))
                .fetch();
    }



    public List<CarResponse> searchedAndFilteredCarNoPaging(SearchFilterCarRequest filter) {
        BooleanBuilder builder = buildWhereExpression(filter, qCar);
        OrderSpecifier<?> order = new OrderSpecifier<>(Order.ASC, qCar.id);
        return getContents(qCar, order, builder);
    }

    @Override
    public NumberExpression<Long> getCountExpression(QCar object) {
        return qCar.id.count();
    }

    @Override
    public BooleanBuilder buildWhereExpression(SearchFilterCarRequest filter, QCar object) {
        String searchKey = filter.getSearchKey();
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.isNotBlank(searchKey)) {
            BooleanBuilder searchBuilder = new BooleanBuilder();
            searchBuilder.or(qCar.model.like(
                    String.format(StringConstants.PERCENT, CommonConstants.WILDCARD, searchKey,
                            CommonConstants.WILDCARD)));
            if (NumberUtils.isCreatable(searchKey)) {
                Integer idValue = Integer.valueOf(searchKey);
                searchBuilder.or(qCar.id.eq(idValue));
            }
            builder.and(searchBuilder);
        }

        if (!filter.getVehicleType().isEmpty()) {
            builder.and(qCar.vehicleType.in(filter.getVehicleType()));
        }

        if (!filter.getIsActive().isEmpty()) {
            builder.and(qCar.isActive.in(filter.getIsActive()));
        }

        builder.and(qCar.isDeleted.isFalse());

        return builder;
    }

    @Override
    public OrderSpecifier<?> getSortOrderSpecifier(String sortKey, Pageable pageable, QCar object) {
        Order direction = Optional.of(pageable.getSort())
                .map(sort -> sort.getOrderFor(sortKey))
                .map(order -> order.isDescending() ? Order.DESC : Order.ASC)
                .orElse(Order.ASC);
        return switch (sortKey) {
            case "price" -> new OrderSpecifier<>(direction, qCar.price);
            default -> new OrderSpecifier<>(direction, qCar.id);
        };
    }

    @Override
    public List<CarResponse> getPageContents(Pageable pageable, QCar qCar, OrderSpecifier<?> orderSpecifiers, BooleanBuilder builder) {
        return jpaQueryFactory.selectFrom(qCar)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifiers)
                .fetch()
                .stream()
                .map(carStructMapper::toCarResponse)
                .toList();
    }

    @Override
    public List<CarResponse> getContents(QCar qCar, OrderSpecifier<?> orderSpecifiers, BooleanBuilder builder) {
        return jpaQueryFactory.selectFrom(qCar)
                .where(builder)
                .orderBy(qCar.id.asc())
                .fetch()
                .stream()
                .map(carStructMapper::toCarResponse)
                .toList();
    }
}