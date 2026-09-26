package ru.impathy.domain.exeptions;

public class GrpcServiceUnavailableException extends RuntimeException {
    public GrpcServiceUnavailableException(String message) {
        super(message);
    }
}
