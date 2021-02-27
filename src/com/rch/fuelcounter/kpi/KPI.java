package com.rch.fuelcounter.kpi;

import com.rch.fuelcounter.cars.Car;
import com.rch.fuelcounter.cars.CarType;
import com.rch.fuelcounter.session.AnalysisData;
import com.rch.fuelcounter.session.SessionDataManager;
import com.rch.fuelcounter.util.Util;

import java.util.*;

import static com.rch.fuelcounter.cars.CarPark.getCars;

public class KPI {
    public static class Extrema{
        public final boolean isMaxima;
        public final Float value;
        public final CarType vehicleType;

        Extrema(boolean isMaxima, Float value, CarType vehicleType){
            this.isMaxima = isMaxima;
            this.value = value;
            this.vehicleType = vehicleType;
        }
    }

    public static Extrema getExtremaCost(String typeExtrema, String startPeriod, String endPeriod){
        Map.Entry<CarType, Float> resultEntry = null;
        boolean isMaxima = typeExtrema.equals("max");

        for (Map.Entry<CarType, Float> e : getTypeFullCost(null, startPeriod, endPeriod).entrySet()) {
            if (Util.compare(e.getValue(), resultEntry != null ? resultEntry.getValue() : e.getValue(), isMaxima))
                resultEntry = e;
        }

        return new Extrema(isMaxima, resultEntry.getValue(), resultEntry.getKey());
    }

    public static Map<CarType, Float> getTypeFullCost(String type, String startPeriod, String endPeriod){
        AnalysisData data = SessionDataManager.collectAnalysisData(startPeriod, endPeriod);

        Map<CarType, Float> agrResult = new HashMap<>();

        for (Car car : getCars(type))
            agrResult.put(car.getCarType(), agrResult.getOrDefault(car.getCarType(), 0F) + calcFullCostCar(car, data));

        return agrResult;
    }

    private static Float calcFullCostCar(Car car, AnalysisData data){
        return data.getCarMileage(car) * car.getCarType().getCostOnHundred() / 100
                + data.getCarMileage(car) * car.getDriver().getTariff();
    }


    public static CarTypeGroupedData getStatistic(String startPeriod, String endPeriod){
        AnalysisData data = SessionDataManager.collectAnalysisData(startPeriod,endPeriod);
        return new CarTypeGroupedData(data);

    }

}
