package com.wbsrisktaskerx.wbsrisktaskerx.service.sparepart;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.QSparepart;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.Sparepart;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.SparepartBranch;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.SparepartDetail;
import com.wbsrisktaskerx.wbsrisktaskerx.exception.AppException;
import com.wbsrisktaskerx.wbsrisktaskerx.exception.ErrorCode;
import com.wbsrisktaskerx.wbsrisktaskerx.mapper.SparepartDetailMapper;
import com.wbsrisktaskerx.wbsrisktaskerx.mapper.SparepartMapper;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.PagingRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.dto.SparePartBranchDTO;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.dto.SparePartDetailDTO;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.AddSparePartRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterSparepartRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SparePartUpdateRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.SparepartBranchRespone;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.SparepartDetailResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.SparepartResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.repository.SparepartBranchRepository;
import com.wbsrisktaskerx.wbsrisktaskerx.repository.SparepartDetailRepository;
import com.wbsrisktaskerx.wbsrisktaskerx.repository.SparepartJpaQueryRepository;
import com.wbsrisktaskerx.wbsrisktaskerx.repository.SparepartRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.wbsrisktaskerx.wbsrisktaskerx.utils.SparePartUtils.getSparePartById;


@Service
public class SparepartServiceImpl implements ISparepartService {

    private final SparepartRepository sparepartRepository;
    private final SparepartBranchRepository sparepartBranchRepository;
    private final SparepartDetailRepository sparepartDetailRepository;
    private final SparepartJpaQueryRepository sparepartJpaQueryRepository;
    private final QSparepart sparepart = QSparepart.sparepart;
    private final JPAQueryFactory jpaQueryFactory;
    private final SparepartMapper sparePartMapper;
    private final SparepartDetailMapper sparepartDetailMapper;

    public SparepartServiceImpl(
        SparepartRepository sparepartRepository,
        SparepartBranchRepository sparepartBranchRepository,
        SparepartDetailRepository sparepartDetailRepository,
        SparepartJpaQueryRepository sparepartJpaQueryRepository,
        JPAQueryFactory jpaQueryFactory,
        SparepartMapper sparePartMapper,
        SparepartDetailMapper sparepartDetailMapper
    ) {
        this.sparepartRepository = sparepartRepository;
        this.sparepartBranchRepository = sparepartBranchRepository;
        this.sparepartDetailRepository = sparepartDetailRepository;
        this.sparepartJpaQueryRepository = sparepartJpaQueryRepository;
        this.jpaQueryFactory = jpaQueryFactory;
        this.sparePartMapper = sparePartMapper;
        this.sparepartDetailMapper = sparepartDetailMapper;
    }

    @Override
    public Page<SparepartResponse> searchFilterSparepart(PagingRequest<SearchFilterSparepartRequest> request){
        return sparepartJpaQueryRepository.getSearchFilterResult(request, sparepart);
    }

    @Override
    public SparepartDetailResponse getDetailSparepartById(Integer id) {
        Sparepart sparepart = getSparepartById(id);
        if(sparepart.getIsDeleted()){
            throw new AppException(ErrorCode.SPARE_PART_NOT_FOUND);
        }
        //resolve consecutive null pointer
        Integer detailId = sparepart.getSparepartDetail() != null ? sparepart.getSparepartDetail().getId() : -1;
        SparepartDetail detail = sparepartDetailRepository.findById(detailId)
                .orElse(new SparepartDetail());

        List<SparepartBranchRespone> branches = Optional.ofNullable(detail.getSparepartBranches())
                .map(branchesSet -> branchesSet.stream()
                        .map(sparePartMapper::toSparepartBranchResponse)
                        .toList())
                .orElse(Collections.emptyList());
        return sparepartDetailMapper.toResponse(sparepart, detail, branches);
    }

    @Override
    public List<SparepartBranchRespone> getAllBranches() {
        return sparepartBranchRepository.findAll()
                .stream()
                .map(sparePartMapper::toSparepartBranchResponse)
                .toList();
    }

    @Override
    public List<SparepartResponse> getAllSparepart() {
        List<Sparepart> spareparts = sparepartRepository.findAll();
        return spareparts.stream()
                .map(sparePartMapper::toSparepartResponse)
                .toList();
    }

    private Sparepart getSparepartById(Integer id){
        return sparepartRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SPARE_PART_NOT_FOUND));
    }

    @Override
    @Transactional
    public void updateSparePart(SparePartUpdateRequest request) {
        // 1. Fetch current entities
        Sparepart sparepart = getSparePartById(sparepartRepository, request.getSparePart().getId() , ErrorCode.SPARE_PART_NOT_FOUND);

        SparePartDetailDTO detailDTO = request.getSparePartDetail();
        SparepartDetail detail;
        if (ObjectUtils.isNotEmpty(detailDTO.getId())) {
            detail = getSparePartById(
                    sparepartDetailRepository,
                    detailDTO.getId(),
                    ErrorCode.SPARE_PART_DETAIL_NOT_FOUND
            );
            sparePartMapper.updateSparepartDetailFromDTO(detailDTO, detail);
        } else {
            detail = sparePartMapper.toSparepartDetail(detailDTO);
        }

        // 3. Process each branch
        List<SparepartBranch> updatedBranches = new ArrayList<>();
        List<SparePartBranchDTO> incomingBranches = request.getSparePartBranches();
        for (SparePartBranchDTO dto : incomingBranches) {
            SparepartBranch branch;
            if (ObjectUtils.isNotEmpty(dto.getId())) {
                // Update existing
                branch = getSparePartById(sparepartBranchRepository, dto.getId(), ErrorCode.SPARE_PART_BRANCH_NOT_FOUND);
                sparePartMapper.updateSparepartBranchFromDTO(dto, branch);
            } else {
                // If ID is null → check if locationCode already exists in DB
                List<SparepartBranch> existingBranches = sparepartBranchRepository.findByLocationCode(dto.getLocationCode());
                if (!existingBranches.isEmpty()) {
                    throw new AppException(ErrorCode.DUPLICATE_LOCATION_CODE);
                }
                branch = sparePartMapper.toSparepartBranch(dto);
            }
            updatedBranches.add(branch);
        }

        // 3. Set relationships
        detail.setSparepartBranches(updatedBranches);
        sparepart.setSparepartDetail(detail);

        // 4. Update sparePart
        sparePartMapper.updateSparepartFromDTO(request.getSparePart(), sparepart);

        // 5. Save all
        sparepartDetailRepository.save(detail);
        sparepartBranchRepository.saveAll(updatedBranches);
        sparepartRepository.save(sparepart);
    }

    @Override
    public Boolean deleteSparepartWithId(Integer id){
            Sparepart sparepart = getSparepartById(id);
            if(sparepart.getIsDeleted()){
                throw new AppException(ErrorCode.SPARE_PART_NOT_FOUND);
            }
            sparepart.setIsDeleted(true);
            sparepartRepository.save(sparepart);
            return true;
    }

    @Override
    public List<String> getAllSparepartManufacturer(){
        if(sparepartRepository.findAll().isEmpty()){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return sparepartJpaQueryRepository.getAlLManufacturer();
    }

    @Override
    public Boolean switchSparePartStatus(Integer id){
        Sparepart sparepart = getSparepartById(id);
        if(!sparepart.getIsDeleted()){
            sparepart.setIsActive(!sparepart.getIsActive());
            sparepartRepository.save(sparepart);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Sparepart addSparePart(AddSparePartRequest request){
        return sparepartRepository.save(sparePartMapper.toSparePart(request));
    }
}
