package com.wbsrisktaskerx.wbsrisktaskerx.mapper;

import com.wbsrisktaskerx.wbsrisktaskerx.entity.*;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.*;
import org.apache.commons.lang3.ObjectUtils;

import static com.wbsrisktaskerx.wbsrisktaskerx.mapper.CustomerMapper.customerMapper;

import java.math.BigDecimal;

public class CarMapper {

    public static CarBrandResponse carBrandMapper(CarBrand carBrand) {
        return CarBrandResponse.builder()
                .id(carBrand.getId())
                .name(carBrand.getName())
                .build();
    }

    public static CarCategoryResponse carCategoryMapper(CarCategory carCategory) {
        return CarCategoryResponse.builder()
                .id(carCategory.getId())
                .name(carCategory.getName())
                .build();
    }

    public static CarResponse carMapper(Car car) {
        return CarResponse.builder()
                .id(car.getId())
                .model(car.getModel())
                .variant(car.getVariant())
                .vehicleType(car.getVehicleType())
                .price(ObjectUtils.isEmpty(car.getPrice()) ? BigDecimal.ZERO : car.getPrice())
                .imageUrl(car.getImageUrl())
                .isActive(car.getIsActive())
                .build();
    }


    public static WarrantyResponse warrantyMapper(Warranty warranty) {
        return WarrantyResponse.builder()
                .id(warranty.getId())
                .car(carMapper(warranty.getCar()))
                .customer(customerMapper(warranty.getCustomer()))
                .startedDate(warranty.getStartedDate())
                .expiredDate(warranty.getExpiredDate())
                .build();
    }
}
