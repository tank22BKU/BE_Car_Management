package com.wbsrisktaskerx.wbsrisktaskerx.pojo.response;

import com.wbsrisktaskerx.wbsrisktaskerx.entity.CarBranch;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarDetailResponse {
    Integer id;
    String model;
    String variant;
    String vehicleType;
    BigDecimal price;
    Boolean isActive;
    String imageUrl;
    CarSpecificationResponse carSpecificationResponse;
    List<CarBranchResponse> carBranchResponse;
}
