package com.rch.fuelcounter.ui;

public enum UserType {
    admin("superpass"),
    user("simplepass");

    private final String password;

    UserType(String password){
        this.password = password;
    }
    public boolean checkCredential(String password){
        return password.equals(this.password);
    }
}
