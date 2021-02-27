package com.rch.fuelcounter.exceptions;

public class CloseSessionError extends SessionError{

    public CloseSessionError() {
    }

    public CloseSessionError(String message) {
        super(message);
    }

    public CloseSessionError(String message, Throwable cause) {
        super(message, cause);
    }
}
