package com.rch.multithreading;

import com.rch.multithreading.entity.Warehouse;
import com.rch.multithreading.threads.BusinessProcess;
import com.rch.multithreading.threads.BuyProcess;
import com.rch.multithreading.threads.SupplyProcess;
import com.rch.multithreading.threads.Timer;
import com.rch.multithreading.utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int period = 20; //предполагаемое время работы процессов (количество тактов)
        int stockSize = 20; //первоначальный объем продукции на складе

        List<BusinessProcess> processes = new ArrayList<>();

        Warehouse warehouse = new Warehouse(stockSize);

        //создаем потоки по реализации продукции
        int buyersCount = Util.random(3,9);
        for (int i = 0; i < buyersCount; i++) {
            processes.add(new BuyProcess("buyer" + i, warehouse));
        }

        //создаем потоки для поставок продукции
        int suppliersCount = Util.getSufficientSuppliersCount(processes.size(), period, stockSize);
        for (int i = 0; i < suppliersCount; i++) {
            processes.add(new SupplyProcess("supplier" + i, warehouse));
        }

        //развернем список, так как в каждом такте сначала проводим поставку, а потом покупку
        Collections.reverse(processes);
        for (BusinessProcess bp: processes) {
            bp.setDaemon(true);
            bp.start();
        }

        //запускаем таймер работы
        Timer timer = new Timer(period, processes);
        timer.start();
    }
}
