package com.example.project.ubx.frame.exception;

/**
 * Exception thrown when the underlying transport driver throws an exception.
 */
public final class UbxTransportDriverException extends UbxTransportException {

    public UbxTransportDriverException(String message) {
        super(message);
    }

    public UbxTransportDriverException(String message, Throwable cause) {
        super(message, cause);
    }

}
