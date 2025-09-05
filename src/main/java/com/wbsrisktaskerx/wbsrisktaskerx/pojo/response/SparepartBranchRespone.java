package com.wbsrisktaskerx.wbsrisktaskerx.pojo.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SparepartBranchRespone {
    Integer id;
    String branchName;
    String locationCode;
    Long currentStock;
    Long retailPrice;
    OffsetDateTime lastRestockDate;

    @QueryProjection
    public SparepartBranchRespone(Integer id, String branchName, String locationCode, Long currentStock, Long retailPrice, OffsetDateTime lastRestockDate) {
        this.id = id;
        this.branchName = branchName;
        this.locationCode = locationCode;
        this.currentStock = currentStock;
        this.retailPrice = retailPrice;
        this.lastRestockDate = lastRestockDate;
    }
}