package com.wbsrisktaskerx.wbsrisktaskerx.pojo.response;

import com.wbsrisktaskerx.wbsrisktaskerx.pojo.dto.SparePartDetailDTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SparepartDetailResponse {
    Integer id;
    String fullName;
    String manufacturer;
    String compatibleVehicleType;
    Integer quantity;
    Double price;
    Boolean active;
    SparePartDetailDTO sparepartDetail;
    List<SparepartBranchRespone> branches;
}