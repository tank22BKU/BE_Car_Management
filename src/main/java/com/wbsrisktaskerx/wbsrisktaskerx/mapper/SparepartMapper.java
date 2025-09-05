package com.wbsrisktaskerx.wbsrisktaskerx.mapper;

import com.wbsrisktaskerx.wbsrisktaskerx.pojo.dto.SparePartBranchDTO;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.dto.SparePartDTO;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.dto.SparePartDetailDTO;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.Sparepart;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.SparepartBranch;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.SparepartDetail;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.AddSparePartRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.SparepartBranchRespone;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.SparepartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(componentModel = "spring")
public interface SparepartMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    Sparepart toSparePart(AddSparePartRequest request);
    SparepartResponse toSparepartResponse(Sparepart sparepart);
    Sparepart toSparepart(SparepartResponse sparepartResponse);
    Sparepart toSparepart(SparePartDTO dto);
    void updateSparepartFromDTO(SparePartDTO dto, @MappingTarget Sparepart entity);
    void updateSparepartDetailFromDTO(SparePartDetailDTO dto, @MappingTarget SparepartDetail entity);
    void updateSparepartBranchFromDTO(SparePartBranchDTO dto, @MappingTarget SparepartBranch entity);
    SparepartBranch toSparepartBranch(SparePartBranchDTO dto);
    SparepartBranchRespone toSparepartBranchResponse(SparepartBranch sparepartBranch);
    List<SparepartBranch> toSparepartBranchList(List<SparePartBranchDTO> dtos);
    SparepartDetail toSparepartDetail(SparePartDetailDTO detailDTO);
}
