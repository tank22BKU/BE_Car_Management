package com.wbsrisktaskerx.wbsrisktaskerx.service.campaign;

import com.wbsrisktaskerx.wbsrisktaskerx.entity.Branch;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.Campaign;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.QCampaign;
import com.wbsrisktaskerx.wbsrisktaskerx.exception.AppException;
import com.wbsrisktaskerx.wbsrisktaskerx.exception.ErrorCode;
import com.wbsrisktaskerx.wbsrisktaskerx.mapper.CampaignDetailMapper;
import com.wbsrisktaskerx.wbsrisktaskerx.mapper.CampaignMapper;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.PagingRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.dto.CampaignDTO;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.ActiveRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterCampaignRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CampaignDetailResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.CampaignResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.repository.BranchRepository;
import com.wbsrisktaskerx.wbsrisktaskerx.repository.CampaignJpaQueryRepository;
import com.wbsrisktaskerx.wbsrisktaskerx.repository.CampaignRepository;
import com.wbsrisktaskerx.wbsrisktaskerx.service.branch.IBranchService;
import com.wbsrisktaskerx.wbsrisktaskerx.utils.BaseAdminService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class CampaignService implements ICampaignService {
    private final CampaignDetailMapper campaignDetailMapper;
    private final CampaignJpaQueryRepository campaignJpaQueryRepository;
    private final CampaignMapper campaignMapper;
    private final CampaignRepository campaignRepository;
    private final BaseAdminService baseAdminService;
    private final BranchRepository branchRepository;
    private final QCampaign campaign =  QCampaign.campaign;

    public CampaignService(CampaignDetailMapper campaignDetailMapper,
            CampaignJpaQueryRepository campaignJpaQueryRepository, CampaignMapper campaignMapper,
            CampaignRepository campaignRepository, BaseAdminService baseAdminService,
            BranchRepository branchRepository, IBranchService branchService) {
        this.campaignDetailMapper = campaignDetailMapper;
        this.campaignJpaQueryRepository = campaignJpaQueryRepository;
        this.campaignMapper = campaignMapper;
        this.campaignRepository = campaignRepository;
        this.baseAdminService = baseAdminService;
        this.branchRepository = branchRepository;
    }

    @Override
    public Page<CampaignResponse> getAllCampaigns(PagingRequest<SearchFilterCampaignRequest> request) {
        return campaignJpaQueryRepository.getSearchFilterResult(request, campaign);
    }

    private Branch getBranchById(Integer id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BRANCH_NOT_FOUND));
    }

    @Override
    public Boolean createCampaign(CampaignDTO dto) {
        campaignRepository.save(
                campaignMapper.toEntity(dto, baseAdminService.getCurrentUser(), getBranchById(dto.getBranchId())));
        return Boolean.TRUE;
    }

    @Override
    public Boolean switchCampaignStatus(ActiveRequest request) {
        Campaign campaign = getCampaignById(request.getId());
        campaign.setIsActive(request.getIsActive());
        campaignRepository.save(campaign);
        return Boolean.TRUE;
    }

    private Campaign getCampaignById(Integer id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CAMPAIGN_NOT_FOUND));
    }

    @Override
    public Boolean deleteCampaign(Integer id) {
        Campaign campaign = getCampaignById(id);
        campaign.setIsDeleted(Boolean.TRUE);
        campaignRepository.save(campaign);
        return Boolean.TRUE;
    }

    @Override
    public CampaignDetailResponse getDetailCampaignById(Integer id) {
        Campaign campaign = getCampaignById(id);
        if (campaign.getIsDeleted()) {
            throw new AppException(ErrorCode.CAMPAIGN_NOT_FOUND);
        }
        return campaignDetailMapper.toDetailResponse(campaign);
    }

    @Override
    public Boolean updateCampaignDetail(CampaignDTO dto) {
        Campaign campaign = getCampaignById(dto.getId());
        campaign.setCampaignName(dto.getCampaignName());
        campaign.setCampaignType(dto.getCampaignType());
        campaign.setCampaignGoal(dto.getCampaignGoal());
        campaign.setNoteDetails(dto.getNoteDetails());
        campaign.setIsActive(dto.getIsActive());
        campaign.setBudget(dto.getBudget());
        campaign.setTargetAudience(dto.getTargetAudience());
        campaign.setBranch(getBranchById(dto.getBranchId()));
        campaign.setStartDate(dto.getStartDate());
        campaign.setEndDate(dto.getEndDate());
        campaignRepository.save(campaign);
        return Boolean.TRUE;
    }

}