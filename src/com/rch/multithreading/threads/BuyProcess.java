package com.rch.multithreading.threads;

import com.rch.multithreading.entity.Warehouse;

/**
 * <p>Покупательский процесс</p>
 * <p><b>volume</b> (static) - объем, приобретаемый покупателем за один раз</p>
 *
 */
public class BuyProcess extends BusinessProcess {
    public static final int volume = 5;

    public BuyProcess(String name, Warehouse warehouse){
        super(warehouse, name);
    }

    @Override
    public void action() {
        counterForStatusDemonstration += warehouse.getStuff(volume);
    }

    @Override
    public String getHeader(){
        return name + " - Куплено ";
    }
}
