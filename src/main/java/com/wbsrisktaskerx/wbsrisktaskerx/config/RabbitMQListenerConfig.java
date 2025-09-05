package com.wbsrisktaskerx.wbsrisktaskerx.config;

import com.wbsrisktaskerx.wbsrisktaskerx.service.SmsService.SmsMessageHandler;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQListenerConfig {

    @Bean
    public MessageListenerAdapter messageListenerAdapter(SmsMessageHandler smsMessageHandler, Jackson2JsonMessageConverter jsonMessageConverter) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(smsMessageHandler);
        adapter.setMessageConverter(jsonMessageConverter);
        return adapter;
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
