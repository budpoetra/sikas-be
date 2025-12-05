package com.juaracoding.sikas.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TransactionNumberGenerator {

    private static final String PREFIX = "TRX";
    private static final AtomicLong counter = new AtomicLong(1);

    @Value("${app.transaction.number.format:TRX-%d-%tY%tm%td-%06d}")
    private String format;

    public String generate() {
        LocalDateTime now = LocalDateTime.now();
        long seq = counter.getAndIncrement();
        return String.format(format,
                now.getYear() % 100,
                now, now, now,
                seq % 1000000);
    }
}