package com.wbsrisktaskerx.wbsrisktaskerx.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import com.wbsrisktaskerx.wbsrisktaskerx.entity.Campaign;

public interface CampaignRepository extends JpaRepository<Campaign, Integer> {
    Campaign findCampaignByCampaignName(@NotNull String campaignName);
}
