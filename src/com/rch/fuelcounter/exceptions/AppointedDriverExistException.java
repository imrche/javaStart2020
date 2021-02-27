package com.rch.fuelcounter.exceptions;

public class AppointedDriverExistException extends ApplicationException{

    public AppointedDriverExistException() {
    }

    public AppointedDriverExistException(String message) {
        super(message);
    }

    public AppointedDriverExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
