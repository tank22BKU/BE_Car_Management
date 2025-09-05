package com.wbsrisktaskerx.wbsrisktaskerx.mapper;

import com.wbsrisktaskerx.wbsrisktaskerx.entity.Campaign;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CampaignDetailResponse;

import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface CampaignDetailMapper {
    CampaignDetailResponse toDetailResponse(Campaign campaign);
}