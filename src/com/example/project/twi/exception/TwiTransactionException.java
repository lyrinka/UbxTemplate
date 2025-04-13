package com.example.project.twi.exception;

/**
 * TWI transaction errors.
 * These errors are caused by external hardware and the bus, such as no acknowledgment from a slave device.
 */
public class TwiTransactionException extends Exception {

    public TwiTransactionException(String message) {
        super(message);
    }

    public TwiTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

}
