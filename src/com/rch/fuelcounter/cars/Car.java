package com.rch.fuelcounter.cars;

import com.rch.fuelcounter.drivers.Driver;
import com.rch.fuelcounter.exceptions.ApplicationException;
import com.rch.fuelcounter.util.Util;

import java.io.Serializable;


public class Car implements Serializable {
    private static final long serialVersionUID = 1L;

    private final CarType carType;
    private final String licence;
    private Driver driver;

    public Car(String carType, String licence) throws ApplicationException {
        this(CarType.getType(carType), licence);
    }

    public Car(CarType carType, String licence) throws ApplicationException {
        if (licence == null)
            throw new ApplicationException("Не указан номер для транспорта");
        this.carType = carType;
        this.licence = licence;
    }

    public void setDriver(Driver driver){
        this.driver = driver;
    }

    public Driver getDriver() {
        return driver;
    }

    public boolean hasDriver(){
        return driver != null;
    }

    public String getIdentifier(){
        return String.format("%s_%s (класс \"%s\")",carType.getType(),licence,carType.getName());
    }

    public String getType(){
        return carType.getType();
    }
    public String getLicence() {
        return licence;
    }


    public CarType getCarType2(){
        return carType;
    }



}
