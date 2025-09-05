package com.wbsrisktaskerx.wbsrisktaskerx.controller;

import com.wbsrisktaskerx.wbsrisktaskerx.common.constants.EndpointConstants;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.ApiResult;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.PagingRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.dto.CampaignDTO;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.ActiveRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.CampaignSmsNotificationsRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterCampaignRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CampaignDetailResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CampaignResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.repository.CampaignRepository;
import com.wbsrisktaskerx.wbsrisktaskerx.service.campaign.ICampaignService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EndpointConstants.CAMPAIGN)
public class CampaignController {
    private final ICampaignService campaignService;
    private final CampaignRepository campaignRepository;

    public CampaignController(ICampaignService campaignService, CampaignRepository campaignRepository) {
        this.campaignService = campaignService;
        this.campaignRepository = campaignRepository;
    }

    @PostMapping(EndpointConstants.SEARCH_FILTER)
    public ResponseEntity<ApiResult<Page<CampaignResponse>>> getAllCampaigns(
            @RequestBody @Valid PagingRequest<SearchFilterCampaignRequest> request) {
        Page<CampaignResponse> response = campaignService.getAllCampaigns(request);
        return ResponseEntity.ok(ApiResult.success(response));
    }

    @Operation(summary = "Create Campaign", description = "This API will create new Campaign with Input Data\n"
            + "AdminId and CreatedBy field are identified by the Admin logged in")
    @PostMapping
    public ResponseEntity<ApiResult<Boolean>> createCampaign(@RequestBody @Valid CampaignDTO dto) {
        Boolean response = campaignService.createCampaign(dto);
        return ResponseEntity.ok(ApiResult.success(response));
    }

    @Operation(summary = "Switch Marketing Campaign status", description = "This API will get Marketing Campaign Request Body then check if existing then switch between active and inactive.")
    @PutMapping(EndpointConstants.STATUS)
    public ResponseEntity<ApiResult<Boolean>> switchCampaignStatus(@RequestBody @Valid ActiveRequest request) {
        return ResponseEntity.ok(ApiResult.success(campaignService.switchCampaignStatus(request)));
    }

    @Operation(summary = "Delete Marketing Campaign", description = "This API will get Marketing Campaign Id then check if existing then delete that entity")
    @DeleteMapping(EndpointConstants.ID)
    public ResponseEntity<ApiResult<Boolean>> deteleCampaign(@PathVariable @Valid Integer id) {
        return ResponseEntity.ok(ApiResult.success(campaignService.deleteCampaign(id)));
    }

    @Operation(summary = "Get Campaign detail", description = "Get a Campaign detail by id")
    @GetMapping(EndpointConstants.ID)
    public ResponseEntity<ApiResult<CampaignDetailResponse>> getDetailCampaign(@PathVariable("id") Integer id) {
        CampaignDetailResponse detail = campaignService.getDetailCampaignById(id);
        return ResponseEntity.ok(ApiResult.success(detail));
    }

    @Operation(
            summary = "Update Campaign Details",
            description = """
        Updates an existing campaign by its ID.

        ### Fields That Can Be Updated:
        - `campaignName`, `campaignType`, `campaignGoal`, `noteDetails`
        - `isActive`, `budget`, `targetAudience`
        - `startDate`, `endDate`
        - `branchId` (must reference an existing Branch)

        The campaign is identified by the `id` field in the request.
        """
    )
    @PutMapping
    public ResponseEntity<ApiResult<Boolean>> updateCampaignDetail(@RequestBody CampaignDTO dto) {
        return ResponseEntity.ok(ApiResult.success(campaignService.updateCampaignDetail(dto)));
    }

}
