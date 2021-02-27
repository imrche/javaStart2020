package com.rch.fuelcounter.drivers;

import com.rch.fuelcounter.cars.Car;
import com.rch.fuelcounter.exceptions.ApplicationException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Driver implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Integer defaultTariff  = 5;
    private static Map<String,Driver> drivers = new HashMap<>();

    private final String login;
    private final String name;
    private Car car = null;

    public Driver(String login, String name) throws ApplicationException {
        if (login == null || name == null)
            throw new ApplicationException("Не все данные для добавления водителя указаны!");
        this.login = login;
        this.name = name;
        drivers.put(login,this);
    }

    public static Driver getDriverByLogin(String login) throws ApplicationException {
        if(!drivers.containsKey(login))
            throw new ApplicationException("Водитель с сетевым именем " + login + " не найден!");
        return drivers.get(login);
    }

    public static void setDrivers(Map<String, Driver> drivers) {
        Driver.drivers = drivers;
    }

    public void appointToVehicle(Car car){
        if (this.car != null && this.car.hasDriver())//todo подумать надо ли оно(перекрестные ссылки) вообще
            this.car.getDriver().appointToVehicle(null);

        this.car = car;
        this.car.setDriver(this);
    }

    public void removeFromVehicle(){
        car.setDriver(null);
        car = null;
    }

    public Integer getTariff(){
        return defaultTariff;
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
