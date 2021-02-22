package com.rch.fuelcounter.session;

import com.rch.fuelcounter.cars.Car;
import com.rch.fuelcounter.cars.CarPark;
import com.rch.fuelcounter.cars.RegData;
import com.rch.fuelcounter.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AnalysisData {
    class Data {
        Integer mileage = 0;
        Integer additional = 0;

        Data (RegData regData){
            add(regData);
        }
        void add(RegData regData){
            mileage += regData.mileage != null ? regData.mileage : 0;
            additional += regData.additional != null ? regData.additional : 0;
        }
    }

    private final List<String> rawData;

    public List<String> getRawData() {
        return rawData;
    }

    public Map<Car, Data> map = new HashMap<>();

    public AnalysisData(String day){
        this(day,null);
    }
    public AnalysisData(String startPeriod, String endPeriod){
        rawData = SessionDataManager.getSessionData(startPeriod,endPeriod);

        for (String s : rawData){
            RegData parsedData = new RegData(Util.parse(s));
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
