package com.sinse.xmlappdemo.exception;

public class PharmacyException extends RuntimeException{

    public PharmacyException(String message) {
        super(message);
    }

    public PharmacyException(String message, Throwable cause) {
        super(message, cause);
    }

    public PharmacyException(Throwable cause) {
        super(cause);
    }
}
