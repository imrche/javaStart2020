package com.rch.fuelcounter.cars;

import com.rch.fuelcounter.util.MyLinkedList;
import com.rch.fuelcounter.util.Util;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CarPark {
    private static List<Car> cars = new ArrayList<>();

    public static Map<String, Car> getCarsList() {
        return carsList;
    }

    public static void setCarsList(Map<String, Car> carsList) {
        CarPark.carsList = carsList;
    }

    private static Map<String, Car> carsList = new HashMap<>();

    public static Car addCar(String type, String license){
        String key = type + "_" + license;
        if (carsList.containsKey(key))
            return carsList.get(key);

        carsList.put(key, new Car(type,license));
        return carsList.get(key);
    }

    public static Car getCar(String type, String license){
        String key = type + "_" + license;
/*        if (!carsList.containsKey(key))
            return null;*/

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

    public static boolean checkFormat(String str){
        RegData data = new RegData(Util.parse(str));
        Car car = getCar(data.type, data.licence);
        if ( car == null) {
            System.out.println("Машина не зарегистрирована");//todo exception
            return false;
        }
        if (!car.hasDriver()){
            System.out.println("Машина не назначена водителю!");
            return false;
        }
        return true;
    }

    //todo переделать на мапу с индексом type_license
    public static void fabric(String str){
        RegData data = new RegData(Util.parse(str));
        Car car = findCar(data.type, data.licence);

        if (car != null) {
            if (!car.hasDriver()) {
                System.out.println("Машина не назначена водителю!");
                return;
            }
            car.incrementMileage(data.mileage);
            car.incrementAdditional(data.additional);
        } else
            add(new Car(CarType.types.get(data.type), data.licence, data.mileage, data.additional));
    }

    public static void add(Car car){
        cars.add(car);
    }

    public static void setCars(List<Car> cars) {
        CarPark.cars = cars;
    }

    public static List<Car> getListCar(String type){
        if (type != null){
            List<Car> returnList = new ArrayList<>();
            for (Car car : cars)
                if (car.getType().equals(type))
                    returnList.add(car);
            return returnList;
        }
        return cars;
    }

    public static Car findCar(String type, String licence){
        for (Car car : cars)
            if (car.getType().equals(type) && car.getLicence().equals(licence))
                return car;
        return null;
    }

    public static List<Car> sortListCar(List<Car> cars, String sortPar, String sortType) {
        MyLinkedList<Car> resultList = new MyLinkedList<>();
        LinkedList<Car> nullsList = new LinkedList<>();
        while (cars.size() > 0) {
            int i = 0;
            Integer sortValue;
            Car carTmp = null;
            for (Car car:cars) {
                sortValue = car.getSorterParam(sortPar);
                if (sortValue == null){
                    nullsList.add(car);
                    continue;
                }

                if (car.getSorterParam(sortPar) >= i) {
                    i = car.getSorterParam(sortPar);
                    carTmp = car;
                }
            }
            resultList.addForSort(carTmp, sortType);
            cars.remove(carTmp);
            for (Car car : nullsList) cars.remove(car);
        }
        resultList.addAll(nullsList);

        return resultList;
    }

    public static Map<String, Float> getTypeFullCost(String type){
        Map<String, Float> agrResult = new HashMap<>();
        for (Car car : getListCar(type))
            agrResult.put(car.getType(), agrResult.getOrDefault(car.getType(), 0F) + car.getFullCost());

        return agrResult;
    }

    public static void showFullCost(String type){
        for (Map.Entry<String, Float> e : getTypeFullCost(type).entrySet())
            System.out.println(CarType.types.get(e.getKey()).getName() + " " + e.getValue());
    }

    public static void showExtremumCost(boolean max){
        Map.Entry<String, Float> extr = null;

        for (Map.Entry<String, Float> e : getTypeFullCost(null).entrySet())
            if (Util.compare(e.getValue(), extr != null ? extr.getValue() : e.getValue(), max))
                extr = e;

        if (extr != null)
            System.out.println((max ? "Наибольшая" : "Наименьшая") +  " стоимость расхода у \"" + CarType.types.get(extr.getKey()).getName() + "\" -> " + extr.getValue());
    }

    public static void showStat(String sortPar, String sortType){
        for (Map.Entry<String, CarType> type : CarType.types.entrySet()) {
            System.out.println("------ " + type.getValue().getName() + " ------");
            System.out.println("Номер    Пробег      " + type.getValue().getNameAdditional());
            for (Car car: sortListCar(getListCar(type.getKey()),sortPar,sortType))
                System.out.printf("%s        %s         %s%n",car.getLicence(), car.getMileage(), car.getAdditional());
        }
    }
}
