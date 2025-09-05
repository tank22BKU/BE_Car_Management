package com.wbsrisktaskerx.wbsrisktaskerx.service.export;

import com.wbsrisktaskerx.wbsrisktaskerx.pojo.PagingRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterAdminRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterCampaignRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterCustomersRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterSparepartRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SearchFilterCarRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.ExportResponse;

import java.io.IOException;

public interface IExportService {
    ExportResponse getCustomerList(PagingRequest<SearchFilterCustomersRequest> request) throws IOException;

    ExportResponse getAdminList(PagingRequest<SearchFilterAdminRequest> request) throws IOException;

    ExportResponse exportCustomerPurchaseHistory(Integer id) throws IOException;

    ExportResponse exportCustomerWarrantyHistory(Integer customerId) throws IOException;

    ExportResponse exportSparePartDetail(int id) throws IOException;

    ExportResponse exportSparepartList(PagingRequest<SearchFilterSparepartRequest> request) throws IOException;

    ExportResponse exportCampaignList(PagingRequest<SearchFilterCampaignRequest> request) throws IOException;

    ExportResponse exportCampaignDetail(int id) throws IOException;

    ExportResponse exportCarDetail(int id) throws IOException;

    ExportResponse exportCarList(PagingRequest<SearchFilterCarRequest> request) throws IOException;
}