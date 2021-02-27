package com.rch.fuelcounter;

import com.rch.fuelcounter.cars.CarType;
import com.rch.fuelcounter.console.Console;

public class Main {
    public static void main(String[] args) {
        try {
            StoredClass.loadStoredData();
        } catch (LoadDataException e) {
            System.out.println(e.getText());
        }

        CarType.types.put("100", new CarType("100","Легковой авто",          12.5F,  46.10F, ""));
        CarType.types.put("200", new CarType("200","Грузовой авто",          12F,    48.90F, "Объем перевезенного груза(м3)"));
        CarType.types.put("300", new CarType("300","Пассажирский транспорт", 11.5F,  47.50F, "Число перевезенных пассажиров"));
        CarType.types.put("400", new CarType("400","Тяжелая техника(краны)", 20F,    48.90F, "Вес поднятых грузов(т)"));

        Console.run();

        try {
            StoredClass.saveStoredData();
        } catch (LoadDataException e) {
            System.out.println(e.getText());
        }
    }
}
//todo везде проставить @NotNull