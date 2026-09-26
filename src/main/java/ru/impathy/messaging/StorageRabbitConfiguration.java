package ru.impathy.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("storage & !test")
public class StorageRabbitConfiguration {
    @Bean
    public Queue orderApprovalQueue() {
        return new Queue("impathy.order.approval.queue", true);
    }

    @Bean
    public Binding orderApprovalBinding(Queue orderApprovalQueue, TopicExchange impathyExchange) {
        return BindingBuilder.bind(orderApprovalQueue).to(impathyExchange).with("order.sent");
    }
}
