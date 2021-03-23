package com.rch.multithreading.utils;

import com.rch.multithreading.threads.BuyProcess;
import com.rch.multithreading.threads.SupplyProcess;


public class Util {
    /**
     * Генерация числа в заданном интервале
     * @param min начало интервала
     * @param max конец интервала
     * @return число принадлежащее [min,max]
     */
    public static int random(int min, int max){
        return (int)((Math.random()*(max-min+1))+min);
    }

    /**
     * Получение количества поставщиков, достаточного для покрытия потребностей покупателей
     * @param buyersCount количество покупателей
     * @param period период, в течение которого ожидаются покупки
     * @param stockSize запас на складе
     * @return количество поставщиков
     */
    public static int getSufficientSuppliersCount(int buyersCount, int period, int stockSize){
        float allBuyOnPeriod = BuyProcess.volume * buyersCount * period;
        float supplyInClock = (allBuyOnPeriod - stockSize) / period / SupplyProcess.volume;

        return (int) Math.ceil(supplyInClock);
    }
}
