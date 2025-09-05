package com.wbsrisktaskerx.wbsrisktaskerx.pojo.dto;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SparePartDetailDTO {
    Integer id;
    String material;
    String materialDescription;
    String frictionCoefficient;
    String frictionCoefficientDescription;
    String lifespan;
    String lifespanDescription;
    String warranty;
    String warrantyDescription;
    String weight;
    String weightDescription;
    String packagingSize;
    String packagingSizeDescription;
    String unitOfMeasurement;
    String unitOfMeasurementDescription;
    String thickness;
    String thicknessDescription;
}
