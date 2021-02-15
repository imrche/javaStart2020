package com.rch.fuelcounter.session;

import java.util.ArrayList;
import java.util.Arrays;

public class Session {
    private static Session instance;

    private ArrayList<String> sessionData;

    private Session(){}

    public static void openSession(){
        instance = new Session();
        instance.sessionData = new ArrayList<>();
    }

    public static void closeSession(){
        System.out.println("Закрываем сессию");
        System.out.println("В файл пишем " + Arrays.toString(instance.sessionData.toArray()));
        instance=null;
    }

    public static void addData(String data){
        instance.sessionData.add(data);
    }

    public static boolean isSessionOpen(){
        return instance != null;
    }
}
