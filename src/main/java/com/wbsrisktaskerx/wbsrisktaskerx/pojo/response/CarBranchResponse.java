package com.wbsrisktaskerx.wbsrisktaskerx.pojo.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarBranchResponse {
    Integer id;
    String branchName;
    Double availableForSales;

    @QueryProjection
    public CarBranchResponse(Integer id, String branchName, Double availableForSales) {
        this.id = id;
        this.branchName = branchName;
        this.availableForSales = availableForSales;
    }
}
