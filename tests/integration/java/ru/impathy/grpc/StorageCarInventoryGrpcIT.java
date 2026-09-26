package ru.impathy.grpc;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.impathy.App;
import ru.impathy.grpc.cars.v1.AvailableCarResponse;
import ru.impathy.grpc.cars.v1.AvailableCarsResponse;
import ru.impathy.grpc.cars.v1.CarIdRequest;
import ru.impathy.grpc.cars.v1.CarInventoryServiceGrpc;
import ru.impathy.integration.StorageGrpcPostgresIntegrationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = App.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class StorageCarInventoryGrpcIT extends StorageGrpcPostgresIntegrationTest {

    @Test
    void shouldListAndGetAvailableCarsViaGrpc() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 19093)
                .usePlaintext()
                .build();
        try {
            CarInventoryServiceGrpc.CarInventoryServiceBlockingStub stub = CarInventoryServiceGrpc.newBlockingStub(channel);

            AvailableCarsResponse listResponse = stub.listAvailableCars(Empty.getDefaultInstance());
            assertNotNull(listResponse);
            assertFalse(listResponse.getCarsList().isEmpty());
            assertEquals("20000000-0000-0000-0000-000000000001", listResponse.getCars(0).getId());

            AvailableCarResponse getResponse = stub.getAvailableCar(CarIdRequest.newBuilder()
                    .setId("20000000-0000-0000-0000-000000000001")
                    .build());
            assertNotNull(getResponse.getCar());
            assertEquals("BMW", getResponse.getCar().getBrand());
        } finally {
            channel.shutdownNow();
        }
    }
}
