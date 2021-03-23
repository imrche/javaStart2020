package com.rch.multithreading.entity;

/**
 * <p>Склад для хранения и реализации продукции</p>
 * <p><b>volume</b> - текущий объем продукции на складе</p>
 */
public class Warehouse {
    public int volume;

    public Warehouse(int startVolume){
        this.volume = startVolume;
    }

    /**
     * Получить продукцию со склада для реализации
     * @param volume объем сделки
     * @return объем проведенной сделки "по факту"
     */
    public synchronized int getStuff(int volume) {
        if (volume <= this.volume) {
            this.volume -= volume;
            return volume;
        }
        return 0;
    }

    /**
     * Выгрузить на склад продукцию от поставщика
     * @param volume объем поставки
     */
    public synchronized void setStuff(int volume) {
        this.volume += volume;
    }
}
