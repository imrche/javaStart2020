package com.rch.fuelcounter.data.drivers;

import com.rch.fuelcounter.data.cars.Car;
import com.rch.fuelcounter.exceptions.ApplicationException;
import com.rch.fuelcounter.exceptions.DriverExistException;
import com.rch.fuelcounter.exceptions.IncorrectInputData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс для рабоы с водителями
 * <p><b>drivers</b> @static @map - список нанятых водителей</p>
 * <p><b>login</b> @String - сетевое имя водителя</p>
 * <p><b>name</b> @String - ФИО водителя</p>
 * <p><b>car</b> @Car - ссылка на ТС</p>
 * <p><b>defaultTariff</b> @Integer - ссылка на ТС</p>
 */
public class Driver implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Integer defaultTariff  = 5;
    private static Map<String,Driver> drivers = new HashMap<>();

    private final String login;
    private final String name;
    private Car car = null;

    public Driver(String login, String name) throws ApplicationException {
        if (login == null || name == null)
            throw new IncorrectInputData("Не все данные для добавления водителя указаны!");
        this.login = login;
        this.name = name;
        drivers.put(login,this);
    }

    /**
     * Получить инстанс водителя по логину
     * @param login логин
     * @return @Driver
     * @throws ApplicationException если водитель по логину не найден
     */
    public static Driver getDriverByLogin(String login) throws ApplicationException {
        if(!drivers.containsKey(login))
            throw new DriverExistException("Водитель с сетевым именем " + login + " не найден!");
        return drivers.get(login);
    }

    /**
     * Установка списка водителей (для загрузки из файла)
     * @param drivers список водителей
     */
    public static void setDrivers(Map<String, Driver> drivers) {
        Driver.drivers = drivers;
    }

    /**
     * Назначение водителя на заданное ТС
     * @param car ТС
     */
    public void appointToVehicle(Car car){
        if (this.car != null && this.car.hasDriver())
            this.car.setDriver(null);

        this.car = car;

        if (this.car != null)
            this.car.setDriver(this);
    }

    /**
     * Снять водителя с ТС
     */
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

    public boolean hasAppointment(){
        return car != null;
    }
}
