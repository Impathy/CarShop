package ru.impathy.integration.grpc;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.impathy.domain.exeptions.EntityNotFoundExeption;
import ru.impathy.domain.exeptions.GrpcServiceUnavailableException;
import ru.impathy.grpc.cars.v1.AvailableCar;
import ru.impathy.grpc.cars.v1.CarIdRequest;
import ru.impathy.grpc.cars.v1.CarInventoryServiceGrpc;
import ru.impathy.integration.dto.AvailableCarDto;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Profile("order")
public class StorageCarInventoryGrpcClient {
    private static final Logger log = LoggerFactory.getLogger(StorageCarInventoryGrpcClient.class);

    private final CarInventoryServiceGrpc.CarInventoryServiceBlockingStub stub;
    private final long timeoutMs;

    public StorageCarInventoryGrpcClient(CarInventoryServiceGrpc.CarInventoryServiceBlockingStub stub,
                                         @Value("${app.grpc.timeout-ms:2000}") long timeoutMs) {
        this.stub = stub;
        this.timeoutMs = timeoutMs;
    }

    public List<AvailableCarDto> listAvailableCars() {
        try {
            log.info("Requesting available cars from storage via gRPC");
            return stub.withDeadlineAfter(timeoutMs, TimeUnit.MILLISECONDS)
                    .listAvailableCars(Empty.getDefaultInstance())
                    .getCarsList()
                    .stream()
                    .map(this::toDto)
                    .toList();
        } catch (StatusRuntimeException ex) {
            throw translate(ex, "Failed to list available cars");
        }
    }

    public AvailableCarDto getAvailableCar(UUID id) {
        try {
            log.info("Requesting available car {} from storage via gRPC", id);
            return toDto(stub.withDeadlineAfter(timeoutMs, TimeUnit.MILLISECONDS)
                    .getAvailableCar(CarIdRequest.newBuilder().setId(id.toString()).build())
                    .getCar());
        } catch (StatusRuntimeException ex) {
            throw translate(ex, "Failed to get available car: " + id);
        }
    }

    private GrpcServiceUnavailableException translate(StatusRuntimeException ex, String message) {
        Status.Code code = ex.getStatus().getCode();
        if (code == Status.Code.NOT_FOUND) {
            throw new EntityNotFoundExeption(ex.getStatus().getDescription() == null ? message : ex.getStatus().getDescription());
        }
        if (code == Status.Code.UNAVAILABLE || code == Status.Code.DEADLINE_EXCEEDED) {
            return new GrpcServiceUnavailableException(message);
        }
        throw new GrpcServiceUnavailableException(message);
    }

    private AvailableCarDto toDto(AvailableCar car) {
        AvailableCarDto dto = new AvailableCarDto();
        dto.setId(UUID.fromString(car.getId()));
        dto.setBrand(car.getBrand());
        dto.setModelName(car.getModelName());
        dto.setBodyType(ru.impathy.domain.entities.cars.BodyType.valueOf(car.getBodyType()));
        dto.setFuelType(ru.impathy.domain.entities.cars.FuelType.valueOf(car.getFuelType()));
        dto.setGearboxType(ru.impathy.domain.entities.cars.GearboxType.valueOf(car.getGearboxType()));
        dto.setDriveType(ru.impathy.domain.entities.cars.DriveType.valueOf(car.getDriveType()));
        dto.setColor(car.getColor());
        dto.setEnginePowerHP(car.getEnginePowerHp());
        dto.setEngineVolume(car.getEngineVolume());
        dto.setBasePrice(car.getBasePrice());
        dto.setExtraPrice(car.getExtraPrice());
        dto.setAvailableQuantity(car.getAvailableQuantity());
        return dto;
    }
}
