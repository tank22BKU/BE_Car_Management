package com.wbsrisktaskerx.wbsrisktaskerx.pojo.request;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddSparePartRequest {
    @NotNull
    String fullName;

    @NotNull
    String manufacturer;

    @NotNull
    String compatibleVehicleType;

    @NotNull
    Long quantity;

    @NotNull
    Double price;

    @NotNull
    Boolean isActive;
}