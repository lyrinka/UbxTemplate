package com.example.project.twi.exception;

/**
 * TWI driver errors.
 * These errors originate from the driver itself and are not related to external hardware and the bus.
 */
public class TwiDriverException extends Exception {

    public TwiDriverException(String message) {
        super(message);
    }

    public TwiDriverException(String message, Throwable cause) {
        super(message, cause);
    }

}
