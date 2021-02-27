package com.rch.fuelcounter.cars;

import com.rch.fuelcounter.exceptions.ApplicationException;
import com.rch.fuelcounter.exceptions.CarExistException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Управление автопарком
 */
public class CarPark {
    private static List<Car> cars = new ArrayList<>();

    public static Map<String, Car> getCarsList() {
        return carsList;
    }

    public static void setCarsList(Map<String, Car> carsList) {
        CarPark.carsList = carsList;
    }

    private static Map<String, Car> carsList = new HashMap<>();

    public static Car addCar(String type, String license) throws ApplicationException {
        String key = type + "_" + license;
        if (carsList.containsKey(key))
            return carsList.get(key);

        carsList.put(key, new Car(type,license));
        return carsList.get(key);
    }

    public static Car getCar(String type, String license){
        String key = type + "_" + license;
        return carsList.get(key);
    }

    public static Car getCar(String key) throws ApplicationException {
        if (key == null)//todo проверить на шаблон ключа
            throw new ApplicationException("Для поиска машины передан пустой ключ!");
        if (!carsList.containsKey(key))
            throw new CarExistException("Машина с кодом " + key + " не найдена!");
        return carsList.get(key);
    }


    public static Collection<Car> getCars(String type){
        if (type != null)
            return carsList.entrySet().stream()
                    .filter(entry -> entry.getKey().contains(type + "_"))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());

        return carsList.values();
    }


    public static void add(Car car){
        cars.add(car);
    }

    public static void setCars(List<Car> cars) {
        CarPark.cars = cars;
    }

}
