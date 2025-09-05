package com.wbsrisktaskerx.wbsrisktaskerx.service.SmsService;

import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import com.wbsrisktaskerx.wbsrisktaskerx.exception.AppException;
import com.wbsrisktaskerx.wbsrisktaskerx.exception.ErrorCode;
import com.wbsrisktaskerx.wbsrisktaskerx.pojo.request.SmsRequest;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class SmsMessageHandler implements MessageListener {

    private final Jackson2JsonMessageConverter jsonMessageConverter;
    private final RabbitTemplate rabbitTemplate;
    private final MessageLimiter messageLimiter;
    private final ExecutorService retryExecutor = Executors.newSingleThreadScheduledExecutor();
    private static final Duration RETRY_DELAY = Duration.ofSeconds(2);

    @Value("${twilio.account_sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth_token}")
    private String AUTH_TOKEN;

    @Value("${twilio.messaging_service_sid}")
    private String MESSAGING_SERVICE_SID;

    public SmsMessageHandler(Jackson2JsonMessageConverter jsonMessageConverter, RabbitTemplate rabbitTemplate, MessageLimiter messageLimiter) {
        this.jsonMessageConverter = jsonMessageConverter;
        this.rabbitTemplate = rabbitTemplate;
        this.messageLimiter = messageLimiter;
    }

    @PostConstruct
    public void init(){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    @Override
    public void onMessage(Message message) {
        if (!messageLimiter.tryConsume()) {
            log.warn("Rate limit exceeded - skipping SMS temporarily");
            retryExecutor.submit(() -> retryLater(message));
            return;
        }

        try {
            SmsRequest sms = (SmsRequest) jsonMessageConverter.fromMessage(message);
            log.info("Received SMS FROM QUEUE: {}", sms);

            com.twilio.rest.api.v2010.account.Message messsage = com.twilio.rest.api.v2010.account.Message.creator(
                    new PhoneNumber(sms.getPhoneNumber()),
                    MESSAGING_SERVICE_SID,
                    sms.getMessage()
            ).create();
        } catch (Exception e) {
            throw new AppException(ErrorCode.SMS_GENERATE_FAILED);
        }
    }

    private void retryLater(Message message) {
        try {
            Thread.sleep(RETRY_DELAY.toMillis());
            rabbitTemplate.send(message.getMessageProperties().getConsumerQueue(), message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("Failed to requeue message", e);
        }
    }
}
