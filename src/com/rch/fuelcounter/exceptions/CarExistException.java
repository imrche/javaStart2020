package com.rch.fuelcounter.exceptions;

public class CarExistException extends ApplicationException{

    public CarExistException() {
    }

    public CarExistException(String message) {
        super(message);
    }

    public CarExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
