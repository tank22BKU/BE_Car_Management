package com.wbsrisktaskerx.wbsrisktaskerx.pojo.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SparepartResponse {
    Integer id;
    String fullName;
    String manufacturer;
    String compatibleVehicleType;
    Long quantity;
    Double price;
    Boolean isActive;

    @QueryProjection
    public SparepartResponse(Integer id, String fullName, String manufacturer, String compatibleVehicleType, Long quantity, Double price, Boolean isActive) {
        this.id = id;
        this.fullName = fullName;
        this.manufacturer = manufacturer;
        this.compatibleVehicleType = compatibleVehicleType;
        this.quantity = quantity;
        this.price = price;
        this.isActive = isActive;
    }
}