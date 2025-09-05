package com.wbsrisktaskerx.wbsrisktaskerx.service.SmsService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class DynamicQueueListenerManager {

    private final SmsMessageHandler smsMessageHandler;
    private final ConnectionFactory connectionFactory;
    private final Map<String, SimpleMessageListenerContainer> containers = new ConcurrentHashMap<>();

    public DynamicQueueListenerManager( SmsMessageHandler smsMessageHandler, ConnectionFactory connectionFactory) {
        this.smsMessageHandler = smsMessageHandler;
        this.connectionFactory = connectionFactory;
    }

    public void startListening(String queueName) {
        if (containers.containsKey(queueName)) return;

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(smsMessageHandler);
        container.setConcurrentConsumers(2);
        container.setMaxConcurrentConsumers(4);
        container.setPrefetchCount(1);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setAdviceChain(RetryInterceptorBuilder.stateless()
                .maxAttempts(3)
                .backOffOptions(1000, 2.0, 10000) // exponential backoff
                .build());

        container.start();
        containers.put(queueName, container);
    }

    public void stopListening(String queueName) {
        SimpleMessageListenerContainer container = containers.remove(queueName);
        if (container != null) {
            container.stop();
            log.info("Stopped listener for queue: {}", queueName);
        } else {
            log.warn("No active listener for queue: {}", queueName);
        }
    }
}
