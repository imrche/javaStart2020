package com.rch.fuelcounter.kpi.extrema;

import com.rch.fuelcounter.data.cars.CarType;
import com.rch.fuelcounter.exceptions.ApplicationException;
import com.rch.fuelcounter.exceptions.FindExtremaException;
import com.rch.fuelcounter.exceptions.IncorrectInputData;
import com.rch.fuelcounter.kpi.KPI;

import java.util.Map;

/**
 * Класс для определения экстремума по стоимости использования в разрезе типа
 * <p><b>extremaType</b> @ExtremaType - тип экстремума</p>
 * <p><b>value</b> @Float - значение экстремума</p>
 * <p><b>vehicleType</b> @CarType - ссылка на тип ТС, которому принадлежит данный экстремум</p>
 */
public class Extrema{
    public final ExtremaType extremaType;
    public final Float value;
    public final CarType carType;

    public Extrema(String typeExtrema, String startPeriod, String endPeriod) throws ApplicationException {
        Map.Entry<CarType, Float> resultEntry = null;

        try {
            extremaType = ExtremaType.valueOf(typeExtrema);
        } catch (IllegalArgumentException e){
            throw new IncorrectInputData("Некорректный вид экстремума!");
        }

        Map<CarType, Float> map = KPI.getTypeFullCost(null, startPeriod, endPeriod);
        switch (extremaType){
            case max : resultEntry = map.entrySet().stream().max(Map.Entry.comparingByValue()).orElse(null); break;
            case min : resultEntry = map.entrySet().stream().min(Map.Entry.comparingByValue()).orElse(null); break;
        }

        if (resultEntry == null)
            throw new FindExtremaException("Данных для расчета не найдено!");

        if (resultEntry.getKey() == null)
            throw new FindExtremaException("Экстремум не определен!");

        value = resultEntry.getValue();
        carType = resultEntry.getKey();
    }
}