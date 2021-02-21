package com.rch.fuelcounter;

import com.rch.fuelcounter.cars.Car;
import com.rch.fuelcounter.cars.CarPark;
import com.rch.fuelcounter.drivers.Driver;
import com.rch.fuelcounter.session.Session;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoredClass implements Serializable {
    private static final String dataFilePath = Session.class.getClassLoader().getResource("").getPath() + "data.bin";

    private static final long serialVersionUID = 1L;

    private Map<String, Car> cars;
    private Map<String, Driver> drivers;

    public Map<String, Car> getCars() {
        return cars;
    }

    public void setCars(Map<String, Car> cars) {
        this.cars = cars;
    }

    public Map<String, Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(Map<String, Driver> drivers) {
        this.drivers = drivers;
    }

    public static void loadStoredData() throws LoadDataException {
        try {
            FileInputStream r = new FileInputStream(dataFilePath);
            if (r.available() > 0) {
                ObjectInputStream o = new ObjectInputStream(r);
                StoredClass storedClass = (StoredClass) o.readObject();
                Driver.setDrivers(storedClass.drivers);
                CarPark.setCarsList(storedClass.cars);
            }
        } catch (FileNotFoundException e) {
            throw new LoadDataException("Файл с сохраненными данными отсутствует!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveStoredData() throws LoadDataException {
        try {
            FileOutputStream fo = new FileOutputStream(dataFilePath);
            ObjectOutputStream o = new ObjectOutputStream(fo);
            StoredClass storedClass = new StoredClass();
            storedClass.setDrivers( Driver.getDriversList());
            storedClass.setCars(CarPark.getCarsList());
            o.writeObject(storedClass);
            o.close();
        } catch (FileNotFoundException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
