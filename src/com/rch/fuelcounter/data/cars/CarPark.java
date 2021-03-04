package com.rch.fuelcounter.data.cars;

import com.rch.fuelcounter.exceptions.ApplicationException;
import com.rch.fuelcounter.exceptions.CarExistException;
import com.rch.fuelcounter.exceptions.IncorrectInputData;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Управление автопарком
 * <p><b>carList</b> @Map</> - список ТС в автопарке (ключ - ТИП_НОМЕР, значение - ссылка на ТС)</p>
 */
public class CarPark {
    //транспорт приписанный к автопарку
    private static Map<String, Car> carsList = new HashMap<>();

    /**
     * Добавить транспортное средство в автопарк
     * @param type тип автотранспорта строкой
     * @param license номер автотранспорта
     * @throws ApplicationException при некооректных введенных данных
     */
    public static void addCar(String type, String license) throws ApplicationException {
        String key = type + "_" + license;
        if (carsList.containsKey(key))
            return;

        carsList.put(key, new Car(type,license));
        carsList.get(key);
    }

    /**
     * Получить транспортное средство
     * @param type тип авто
     * @param license номер авто
     * @return ссылка на авто
     */
    public static Car getCar(String type, String license){
        String key = type + "_" + license;
        return carsList.get(key);
    }

    public static Map<String, Car> getCarsList() {
        return carsList;
    }

    public static void setCarsList(Map<String, Car> carsList) {
        CarPark.carsList = carsList;
    }

    /**
     * Получить транспортное средство по уникальному коду
     * @param key код авто в формате {ТИП_НОМЕР}
     * @return ссылка на авто
     * @throws ApplicationException при некорректном введенном ключе
     */
    public static Car getCar(String key) throws ApplicationException {
        if (key == null)
            throw new IncorrectInputData("Для поиска машины передан пустой ключ!");
        if (!Pattern.matches("[0-9]+_[0-9]+",key))
            throw new IncorrectInputData("Ключ поиска машины не соответсвует ожидаемой маске (help - для пподробной инфо)");
        if (!carsList.containsKey(key))
            throw new CarExistException("Машина с кодом " + key + " не найдена!");
        return carsList.get(key);
    }

    /**
     * Получить список транспортных стредств автопарка
     * @param type фильтр по типу (null - без фильтра)
     * @return список Car из carList
     */
    public static Collection<Car> getCars(String type){
        if (type != null)
            return carsList.entrySet().stream()
                    .filter(entry -> entry.getKey().contains(type + "_"))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());

        return carsList.values();
    }
}
