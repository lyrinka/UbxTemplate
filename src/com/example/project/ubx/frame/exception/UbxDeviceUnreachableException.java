package com.example.project.ubx.frame.exception;

/**
 * Exception thrown when the UBlox device is unreachable.
 */
public final class UbxDeviceUnreachableException extends UbxTransportException {

    public UbxDeviceUnreachableException(String message) {
        super(message);
    }

    public UbxDeviceUnreachableException(String message, Throwable cause) {
        super(message, cause);
    }

}
