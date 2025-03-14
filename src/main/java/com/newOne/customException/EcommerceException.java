package com.newOne.customException;

import org.springframework.http.HttpStatus;

/**
 * Custom exception class for handling errors specific to e-commerce operations.
 */
public class EcommerceException extends RuntimeException {
    private  HttpStatus status;

    public EcommerceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
    public EcommerceException( String message) {
        super(message);
    }


    public HttpStatus getStatus() {
        return status;
    }
}
