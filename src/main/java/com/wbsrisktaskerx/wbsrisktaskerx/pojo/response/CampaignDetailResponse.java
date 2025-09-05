package com.wbsrisktaskerx.wbsrisktaskerx.pojo.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CampaignDetailResponse {
    Integer id;
    String campaignName;
    String campaignType;
    OffsetDateTime startDate;
    OffsetDateTime endDate;
    Double budget;
    String targetAudience;
    String createdBy;
    Boolean isActive;
    String socialMedia;
    String searchEngine;
    String traditionalChannel;
    String campaignGoal;
    String noteDetails;

    @QueryProjection
    public CampaignDetailResponse(Integer id, String campaignName, String campaignType, OffsetDateTime startDate, OffsetDateTime endDate,
                            Double budget, String targetAudience, String createdBy, Boolean isActive,
                            String socialMedia, String searchEngine, String traditionalChannel, String campaignGoal, String noteDetails) {
        this.id = id;
        this.campaignName = campaignName;
        this.campaignType = campaignType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.targetAudience = targetAudience;
        this.createdBy = createdBy;
        this.isActive = isActive;
        this.socialMedia = socialMedia;
        this.searchEngine = searchEngine;
        this.traditionalChannel = traditionalChannel;
        this.campaignGoal = campaignGoal;
        this.noteDetails = noteDetails;
    }
}