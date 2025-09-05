package com.wbsrisktaskerx.wbsrisktaskerx.mapper;

import com.wbsrisktaskerx.wbsrisktaskerx.entity.Admin;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.Branch;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.Campaign;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.dto.CampaignDTO;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CampaignResponse;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.FIELD, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CampaignMapper {
    CampaignResponse toResponse(Campaign campaign);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", source = "dto.isActive", defaultValue = "true")
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "admin", source = "admin")
    @Mapping(target = "branch", source = "branch")
    @Mapping(target = "createdBy", source = "admin.fullName")
    Campaign toEntity(CampaignDTO dto, Admin admin, Branch branch);

}
