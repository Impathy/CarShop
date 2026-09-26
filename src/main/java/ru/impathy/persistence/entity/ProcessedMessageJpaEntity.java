package ru.impathy.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "processed_messages")
public class ProcessedMessageJpaEntity extends BaseJpaEntity {
    @Column(name = "message_id", nullable = false, unique = true)
    private String messageId;

    @Column(name = "message_type", nullable = false)
    private String messageType;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
