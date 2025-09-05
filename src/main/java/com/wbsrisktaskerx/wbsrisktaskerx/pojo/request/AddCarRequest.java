package com.wbsrisktaskerx.wbsrisktaskerx.pojo.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddCarRequest {
    @NotNull
    String model;

    @NotNull
    String variant;

    @NotNull
    String vehicleType;

    @NotNull
    BigDecimal price;

    @NotNull
    Boolean isActive;
}
