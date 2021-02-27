package com.rch.fuelcounter.exceptions;

public class NullCommandValueException extends ApplicationException {
    public NullCommandValueException() {
    }

    public NullCommandValueException(String message) {
        super(message);
    }
}
