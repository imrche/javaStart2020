package com.rch.multithreading.threads;

import com.rch.multithreading.entity.Warehouse;

/**
 * <p>Процесс поставки продукции на склад</p>
 * <p><b>volume</b> - объем, приобретаемый покупателем за один раз</p>
 *
 */
public class SupplyProcess extends BusinessProcess{
    public static final int volume = 3;

    public SupplyProcess(String name, Warehouse warehouse){
        super(warehouse,name);
    }

    @Override
    public void action() {
        warehouse.setStuff(volume);
        counterForStatusDemonstration += volume;
    }

    @Override
    public String getHeader(){
        return name + " - Поставлено ";
    }
}
