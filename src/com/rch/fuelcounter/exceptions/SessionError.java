package com.rch.fuelcounter.exceptions;

public class SessionError extends ApplicationException{
    public SessionError() {
    }

    public SessionError(String message) {
        super(message);
    }

    public SessionError(String message, Throwable cause) {
        super(message, cause);
    }
}
