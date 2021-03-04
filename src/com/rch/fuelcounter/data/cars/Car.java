package com.rch.fuelcounter.data.cars;

import com.rch.fuelcounter.data.drivers.Driver;
import com.rch.fuelcounter.exceptions.ApplicationException;
import com.rch.fuelcounter.exceptions.IncorrectInputData;

import java.io.Serializable;

/**
 * Класс для работы с ТС
 * <p><b>carType</b> @CarType - тип ТС</p>
 * <p><b>license</b> @String - номер ТС</p>
 * <p><b>driver</b> @CarType - водитель назначенный на ТС</p>
 */
public class Car implements Serializable {
    private static final long serialVersionUID = 1L;

    private final CarType carType;
    private final String license;
    private Driver driver;

    public Car(String carType, String license) throws ApplicationException {
        this(CarType.getTypeRef(carType), license);
    }

    public Car(CarType carType, String license) throws ApplicationException {
        if (license == null)
            throw new IncorrectInputData("Не указан номер для транспорта");
        this.carType = carType;
        this.license = license;
    }

    public String getIdentifier(){
        return String.format("%s (\"%s\")",license, carType.getName());
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

    public String getLicense() {
        return license;
    }

    public CarType getCarType(){
        return carType;
    }
}
