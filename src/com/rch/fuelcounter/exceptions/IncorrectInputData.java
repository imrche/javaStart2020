package com.rch.fuelcounter.exceptions;

public class IncorrectInputData extends ApplicationException{

    public IncorrectInputData() {
    }

    public IncorrectInputData(String message) {
        super(message);
    }

    public IncorrectInputData(String message, Throwable cause) {
        super(message, cause);
    }
}
