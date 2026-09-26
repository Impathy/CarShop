package ru.impathy.integration.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.impathy.grpc.cars.v1.CarInventoryServiceGrpc;

@Configuration
@Profile("order")
public class OrderGrpcConfiguration {
    @Bean(destroyMethod = "shutdownNow")
    public ManagedChannel storageGrpcChannel(@Value("${app.grpc.storage-host:localhost}") String host,
                                             @Value("${app.grpc.storage-port:9091}") int port) {
        return ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
    }

    @Bean
    public CarInventoryServiceGrpc.CarInventoryServiceBlockingStub storageGrpcStub(ManagedChannel storageGrpcChannel) {
        return CarInventoryServiceGrpc.newBlockingStub(storageGrpcChannel);
    }
}
