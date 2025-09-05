package com.wbsrisktaskerx.wbsrisktaskerx.pojo.dto;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SparePartDTO {
    Integer id;
    String fullName;
    String manufacturer;
    String compatibleVehicleType;
    Boolean isActive;
    Double price;
    Long quantity;
}
