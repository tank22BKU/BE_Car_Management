package com.wbsrisktaskerx.wbsrisktaskerx.pojo.dto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SparePartBranchDTO {
    Integer id;
    String branchName;
    String locationCode;
    Long currentStock;
    Long retailPrice;
    OffsetDateTime lastRestockDate;
}
