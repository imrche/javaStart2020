package com.rch.fuelcounter.cars;

import com.rch.fuelcounter.exceptions.ApplicationException;
import com.rch.fuelcounter.exceptions.CarExistException;
import com.rch.fuelcounter.exceptions.IncorrectInputData;
import com.rch.fuelcounter.session.AnalysisData;
import com.rch.fuelcounter.session.SessionDataManager;
import com.rch.fuelcounter.util.MyLinkedList;
import com.rch.fuelcounter.util.Util;

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

    public static Integer getSorterParam(String type, Car car,AnalysisData data){
        if(type == null || type.equals("mile"))//todo бросать прагму и обрабатывать если тип не передали
            return data.getCarMileage(car);
        else if (type.equals("add"))
            return data.getCarAdditional(car);
        else
            return null;
    }

    public static Collection<Car> sortListCar(Collection<Car> cars, String sortPar, String sortType, AnalysisData data) {
        MyLinkedList<Car> resultList = new MyLinkedList<>();
        LinkedList<Car> nullsList = new LinkedList<>();
        while (cars.size() > 0) {
            int i = 0;
            Integer sortValue;
            Car carTmp = null;
            for (Car car:cars) {
                sortValue = getSorterParam(sortPar,car,data);
                if (sortValue == null){
                    nullsList.add(car);
                    continue;
                }
//todo навести порядок
                if (getSorterParam(sortPar,car,data) >= i) {
                    i = getSorterParam(sortPar,car,data);
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

    private static Float calcFullCostCar(Car car, AnalysisData data){
        return data.getCarMileage(car) * car.getCarType2().getCostOnHundred() / 100
                + data.getCarMileage(car) * car.getDriver().getTariff();
    }

    public static Map<String, Float> getTypeFullCost(String type, AnalysisData data){
        Map<String, Float> agrResult = new HashMap<>();
        for (Car car : getCars(type))
            agrResult.put(car.getType(), agrResult.getOrDefault(car.getType(), 0F) + calcFullCostCar(car,data));

        return agrResult;
    }

    public static void showFullCost(String type, String startPeriod, String endPeriod){
        AnalysisData data = SessionDataManager.collectAnalysisData(startPeriod,endPeriod);

        for (Map.Entry<String, Float> e : getTypeFullCost(type,data).entrySet())
            System.out.println(CarType.types.get(e.getKey()).getName() + " " + e.getValue());
    }

    public static void showExtremumCost(String typeExtremum, String startPeriod, String endPeriod){
        AnalysisData data = SessionDataManager.collectAnalysisData(startPeriod,endPeriod);
        Map.Entry<String, Float> extr = null;
        boolean isMaximum = typeExtremum.equals("max");

        for (Map.Entry<String, Float> e : getTypeFullCost(null,data).entrySet())
            if (Util.compare(e.getValue(), extr != null ? extr.getValue() : e.getValue(), isMaximum))
                extr = e;

        if (extr != null)
            System.out.println((isMaximum ? "Наибольшая" : "Наименьшая") +  " стоимость расхода у \"" + CarType.types.get(extr.getKey()).getName() + "\" -> " + extr.getValue());
    }

    public static void showStat(String sortPar, String sortType, String startPeriod, String endPeriod){
        AnalysisData data = SessionDataManager.collectAnalysisData(startPeriod,endPeriod);

        for (Map.Entry<String, CarType> type : CarType.types.entrySet()) {
            System.out.println("------ " + type.getValue().getName() + " ------");
            System.out.println("Номер    Пробег      " + type.getValue().getNameAdditional());
            for (Car car: sortListCar(getCars(type.getKey()),sortPar,sortType,data))
                System.out.printf("%s        %s         %s%n",car.getLicence(), data.getCarMileage(car), data.getCarAdditional(car));
        }
    }
}
