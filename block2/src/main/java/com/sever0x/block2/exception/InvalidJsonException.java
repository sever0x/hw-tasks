package com.sever0x.block2.exception;

import java.util.List;

public class InvalidJsonException extends RuntimeException {
    private final List<String> invalidRecords;

    public InvalidJsonException(String message, List<String> invalidRecords) {
        super(message);
        this.invalidRecords = invalidRecords;
    }

    public List<String> getInvalidRecords() {
        return invalidRecords;
    }
}
