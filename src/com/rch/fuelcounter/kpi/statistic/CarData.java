package com.rch.fuelcounter.kpi.statistic;

import com.rch.fuelcounter.data.cars.Car;

/**
 * Данные по каждому ТС
 * <p><b>car</b> @Car - ссылка на ТС</p>
 * <p><b>mileage</b> @Integer - пробег ТС за период</p>
 * <p><b>additional</b> @Integer - значение доп. параметра ТС за период</p>
 */
public class CarData {
    public final Car car;
    public final Integer mileage;
    public final Integer additional;

    CarData(Car car, Integer mileage, Integer additional) {
        this.car = car;
        this.mileage = mileage;
        this.additional = additional;
    }
}
