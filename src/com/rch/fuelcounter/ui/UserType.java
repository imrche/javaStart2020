package com.rch.fuelcounter.ui;

import com.rch.fuelcounter.exceptions.ApplicationException;
import com.rch.fuelcounter.exceptions.IncorrectInputData;

/**
 * Пользователи приложения
 */
public enum UserType {
    admin,
    user;

    private  String password;

    public void setPassword(String password) throws ApplicationException {
        if (String.valueOf(password) == null)
            throw new IncorrectInputData("Пароль не может быть пустым!");
        if (password.contains(" "))
            throw new IncorrectInputData("Пароль не может содержать пробелы!");

        this.password = password;
    }

    public String getPassword(){
        return password;
    }

    public boolean checkCredential(String password){
        return password.equals(this.password);
    }
}