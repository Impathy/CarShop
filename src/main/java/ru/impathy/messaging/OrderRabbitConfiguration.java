package ru.impathy.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("order & !test")
public class OrderRabbitConfiguration {
    @Bean
    public Queue orderResultQueue() {
        return new Queue("impathy.order.result.queue", true);
    }

    @Bean
    public Binding orderResultBinding(Queue orderResultQueue, TopicExchange impathyExchange) {
        return BindingBuilder.bind(orderResultQueue).to(impathyExchange).with("order.result");
    }
}
