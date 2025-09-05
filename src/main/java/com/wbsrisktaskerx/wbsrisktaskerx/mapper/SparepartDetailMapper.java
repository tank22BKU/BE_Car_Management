package com.wbsrisktaskerx.wbsrisktaskerx.mapper;

import com.wbsrisktaskerx.wbsrisktaskerx.entity.SparepartDetail;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.SparepartDetailResponse;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.Sparepart;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.SparepartDetail;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.SparepartBranchRespone;

@Mapper(componentModel = "spring")
public interface SparepartDetailMapper {
    @Mapping(target = "id", source = "sparepart.id")
    @Mapping(target = "fullName", source = "sparepart.fullName")
    @Mapping(target = "manufacturer", source = "sparepart.manufacturer")
    @Mapping(target = "compatibleVehicleType", source = "sparepart.compatibleVehicleType")
    @Mapping(target = "quantity", source = "sparepart.quantity")
    @Mapping(target = "price", source = "sparepart.price")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "sparepartDetail", source = "detail")
    @Mapping(target = "branches", source = "branches")
    SparepartDetailResponse toResponse(
        Sparepart sparepart,
        SparepartDetail detail, 
        List<SparepartBranchRespone> branches
    );

    @Mapping(target = "branches", source = "sparepartBranches")
    SparepartDetailResponse toResponse(SparepartDetail entity);
}