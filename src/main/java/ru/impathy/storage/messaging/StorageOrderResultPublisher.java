package ru.impathy.storage.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.impathy.messaging.OrderApprovalResultEvent;
import ru.impathy.messaging.RabbitConfiguration;

@Component
@Profile("storage & !test")
public class StorageOrderResultPublisher {
    private final RabbitTemplate rabbitTemplate;

    public StorageOrderResultPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(OrderApprovalResultEvent event) {
        rabbitTemplate.convertAndSend(RabbitConfiguration.EXCHANGE, "order.result", event);
    }
}
