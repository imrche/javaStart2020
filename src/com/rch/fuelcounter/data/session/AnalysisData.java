package com.rch.fuelcounter.data.session;

import com.rch.fuelcounter.data.cars.Car;
import com.rch.fuelcounter.data.cars.CarPark;
import com.rch.fuelcounter.exceptions.ApplicationException;
import com.rch.fuelcounter.exceptions.IncorrectInputData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Данные смен собранные за период
 */
public class AnalysisData {
    /**
     * Класс для хранения данных по транспортному средству
     */
    public static class Data {
        public Integer mileage;
        public Integer additional;

        Data (ParsedSessionData parsedSessionData){
            add(parsedSessionData);
        }

        /**
         * Добавление данных из анализа
         * с возможностью хранения в параметре значения @null
         * @param parsedSessionData запись из файла смены
         */
        void add(ParsedSessionData parsedSessionData){
            if (parsedSessionData.mileage != null)
                mileage = parsedSessionData.mileage + (mileage == null ? 0 : mileage);
            if (parsedSessionData.additional != null)
                additional = parsedSessionData.additional + (additional == null ? 0 : additional);
        }
    }

    /**
     * Все данные из смен за период в разрезе ТС
     */
    public final Map<Car, Data> map = new HashMap<>();

    public AnalysisData(Integer startPeriod, Integer endPeriod) throws ApplicationException {
        //все строки из файлов смен входящих в период
        List<String> rawData = SessionDataManager.getSessionData(startPeriod, endPeriod);

        for (String s : rawData){
            ParsedSessionData parsedData;
            try {
                //разбор строки
                parsedData = new ParsedSessionData(s);
            } catch (IncorrectInputData incorrectInputData) {
                System.out.println("Строка "+ s + " имеет некорректный формат и будет проигнорирована");
                continue;
            }
            Car car = CarPark.getCar(parsedData.type, parsedData.license);

            //добавление в структуру
            if (car != null) {
                if (map.containsKey(car))
                    map.get(car).add(parsedData);
                else
                    map.put(car, new Data(parsedData));
            }
        }
    }

    public boolean contains(Car car){
        return map.containsKey(car);
    }

    public Integer getCarMileage(Car car) {
        return map.get(car).mileage;
    }

    public boolean hasData(){
        return map.size() > 0;
    }
}
