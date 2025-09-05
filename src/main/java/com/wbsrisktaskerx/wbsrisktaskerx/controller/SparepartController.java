package com.wbsrisktaskerx.wbsrisktaskerx.controller;

import com.wbsrisktaskerx.wbsrisktaskerx.common.constants.EndpointConstants;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.Sparepart;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.ApiResult;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.PagingRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.AddSparePartRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterSparepartRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SparePartUpdateRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.SparepartResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.repository.SparepartRepository;
import com.wbsrisktaskerx.wbsrisktaskerx.service.sparepart.ISparepartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EndpointConstants.SPARE_PART)
public class SparepartController {

    public final ISparepartService sparepartService;
    public final SparepartRepository sparepartRepository;

    public SparepartController(ISparepartService sparepartService, SparepartRepository sparepartRepository) {
        this.sparepartService = sparepartService;
        this.sparepartRepository = sparepartRepository;
    }

    @Operation(
            summary = "Search and filter spare parts",
            description = """
        This API searches and filters spare parts from the database.

        ### Filtering Options:
        - `searchKey`: Can match full name or ID of the spare part.
        - `manufacturers`: Filter by manufacturer name(s).
        - `isActive`: Filter by active status (true/false).

        ### Pagination:
        - `page`: Page index (1-based).
        - `size`: Number of items per page.

        ### Sorting:
        - `sortKey`: Field used for sorting. Supported values:
            - `price`
            - `manufacturer`
            - `quantity`
            - *(any other value will fallback to `id`)*
        - `sortBy`: Direction of sort, either `ASC` or `DESC`.
        """
    )
    @PostMapping(EndpointConstants.SEARCH_FILTER)
    public ResponseEntity<ApiResult<Page<SparepartResponse>>> getListSpareParts(
            @RequestBody @Valid PagingRequest<SearchFilterSparepartRequest> request) {
        return ResponseEntity.ok(ApiResult.success(sparepartService.searchFilterSparepart(request)));
    }

    @Operation(summary = "Get sparepart detail", description = "Get detail of a sparepart by id")
    @GetMapping(EndpointConstants.ID)
    public ResponseEntity<ApiResult<Object>> getDetailSparepart(@PathVariable("id") Integer id) {
        var detail = sparepartService.getDetailSparepartById(id);
        return ResponseEntity.ok(ApiResult.success(detail));
    }

    @Operation(summary = "Update SparePart", description = "This API will update all data SparePart, SparePartDetail, SparePartBranch in DB\n" +
            "After Update. It will notice updated successfully\n"
    )
    @PutMapping()
    public ResponseEntity<ApiResult<Boolean>> updateSparePart(@RequestBody @Valid SparePartUpdateRequest request) {
        sparepartService.updateSparePart(request);
        return ResponseEntity.ok(ApiResult.success(true));
    }

    @Operation(summary = "Delete Sparepart", description = "This API will emulator `delete` feature in DATABASE\n" +
            "The API will get ID from input and set `isDeleted` is true, not actually delete that entity\n" +
            "This helps us ensure tasks related to query orders or history after correctively."
    )
    @DeleteMapping(EndpointConstants.ID)
    public ResponseEntity<ApiResult<Boolean>> deleteSparepartWithId(@PathVariable("id") Integer id){
        return ResponseEntity.ok(ApiResult.success(sparepartService.deleteSparepartWithId(id)));
    }

    @Operation(summary = "Get all manufacturers", description = "This API will get all Manufacturers branch in DB and return\n" +
            "The returned data is a set of Manufacturer's names, which include unique name\n"
    )
    @GetMapping(EndpointConstants.MANUFACTURER)
    public ResponseEntity<ApiResult<List<String>>> getAllSparepartManufacturer(){
        return ResponseEntity.ok(ApiResult.success(sparepartService.getAllSparepartManufacturer()));
    }

    @Operation(summary = "Switch Spare part status", description = "This API will get Spare part Id then check if existing then switch between active and inactive.")
    @PutMapping(EndpointConstants.STATUS)
    public ResponseEntity<ApiResult<Boolean>> switchSparePartStatus(@RequestParam @Valid Integer id){
        return ResponseEntity.ok(ApiResult.success(sparepartService.switchSparePartStatus(id)));
    }

    @Operation(summary = "Add new Spare part", description = "This API will create a new spare part with its input information")
    @PostMapping()
    public ResponseEntity<ApiResult<Sparepart>> createSparepart(@RequestBody @Valid AddSparePartRequest request){
        return ResponseEntity.ok(ApiResult.success(sparepartService.addSparePart(request)));
    }
}
