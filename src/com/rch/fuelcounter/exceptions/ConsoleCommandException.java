package com.rch.fuelcounter.exceptions;

public class ConsoleCommandException extends ApplicationException{

    public ConsoleCommandException() {
    }

    public ConsoleCommandException(String message) {
        super(message);
    }

    public ConsoleCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
