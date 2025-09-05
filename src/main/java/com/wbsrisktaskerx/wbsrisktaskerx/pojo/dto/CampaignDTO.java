package com.wbsrisktaskerx.wbsrisktaskerx.pojo.dto;

import jakarta.annotation.Nullable;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor 
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CampaignDTO {
    Integer id;
    Integer branchId;
    String campaignName;
    String campaignType;
    OffsetDateTime startDate;
    OffsetDateTime endDate;
    Double budget;
    String targetAudience;

    @Nullable
    String createdBy;

    Boolean isActive;
    String socialMedia;
    String searchEngine;
    String traditionalChannel;
    String campaignGoal;
    String noteDetails;
}
