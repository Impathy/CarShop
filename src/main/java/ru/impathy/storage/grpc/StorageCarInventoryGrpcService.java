package ru.impathy.storage.grpc;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.impathy.grpc.cars.v1.AvailableCar;
import ru.impathy.grpc.cars.v1.AvailableCarResponse;
import ru.impathy.grpc.cars.v1.AvailableCarsResponse;
import ru.impathy.grpc.cars.v1.CarIdRequest;
import ru.impathy.grpc.cars.v1.CarInventoryServiceGrpc;
import ru.impathy.storage.persistence.entity.CarStockJpaEntity;
import ru.impathy.storage.persistence.repository.CarStockJpaRepository;
import ru.impathy.persistence.entity.CarJpaEntity;
import ru.impathy.persistence.repository.CarJpaRepository;

import java.util.List;
import java.util.UUID;

@Service
@Profile("storage")
public class StorageCarInventoryGrpcService extends CarInventoryServiceGrpc.CarInventoryServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(StorageCarInventoryGrpcService.class);

    private final CarStockJpaRepository carStockRepository;
    private final CarJpaRepository carRepository;

    public StorageCarInventoryGrpcService(CarStockJpaRepository carStockRepository, CarJpaRepository carRepository) {
        this.carStockRepository = carStockRepository;
        this.carRepository = carRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void listAvailableCars(Empty request, StreamObserver<AvailableCarsResponse> responseObserver) {
        log.info("gRPC listAvailableCars request received");
        List<AvailableCar> cars = carStockRepository.findAllByRemovedFalseOrderByCreatedAtDesc()
                .stream()
                .map(this::toProto)
                .filter(car -> car != null)
                .toList();
        responseObserver.onNext(AvailableCarsResponse.newBuilder().addAllCars(cars).build());
        responseObserver.onCompleted();
    }

    @Override
    @Transactional(readOnly = true)
    public void getAvailableCar(CarIdRequest request, StreamObserver<AvailableCarResponse> responseObserver) {
        UUID carId = parseUuid(request.getId());
        log.info("gRPC getAvailableCar request received for {}", carId);
        CarStockJpaEntity stock = carStockRepository.findByCarIdAndRemovedFalse(carId)
                .orElseThrow(() -> Status.NOT_FOUND.withDescription("Car not found in stock: " + carId).asRuntimeException());
        if (!stock.isAvailable()) {
            throw Status.NOT_FOUND.withDescription("Car not found in stock: " + carId).asRuntimeException();
        }
        AvailableCar car = toProto(stock);
        responseObserver.onNext(AvailableCarResponse.newBuilder().setCar(car).build());
        responseObserver.onCompleted();
    }

    private AvailableCar toProto(CarStockJpaEntity stock) {
        if (!stock.isAvailable()) {
            return null;
        }
        CarJpaEntity car = carRepository.findByIdAndRemovedFalse(stock.getCarId()).orElse(null);
        if (car == null) {
            return null;
        }
        return AvailableCar.newBuilder()
                .setId(car.getId().toString())
                .setBrand(car.getBrand())
                .setModelName(car.getModelName())
                .setBodyType(car.getBodyType().name())
                .setFuelType(car.getFuelType().name())
                .setGearboxType(car.getGearboxType().name())
                .setDriveType(car.getDriveType().name())
                .setColor(car.getColor())
                .setEnginePowerHp(car.getEnginePowerHP())
                .setEngineVolume(car.getEngineVolume())
                .setBasePrice(car.getBasePrice())
                .setExtraPrice(car.getExtraPrice())
                .setAvailableQuantity(stock.getTotalQuantity() - stock.getReservedQuantity())
                .build();
    }

    private UUID parseUuid(String value) {
        try {
            return UUID.fromString(value);
        } catch (Exception e) {
            throw Status.INVALID_ARGUMENT.withDescription("Invalid car id: " + value).asRuntimeException();
        }
    }
}
