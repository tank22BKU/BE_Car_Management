package com.wbsrisktaskerx.wbsrisktaskerx.service.SmsService;

import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.CampaignSmsNotificationsRequest;
import jakarta.validation.Valid;

public interface ISmsService {
    Boolean processNotifiedToCustomer(CampaignSmsNotificationsRequest request);
}
