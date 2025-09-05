package com.wbsrisktaskerx.wbsrisktaskerx.controller;

import com.wbsrisktaskerx.wbsrisktaskerx.common.constants.EndpointConstants;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.ApiResult;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.CampaignSmsNotificationsRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.service.SmsService.ISmsService;
import com.wbsrisktaskerx.wbsrisktaskerx.service.SmsService.SmsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(EndpointConstants.SMS)
public class SmsController {
    private final ISmsService smsService;
    private final SmsServiceImpl smsServiceImpl;

    public SmsController(ISmsService smsService, SmsServiceImpl smsServiceImpl) {
        this.smsService = smsService;
        this.smsServiceImpl = smsServiceImpl;
    }

    @PostMapping(EndpointConstants.NOTIFIED_TO_CUSTOMER)
    @Operation(
        summary = "Send Campaign SMS Notifications to Customers",
        description = "Processes and sends SMS notifications to customers based on campaign configuration. " +
                     "This endpoint handles bulk SMS delivery using asynchronous processing with RabbitMQ.",
        operationId = "notifyCustomers"
    )
    public ResponseEntity<ApiResult<Boolean>> notifiedToCustomer(@RequestBody @Valid CampaignSmsNotificationsRequest request) {
        return ResponseEntity.ok(ApiResult.success(smsService.processNotifiedToCustomer(request)));
    }
}
