package ru.impathy.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Profile;
import ru.impathy.messaging.OrderApprovalEvent;
import ru.impathy.persistence.entity.OutboxMessageJpaEntity;
import ru.impathy.persistence.repository.OutboxMessageJpaRepository;

@Service
@Profile("order")
public class OrderOutboxService {
    private final OutboxMessageJpaRepository repository;
    private final ObjectMapper objectMapper;

    public OrderOutboxService(OutboxMessageJpaRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void enqueueApproval(OrderApprovalEvent event) {
        if (event.getEventId() == null) {
            event.setEventId(java.util.UUID.randomUUID());
        }
        OutboxMessageJpaEntity message = new OutboxMessageJpaEntity();
        message.setEventType("ORDER_SENT_FOR_APPROVAL");
        message.setAggregateId(event.getOrderId().toString());
        message.setTraceId(event.getTraceId());
        message.setPayload(write(event));
        message.setPublished(false);
        repository.save(message);
    }

    private String write(OrderApprovalEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize outbox event", e);
        }
    }
}
