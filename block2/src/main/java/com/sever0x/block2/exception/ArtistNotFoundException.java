package com.sever0x.block2.exception;

public class ArtistNotFoundException extends RuntimeException {
    ArtistNotFoundException(String message) {
        super(message);
    }
}
