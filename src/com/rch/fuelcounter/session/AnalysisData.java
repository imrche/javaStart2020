package com.rch.fuelcounter.session;

import com.rch.fuelcounter.cars.Car;
import com.rch.fuelcounter.cars.CarPark;
import com.rch.fuelcounter.cars.ParsedSessionData;
import com.rch.fuelcounter.exceptions.IncorrectInputData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AnalysisData {
    public static class Data {//скрыть
        public Integer mileage;
        public Integer additional = 0;

        Data (ParsedSessionData parsedSessionData){
            add(parsedSessionData);
        }
        //todo разгрести помойку с null значениями
        void add(ParsedSessionData parsedSessionData){
            if (parsedSessionData.mileage != null && mileage == null)
                mileage = 0;
            if (parsedSessionData.mileage != null)
                mileage += parsedSessionData.mileage;
            additional += parsedSessionData.additional != null ? parsedSessionData.additional : 0;
        }
    }

    private final List<String> rawData;

    public List<String> getRawData() {
        return rawData;
    }

    public Map<Car, Data> map = new HashMap<>();

    public AnalysisData(Integer startPeriod, Integer endPeriod){
        rawData = SessionDataManager.getSessionData(startPeriod, endPeriod);

        for (String s : rawData){
            ParsedSessionData parsedData;
            try {
                parsedData = new ParsedSessionData(s);
            } catch (IncorrectInputData incorrectInputData) {
                System.out.println("Строка "+ s + " имеет некорректный формат и будет проигнорирована");
                continue;
            }
            Car car = CarPark.getCar(parsedData.type, parsedData.licence);

            if (car != null) {
                if (map.containsKey(car))
                    map.get(car).add(parsedData);
                else
                    map.put(car, new Data(parsedData));
            }
        }
    }

    public Integer getCarMileage(Car car){
        return map.get(car).mileage;
    }

    public Integer getCarAdditional(Car car){
        return map.get(car).additional;
    }



}
