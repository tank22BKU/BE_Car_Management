package com.wbsrisktaskerx.wbsrisktaskerx.service.SmsService;


import com.wbsrisktaskerx.wbsrisktaskerx.config.RabbitMQConfig;
import com.wbsrisktaskerx.wbsrisktaskerx.entity.Customer;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.CampaignSmsNotificationsRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SmsRequest;
import com.wbsrisktaskerx.wbsrisktaskerx.repository.CampaignJpaQueryRepository;
import com.wbsrisktaskerx.wbsrisktaskerx.repository.CampaignRepository;
import com.wbsrisktaskerx.wbsrisktaskerx.repository.CustomerRepository;
import com.wbsrisktaskerx.wbsrisktaskerx.utils.PhoneNumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SmsServiceImpl implements ISmsService{
    private final CustomerRepository customerRepository;
    private final RabbitTemplate rabbitTemplate;
    private final CampaignRepository campaignRepository;
    private final AmqpAdmin amqpAdmin;
    private final DynamicQueueListenerManager listenerManager;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public SmsServiceImpl(CustomerRepository customerRepository, RabbitTemplate rabbitTemplate, CampaignJpaQueryRepository campaignJpaQueryRepository, CampaignRepository campaignRepository, AmqpAdmin amqpAdmin, DynamicQueueListenerManager listenerManager) {
        this.customerRepository = customerRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.campaignRepository = campaignRepository;
        this.amqpAdmin = amqpAdmin;
        this.listenerManager = listenerManager;
    }

    private String generateMessage(String campaignName, String customerName) {
        return String.format("Hello %s ❤️\nRiskTaskers are pleased to announce a new campaign!\n%s is happening near you.\nVisit the nearest branch for exclusive offers.",
                customerName,
                campaignName
        );
    }

    @Override
    public Boolean processNotifiedToCustomer(CampaignSmsNotificationsRequest request) {
        List<Customer> listCustomer = customerRepository.findCustomersByTier(request.getTier());
        String campaignId = campaignRepository.findCampaignByCampaignName(request.getCampaignName()).getId().toString();
        String queueName = "sms_campaign_" + campaignId;

        declareQueue(queueName);
        // Start listening dynamically
        listenerManager.startListening(queueName);

        for (Customer customer : listCustomer) {
            String message = generateMessage(request.getCampaignName(), customer.getFullName());
            SmsRequest sms = new SmsRequest(PhoneNumberUtils.formatToPlus84(customer.getPhoneNumber()), message);
            rabbitTemplate.convertAndSend(queueName, sms);
            log.info("Published SMS to {}: {}", queueName, sms);
        }

        long timeoutMillis = listCustomer.size() * 60_000L;
        scheduler.schedule(() -> listenerManager.stopListening(queueName), timeoutMillis, TimeUnit.MILLISECONDS);
        return Boolean.TRUE;
    }

    private void declareQueue(String queueName) {
        Queue queue = QueueBuilder.durable(queueName)
                .autoDelete()
                .withArgument("x-expires", 60_000)
                .build();
        amqpAdmin.declareQueue(queue);

        Binding binding = BindingBuilder
                .bind(queue)
                .to(new DirectExchange(RabbitMQConfig.EXCHANGE_NAME))
                .with(queueName);
        amqpAdmin.declareBinding(binding);
    }
}


