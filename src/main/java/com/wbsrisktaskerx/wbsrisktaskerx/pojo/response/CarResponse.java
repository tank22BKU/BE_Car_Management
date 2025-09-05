package com.wbsrisktaskerx.wbsrisktaskerx.pojo.response;

import java.math.BigDecimal;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarResponse {
    Integer id;
    String model;
    String variant;
    String vehicleType;
    BigDecimal price;
    String imageUrl;
    Boolean isActive;

    @QueryProjection
    public CarResponse(Integer id, String model, String variant, String vehicleType,
                       BigDecimal price, String imageUrl, Boolean isActive) {
        this.id = id;
        this.model = model;
        this.variant = variant;
        this.vehicleType = vehicleType;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
    }
}
