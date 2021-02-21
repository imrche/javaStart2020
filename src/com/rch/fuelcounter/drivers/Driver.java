package com.rch.fuelcounter.drivers;

import com.rch.fuelcounter.cars.Car;
import com.rch.fuelcounter.session.Session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Driver implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Integer defaultTariff  = 5;
    private static Map<String,Driver> drivers = new HashMap<>();

    private final String login;
    private final String name;
    private Car car = null;

    public Driver(String login, String name){
        this.login = login;
        this.name = name;
        drivers.put(login,this);
    }

    public static Driver getDriverByLogin(String login){
        return drivers.get(login);
    }

    public static void setDrivers(Map<String, Driver> drivers) {
        Driver.drivers = drivers;
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

/*    public static void loadData() {
        String path = Session.class.getClassLoader().getResource("").getPath();

        try {
            BufferedReader br = new BufferedReader(new FileReader(path + "drivers.data"));

            String line = br.readLine();
            while(!line.isEmpty()){
                String[] driverData = line.split("/");
                if (driverData.length == 2)
                    new Driver(driverData[1].trim(),driverData[0].trim());
                else
                    System.out.println("ВНИМАНИЕ! При загрузке данных водителей найден строка не соответствующая формату (" + line + ")");

                line = br.readLine();
            }
        } catch (Exception e) {
            System.out.println("ОШИБКА при загрузке данных водителей!");
        }
    }*/
}
