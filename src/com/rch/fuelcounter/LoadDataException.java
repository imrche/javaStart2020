package com.rch.fuelcounter;

public class LoadDataException extends Exception{
    private String text;

    public LoadDataException(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
