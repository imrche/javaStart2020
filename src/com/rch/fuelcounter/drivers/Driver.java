package com.rch.fuelcounter.drivers;

import com.rch.fuelcounter.cars.Car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Driver {
    private static final Integer defaultTariff  = 5;
    private static Map<String,Driver> drivers = new HashMap<>();

    private final String login;
    private final String name;

    public Driver(String name, String login){
        this.login = login;
        this.name = name;
        drivers.put(login,this);
    }

    private Car car = null;

    public static Driver getDriverByLogin(String login){
        return drivers.get(login);
    }

    public void appointToVehicle(Car car){
        this.car = car;
        this.car.setDriver(this);
    }

    public void removeFromVehicle(){
        car.setDriver(null);
        car = null;
    }

    public boolean isAppointedToCar(){
        return car != null;
    }

    public static Map<String,Driver> getDriversList(){
        return drivers;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public Car getCar() {
        return car;
    }

    public String getAppointedDescription(){
        if (car == null)
            return "НЕ НАЗНАЧЕН";
        return car.getIdentifier();
    }
}
