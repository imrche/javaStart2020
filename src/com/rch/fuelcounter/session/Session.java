package com.rch.fuelcounter.session;

import com.rch.fuelcounter.cars.CarPark;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Session {
    private static Session instance;

    ArrayList<String> sessionData;
    String sessionName;

    Session(){
        sessionData = new ArrayList<>();
        sessionName = SessionDataManager.getTodaySessionName();
    }

    public static void openSession(){
        instance = new Session();

        if (SessionDataManager.isTodaySessionExists())
            System.out.println("Смена сегодняшнего дня уже существует и будет открыта для дозаписи.");
    }

    public static void closeSession(){
        try {
            FileWriter fw =  new FileWriter(SessionDataManager.getSessionPath(instance.sessionName),true);
            for (String s : instance.sessionData)
                fw.append(s).append("\n");
            fw.close();
            instance = null;
            System.out.println("Смена закрыта");
        } catch (IOException e) {
            System.out.println("ОШИБКА при сохранении данных смены! Смена не закрыта!");
        }
    }

    public static void addData(String data){
        if (CarPark.checkFormat(data))
            instance.sessionData.add(data);
    }

    public static boolean isSessionOpen(){
        return instance != null;
    }

    //public static Map<String,>
}
