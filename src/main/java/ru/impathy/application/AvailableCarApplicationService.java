package ru.impathy.application;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.impathy.integration.dto.AvailableCarDto;
import ru.impathy.integration.grpc.StorageCarInventoryGrpcClient;

import java.util.List;
import java.util.UUID;

@Service
@Profile("order")
public class AvailableCarApplicationService {
    private final StorageCarInventoryGrpcClient client;

    public AvailableCarApplicationService(StorageCarInventoryGrpcClient client) {
        this.client = client;
    }

    public List<AvailableCarDto> list() {
        return client.listAvailableCars();
    }

    public AvailableCarDto get(UUID id) {
        return client.getAvailableCar(id);
    }
}
