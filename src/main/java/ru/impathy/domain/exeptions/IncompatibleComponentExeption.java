package ru.impathy.domain.exeptions;

public class IncompatibleComponentExeption extends RuntimeException {
    public IncompatibleComponentExeption(String message) {
        super(message);
    }
}
