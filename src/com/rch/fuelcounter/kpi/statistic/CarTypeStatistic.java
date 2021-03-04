package com.rch.fuelcounter.kpi.statistic;

import com.rch.fuelcounter.data.cars.Car;
import com.rch.fuelcounter.data.cars.CarType;
import com.rch.fuelcounter.data.session.AnalysisData;
import com.rch.fuelcounter.data.session.SessionDataManager;
import com.rch.fuelcounter.exceptions.ApplicationException;

import java.util.*;

/**
 * Класс для сгруппированной по типу ТС полной статистической информации по автопарку за период
 */

public class CarTypeStatistic {

    //сгруппированные данные
    final Map<CarType, List<CarData>> data = new HashMap<>();

    public CarTypeStatistic(String startPeriod, String endPeriod) throws ApplicationException {
        this(SessionDataManager.collectAnalysisData(startPeriod,endPeriod));
    }

    public CarTypeStatistic(AnalysisData analysisData){
        for (Map.Entry<Car,AnalysisData.Data> e : analysisData.map.entrySet()){
            add(e.getKey(), e.getValue().mileage, e.getValue().additional);
        }
    }

    private void add(Car car, Integer mileage, Integer additional) {
        if (!data.containsKey(car.getCarType()))
            data.put(car.getCarType(), new ArrayList<>());
        data.get(car.getCarType()).add(new CarData(car, mileage, additional));
    }

    /**
     * Получить список типов ТС, имеющих собранную статистику
     * @return @Set список типов ТС
     */
    public Set<CarType> getTypeList(){
        return data.keySet();
    }

    /**
     * Получить данные статистики по заданному типу ТС
     * @param carType тип ТС
     * @return @List статистика по типу carType
     */
    public List<CarData> getDataList(CarType carType){
        return data.get(carType);
    }

}
