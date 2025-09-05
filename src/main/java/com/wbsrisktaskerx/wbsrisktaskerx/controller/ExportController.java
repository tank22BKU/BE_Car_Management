package com.wbsrisktaskerx.wbsrisktaskerx.controller;

import com.wbsrisktaskerx.wbsrisktaskerx.common.constants.EndpointConstants;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.PagingRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterAdminRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterCampaignRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterCustomersRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterSparepartRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterCarRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.ExportResponse;
import com.wbsrisktaskerx.wbsrisktaskerx.service.export.IExportService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(EndpointConstants.EXPORT)
public class ExportController {

    private final IExportService exportService;

    public ExportController(IExportService exportService) {
        this.exportService = exportService;
    }

    @PostMapping(EndpointConstants.CUSTOMERS)
    public ResponseEntity<ExportResponse> download(@RequestBody PagingRequest<SearchFilterCustomersRequest> request)
            throws IOException {
        ExportResponse exportResponse = exportService.getCustomerList(request);
        return ResponseEntity.ok().body(exportResponse);
    }

    @PostMapping(EndpointConstants.ADMIN)
    public ResponseEntity<ExportResponse> downloadAdmin(@RequestBody PagingRequest<SearchFilterAdminRequest> request)
            throws IOException {
        ExportResponse exportResponse = exportService.getAdminList(request);
        return ResponseEntity.ok().body(exportResponse);
    }

    @GetMapping(EndpointConstants.CUSTOMERS + EndpointConstants.PURCHASE + EndpointConstants.ID)
    public ResponseEntity<ExportResponse> exportPurchaseHistory(@PathVariable int id) throws IOException {
        ExportResponse exportResponse = exportService.exportCustomerPurchaseHistory(id);
        return ResponseEntity.ok().body(exportResponse);
    }

    @GetMapping(EndpointConstants.CUSTOMERS + EndpointConstants.WARRANTY + EndpointConstants.ID)
    public ResponseEntity<ExportResponse> exportWarrantyHistory(@PathVariable int id) throws IOException {
        ExportResponse exportResponse = exportService.exportCustomerWarrantyHistory(id);
        return ResponseEntity.ok().body(exportResponse);
    }

    @GetMapping(EndpointConstants.SPARE_PART + EndpointConstants.ID)
    public ResponseEntity<ExportResponse> exportSparepartDetail(@PathVariable("id") int id) throws IOException {
        ExportResponse exportResponse = exportService.exportSparePartDetail(id);
        return ResponseEntity.ok().body(exportResponse);
    }

    @Operation(summary = "Export List Spare part", description = "This API will get constraints from input"
            + "Then API will search, filter to get final data, after that put these data into Excel file (XLSX)"
            + "Finally, Encoding the XLSX file into Base64 with password")
    @PostMapping(EndpointConstants.SPARE_PART)
    public ResponseEntity<ExportResponse> exportSparepartList(
            @RequestBody PagingRequest<SearchFilterSparepartRequest> request) throws IOException {
        return ResponseEntity.ok(exportService.exportSparepartList(request));
    }

    @Operation(summary = "Export List Marketing Campaign", description = "This API will get constraints from input"
            + "Then API will search, filter to get final data, after that put these data into Excel file (XLSX)"
            + "Finally, Encoding the XLSX file into Base64 with password")
    @PostMapping(EndpointConstants.CAMPAIGN)
    public ResponseEntity<ExportResponse> exportCampaignList(
            @RequestBody PagingRequest<SearchFilterCampaignRequest> request) throws IOException {
        return ResponseEntity.ok(exportService.exportCampaignList(request));
    }

    @GetMapping(EndpointConstants.CAMPAIGN + EndpointConstants.ID)
    public ResponseEntity<ExportResponse> exportCampaignDetail(@PathVariable("id") int id) throws IOException {
        ExportResponse exportResponse = exportService.exportCampaignDetail(id);
        return ResponseEntity.ok().body(exportResponse);
    }

    @Operation(summary = "Export List Car", description = "This API will get constraints from input" +
        "Then API will search, filter to get final data, after that put these data into Excel file (XLSX)" +
        "Finally, Encoding the XLSX file into Base64 with password")
    @PostMapping(EndpointConstants.CAR)
    public ResponseEntity<ExportResponse> exportCarList(@RequestBody PagingRequest<SearchFilterCarRequest> request)
            throws IOException {
        return ResponseEntity.ok().body(exportService.exportCarList(request));
    }

    @Operation(summary = "Export Car Detail", description = "This API will get Id from user then generate excel file(Base64 code) and password and return")
    @GetMapping(EndpointConstants.CAR + EndpointConstants.ID)
    public ResponseEntity<ExportResponse> exportCarDetail(@PathVariable("id") int id) throws IOException {
        ExportResponse exportResponse = exportService.exportCarDetail(id);
        return ResponseEntity.ok().body(exportResponse);
    }
}
