package com.example.project.ubx.frame.exception;

/**
 * Ubx transport adapter layer exceptions.
 */
public class UbxTransportException extends Exception {

    public UbxTransportException(String message) {
        super(message);
    }

    public UbxTransportException(String message, Throwable cause) {
        super(message, cause);
    }

}
