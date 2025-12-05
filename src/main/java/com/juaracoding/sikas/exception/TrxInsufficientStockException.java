package com.juaracoding.sikas.exception;

public class TrxInsufficientStockException extends RuntimeException {

    public TrxInsufficientStockException(String message) {
        super(message);
    }
}