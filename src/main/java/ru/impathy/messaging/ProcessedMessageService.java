package ru.impathy.messaging;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.impathy.persistence.entity.ProcessedMessageJpaEntity;
import ru.impathy.persistence.repository.ProcessedMessageJpaRepository;

@Service
public class ProcessedMessageService {
    private final ProcessedMessageJpaRepository repository;

    public ProcessedMessageService(ProcessedMessageJpaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public boolean alreadyProcessed(String messageId) {
        return repository.findByMessageId(messageId).isPresent();
    }

    @Transactional
    public void markProcessed(String messageId, String messageType) {
        if (repository.findByMessageId(messageId).isPresent()) {
            return;
        }
        ProcessedMessageJpaEntity entity = new ProcessedMessageJpaEntity();
        entity.setMessageId(messageId);
        entity.setMessageType(messageType);
        repository.save(entity);
    }
}
