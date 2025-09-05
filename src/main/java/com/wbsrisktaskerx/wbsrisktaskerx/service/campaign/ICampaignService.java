package com.wbsrisktaskerx.wbsrisktaskerx.service.campaign;

import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.ActiveRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.dto.CampaignDTO;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterCampaignRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import com.wbsrisktaskerx.wbsrisktaskerx.pojo.PagingRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CampaignResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CampaignDetailResponse;

public interface ICampaignService {
    Page<CampaignResponse> getAllCampaigns(PagingRequest<SearchFilterCampaignRequest> request);
    Boolean switchCampaignStatus(ActiveRequest request);
    Boolean createCampaign(CampaignDTO dto);
    Boolean deleteCampaign(Integer id);
    CampaignDetailResponse getDetailCampaignById(Integer id);
    Boolean updateCampaignDetail(CampaignDTO dto);
}
