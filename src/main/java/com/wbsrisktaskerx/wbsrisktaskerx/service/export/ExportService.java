package com.wbsrisktaskerx.wbsrisktaskerx.service.export;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wbsrisktaskerx.wbsrisktaskerx.common.constants.ExportConstants;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.Payment;
import com.wbsrisktaskerx.wbsrisktaskerx.mapper.HistoryMapper;
import com.wbsrisktaskerx.wbsrisktaskerx.mapper.PaymentMapper;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.PagingRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.data.PaymentOptions;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.*;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.response.*;
import com.wbsrisktaskerx.wbsrisktaskerx.repository.*;
import com.wbsrisktaskerx.wbsrisktaskerx.service.sparepart.ISparepartService;
import com.wbsrisktaskerx.wbsrisktaskerx.utils.ExcelUtils;
import com.wbsrisktaskerx.wbsrisktaskerx.utils.PasswordExport;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.wbsrisktaskerx.wbsrisktaskerx.entity.QWarrantyHistory.warrantyHistory;

@Service
public class ExportService implements IExportService {
        private final CustomerJpaQueryRepository customerJpaQueryRepository;
        private final JPAQueryFactory jpaQueryFactory;
        private final HistoryQueryRepository historyQueryRepository;
        private final InstallmentRepository installmentRepository;
        private final PaymentRepository paymentRepository;
        private final AdminJpaQueryRepository adminJpaQueryRepository;
        private final OrderRepository orderRepository;
        private final ISparepartService sparepartService;
        private final SparepartJpaQueryRepository sparepartJpaQueryRepository;
        private final CampaignJpaQueryRepository campaignJpaQueryRepository;
        private final CarJpaQueryRepository carJpaQueryRepository;
        private final ExcelUtils excelUtils;

        public ExportService(CustomerJpaQueryRepository customerJpaQueryRepository, JPAQueryFactory jpaQueryFactory, HistoryQueryRepository historyQueryRepository,
                             InstallmentRepository installmentRepository, PaymentRepository paymentRepository,
                             AdminJpaQueryRepository adminJpaQueryRepository, OrderRepository orderRepository,
                             ISparepartService sparepartService, SparepartJpaQueryRepository sparepartJpaQueryRepository,
                             CampaignJpaQueryRepository campaignJpaQueryRepository,
                             CarJpaQueryRepository carJpaQueryRepository, ExcelUtils excelUtils) {
                this.customerJpaQueryRepository = customerJpaQueryRepository;
                this.jpaQueryFactory = jpaQueryFactory;
                this.installmentRepository = installmentRepository;
                this.paymentRepository = paymentRepository;
                this.adminJpaQueryRepository = adminJpaQueryRepository;
                this.historyQueryRepository = historyQueryRepository;
                this.orderRepository = orderRepository;
                this.sparepartService = sparepartService;
                this.sparepartJpaQueryRepository = sparepartJpaQueryRepository;
                this.campaignJpaQueryRepository = campaignJpaQueryRepository;
                this.carJpaQueryRepository = carJpaQueryRepository;
                this.excelUtils = excelUtils;
        }
        

        @Override
        public ExportResponse getCustomerList(PagingRequest<SearchFilterCustomersRequest> request) throws IOException {
                SearchFilterCustomersRequest filter = request.getFilters();
                List<CustomerResponse> content = customerJpaQueryRepository.findCustomersByFilter(filter);
                ExportDetails details = generateExportDetails();
                String fileName = String.format(ExportConstants.FILE_FORMAT, ExportConstants.FILENAME,
                                details.currentDate,
                                ExportConstants.XLSX);
                return excelUtils.customerToExcel(content, details.password, fileName);
        }

        @Override
        public ExportResponse exportCustomerPurchaseHistory(Integer customerId) throws IOException {
                List<OrderResponse> orderResponses = orderRepository.findByCustomerId(customerId)
                                .stream()
                                .map(PaymentMapper::orderMapper)
                                .toList();

                List<Integer> orderIds = orderResponses
                                .stream()
                                .map(OrderResponse::getId)
                                .toList();

                List<PaymentResponse> allPaymentResponses = paymentRepository.findByOrderIdIn(orderIds)
                                .stream()
                                .map(PaymentMapper::paymentMapper)
                                .toList();

                List<Integer> allPaymentsId = paymentRepository.findByOrderIdIn(orderIds)
                                .stream()
                                .map(Payment::getId)
                                .toList();

                List<Integer> installmentPaymentIds = allPaymentResponses.stream()
                                .filter(p -> p.getPaymentOption().equals(PaymentOptions.Installment))
                                .map(PaymentResponse::getId)
                                .distinct()
                                .toList();

                List<InstallmentsResponse> installmentsResponses = installmentRepository
                                .findByPaymentsIdIn(installmentPaymentIds)
                                .stream()
                                .map(PaymentMapper::installmentsMapper)
                                .toList();

                List<PurchaseHistoryResponse> purchaseHistoryResponses = historyQueryRepository
                                .getListPurchaseHistoryWithExport(customerId);

                ExportDetails details = generateExportDetails();
                String fileName = String.format(ExportConstants.ID_FILE_FORMAT,
                                ExportConstants.PURCHASE_HISTORY_CUSTOMER,
                                customerId,
                                details.currentDate,
                                ExportConstants.XLSX);

                return excelUtils.purchaseHistoryToExcel(
                        purchaseHistoryResponses,
                        installmentsResponses,
                        allPaymentsId,
                        details.password,
                        fileName);
        }

        @Override
        public ExportResponse getAdminList(PagingRequest<SearchFilterAdminRequest> request) throws IOException {
                List<AdminResponse> content = adminJpaQueryRepository
                                .searchedAndFilteredAdminNoPaging(request.getFilters());
                ExportDetails details = generateExportDetails();
                String fileName = String.format(ExportConstants.FILE_FORMAT, ExportConstants.ADMIN_FILENAME,
                                details.currentDate, ExportConstants.XLSX);
                return ExcelUtils.adminToExcel(content, details.password, fileName);
        }

        @Override
        public ExportResponse exportCustomerWarrantyHistory(Integer customerId) throws IOException {
                List<WarrantyHistoryResponse> warrantyHistoryResponses = jpaQueryFactory.selectFrom(warrantyHistory)
                                .where(warrantyHistory.customer.id.eq(customerId))
                                .fetch()
                                .stream()
                                .map(HistoryMapper::warrantyHistoryMapper)
                                .collect(Collectors.toList());

                ExportDetails details = generateExportDetails();
                String fileName = String.format(ExportConstants.ID_FILE_FORMAT,
                                ExportConstants.WARRANTY_HISTORY_CUSTOMER,
                                customerId, details.currentDate, ExportConstants.XLSX);

                return ExcelUtils.warrantyHistoryToExcel(warrantyHistoryResponses, details.password, fileName);
        }

        private ExportDetails generateExportDetails() {
                String password = PasswordExport.generatePassword();
                String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern(ExportConstants.DATE_TIME));
                return new ExportDetails(password, currentDate);
        }

        @Override
        public ExportResponse exportSparePartDetail(int id) throws IOException {
                SparepartDetailResponse detail = sparepartService.getDetailSparepartById(id);

                ExportDetails details = generateExportDetails();
                String fileName = String.format(
                                ExportConstants.ID_FILE_FORMAT,
                                ExportConstants.SPAREPART_DETAIL_FILENAME,
                                id,
                                details.currentDate,
                                ExportConstants.XLSX);

                return ExcelUtils.sparepartDetailToExcel(detail, details.password, fileName);
        }

        /*
         * EXPORT SPARE PART MODULES
         */
        @Override
        public ExportResponse exportSparepartList(PagingRequest<SearchFilterSparepartRequest> request)
                        throws IOException {
                List<SparepartResponse> content = sparepartJpaQueryRepository
                                .searchedAndFilteredSparepartNoPaging(request.getFilters());
                ExportDetails details = generateExportDetails();
                String fileName = String.format(ExportConstants.FILE_FORMAT, ExportConstants.SPAREPART_FILENAME,
                                details.currentDate, ExportConstants.XLSX);
                return ExcelUtils.sparepartListToExcel(content, fileName, details.password);
        }

        /*
         * EXPORT CAMPAIGN MODULES
         */
        @Override
        public ExportResponse exportCampaignList(PagingRequest<SearchFilterCampaignRequest> request)
                        throws IOException {
                List<CampaignResponse> content = campaignJpaQueryRepository
                                .findCampaignsByFilterNoPaging(request.getFilters());
                ExportDetails details = generateExportDetails();
                String fileName = String.format(ExportConstants.FILE_FORMAT, ExportConstants.CAMPAIGN_FILENAME,
                                details.currentDate, ExportConstants.XLSX);
                return ExcelUtils.campaignListToExcel(content, fileName, details.password);
        }

        @Override
        public ExportResponse exportCampaignDetail(int id) throws IOException {
                CampaignResponse campaignOpt = campaignJpaQueryRepository.findCampaignById(id);
                ExportDetails details = generateExportDetails();
                String fileName = String.format(
                                ExportConstants.ID_FILE_FORMAT,
                                ExportConstants.CAMPAIGN_DETAIL_FILENAME,
                                id,
                                details.currentDate,
                                ExportConstants.XLSX);

                return ExcelUtils.campaignDetailToExcel(campaignOpt, details.password, fileName);
        }

        /*
         * EXPORT CAR MODULES
         */
        @Override
        public ExportResponse exportCarDetail(int id) throws IOException {
                CarDetailResponse detail = carJpaQueryRepository.getCarDetail(id);

                ExportDetails details = generateExportDetails();
                String fileName = String.format(
                                ExportConstants.ID_FILE_FORMAT,
                                ExportConstants.CAR_DETAIL_FILENAME,
                                id,
                                details.currentDate,
                                ExportConstants.XLSX);

                return ExcelUtils.CarDetailToExcel(detail, details.password, fileName);
        }

        @Override
        public ExportResponse exportCarList(PagingRequest<SearchFilterCarRequest> request)
                        throws IOException {
                List<CarResponse> content = carJpaQueryRepository.searchedAndFilteredCarNoPaging(request.getFilters());
                ExportDetails details = generateExportDetails();
                String fileName = String.format(ExportConstants.FILE_FORMAT, ExportConstants.CAR_FILENAME,
                                details.currentDate, ExportConstants.XLSX);
                return ExcelUtils.carListToExcel(content, fileName, details.password);
        }

}