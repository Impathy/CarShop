package ru.impathy.domain.exeptions;

public class EntityNotFoundExeption extends RuntimeException {
    public EntityNotFoundExeption(String message) {
        super(message);
    }
}
