package com.sinse.electroshop.exception;

public class ProductRegistException extends RuntimeException{
    public ProductRegistException(String message) {
        super(message);
    }

    public ProductRegistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductRegistException(Throwable cause) {
        super(cause);
    }
}
