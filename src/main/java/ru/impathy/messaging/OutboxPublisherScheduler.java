package ru.impathy.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Profile;
import ru.impathy.persistence.entity.OutboxMessageJpaEntity;
import ru.impathy.persistence.repository.OutboxMessageJpaRepository;

import java.util.List;

@Component
@Profile("order & !test")
public class OutboxPublisherScheduler {
    private final OutboxMessageJpaRepository repository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public OutboxPublisherScheduler(OutboxMessageJpaRepository repository,
                                    RabbitTemplate rabbitTemplate,
                                    ObjectMapper objectMapper) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelayString = "${app.outbox.publish-delay-ms:2000}")
    @Transactional
    public void publish() {
        List<OutboxMessageJpaEntity> messages = repository.findAllByPublishedFalseAndRemovedFalseOrderByCreatedAtAsc();
        for (OutboxMessageJpaEntity message : messages) {
            OrderApprovalEvent event = read(message.getPayload());
            rabbitTemplate.convertAndSend(RabbitConfiguration.EXCHANGE, "order.sent", event);
            message.setPublished(true);
            repository.save(message);
        }
    }

    private OrderApprovalEvent read(String payload) {
        try {
            return objectMapper.readValue(payload, OrderApprovalEvent.class);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to deserialize outbox event", e);
        }
    }
}
