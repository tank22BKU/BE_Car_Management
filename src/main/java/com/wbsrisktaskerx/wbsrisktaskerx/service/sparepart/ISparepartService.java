package com.wbsrisktaskerx.wbsrisktaskerx.service.sparepart;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.Sparepart;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.PagingRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.dto.SparePartDTO;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.AddSparePartRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterSparepartRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SparePartUpdateRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.SparepartResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.SparepartBranchRespone;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.SparepartDetailResponse;
import java.util.List;

public interface ISparepartService {

    List<SparepartResponse> getAllSparepart();
    List<SparepartBranchRespone> getAllBranches();
    SparepartDetailResponse getDetailSparepartById(Integer id);
    Page<SparepartResponse> searchFilterSparepart(PagingRequest<SearchFilterSparepartRequest> request);
    void updateSparePart(SparePartUpdateRequest request);
    Boolean deleteSparepartWithId(Integer id);
    List<String> getAllSparepartManufacturer();
    Boolean switchSparePartStatus(@Valid Integer id);
    Sparepart addSparePart(@Valid AddSparePartRequest request);
}

