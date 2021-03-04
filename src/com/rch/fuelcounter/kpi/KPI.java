package com.rch.fuelcounter.kpi;

import com.rch.fuelcounter.data.cars.Car;
import com.rch.fuelcounter.data.cars.CarType;
import com.rch.fuelcounter.exceptions.ApplicationException;
import com.rch.fuelcounter.data.session.AnalysisData;
import com.rch.fuelcounter.data.session.SessionDataManager;
import com.rch.fuelcounter.exceptions.LoadDataException;

import java.util.*;

import static com.rch.fuelcounter.data.cars.CarPark.getCars;

/**
 * Класс для расчета статистических и пр. данных
 */
public class KPI {
    /**
     * Получение данных о полной стоимости использования ТС
     * @param type ограничение по типу ТС
     * @param startPeriod дата начала анализируемого периода
     * @param endPeriod дата окончания анализируемого периода
     * @return карта, где ключ - тип ТС, значение - стоимость
     * @throws ApplicationException при ошибках получения данных смен
     */
    public static Map<CarType, Float> getTypeFullCost(String type, String startPeriod, String endPeriod) throws ApplicationException {
        AnalysisData data = SessionDataManager.collectAnalysisData(startPeriod, endPeriod);

        if (!data.hasData()){
            throw new LoadDataException("За период не найдено данных для анализв!");
        }

        Map<CarType, Float> agrResult = new HashMap<>();

        for (Car car : getCars(type))
            agrResult.put(car.getCarType(), agrResult.getOrDefault(car.getCarType(), 0F) + calcFullCostCar(car, data));

        return agrResult;
    }

    /**
     * Расчет стоимости владения для переданного ТС
     * @param car ТС требующее расчета стоимости
     * @param data собранные данные за период
     * @return стоимость владения
     */
    private static Float calcFullCostCar(Car car, AnalysisData data){
        return data.contains(car) ?
                data.getCarMileage(car) * car.getCarType().getCostOnHundred() / 100 +
                data.getCarMileage(car) * car.getDriver().getTariff()
                : 0F;
    }
}
