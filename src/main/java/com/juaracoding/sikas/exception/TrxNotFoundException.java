package com.juaracoding.sikas.exception;

public class TrxNotFoundException extends RuntimeException {

    public TrxNotFoundException(String message) {
        super(message);
    }
}
