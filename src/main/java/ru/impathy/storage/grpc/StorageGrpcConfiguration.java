package ru.impathy.storage.grpc;

import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("storage")
public class StorageGrpcConfiguration {
    private static final Logger log = LoggerFactory.getLogger(StorageGrpcConfiguration.class);

    @Bean(initMethod = "start", destroyMethod = "shutdownNow")
    public Server storageGrpcServer(StorageCarInventoryGrpcService service,
                                    @Value("${app.grpc.server-port:9091}") int port) {
        log.info("Starting storage gRPC server on port {}", port);
        return NettyServerBuilder.forPort(port)
                .addService(service)
                .build();
    }
}
