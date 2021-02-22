package com.rch.fuelcounter;

import com.rch.fuelcounter.cars.CarPark;
import com.rch.fuelcounter.cars.CarType;
import com.rch.fuelcounter.session.AnalysisData;
import com.rch.fuelcounter.session.SessionDataManager;
import com.rch.fuelcounter.ui.Console;

import java.util.List;


public class Main {
    public static void main(String[] args) {

        try {
            StoredClass.loadStoredData();
        } catch (LoadDataException e) {
            System.out.println(e.getText());
        }

        System.out.println("--------");
        new AnalysisData(SessionDataManager.getTodaySessionName());
        //List<String> list = SessionDataManager.getSessionData(SessionDataManager.getTodaySessionName());
        //System.out.println(list);
        System.out.println("--------");

/*        String[] inArr = {  "C100_1-100",
                            "C200_1-120-1200",
                            "C300_1-120-30",
                            "C400_1-80-20",
                            "C100_2-50",
                            "C200_2-40-1000",
                            "C300_2-200-45",
                            "C400_2-10-20",
                            "C100_3-10",
                            "C200_3-170-1100",
                            "C300_3-150-29",
                            "C400_3-100-28",
                            "C100_1-300",
                            "C200_1-100-750",
                            "C300_1-32-15"
        };*/

        CarType.types.put("100", new CarType("100","Легковой авто",          12.5F,  46.10F, ""));
        CarType.types.put("200", new CarType("200","Грузовой авто",          12F,    48.90F, "Объем перевезенного груза(м3)"));
        CarType.types.put("300", new CarType("300","Пассажирский транспорт", 11.5F,  47.50F, "Число перевезенных пассажиров"));
        CarType.types.put("400", new CarType("400","Тяжелая техника(краны)", 20F,    48.90F, "Вес поднятых грузов(т)"));


/*        for (String s : inArr)
            CarPark.fabric(s);*/

        Console.run();

        try {
            StoredClass.saveStoredData();
        } catch (LoadDataException e) {
            System.out.println(e.getText());
        }
    }
}