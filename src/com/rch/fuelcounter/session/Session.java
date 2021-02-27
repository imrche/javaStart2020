package com.rch.fuelcounter.session;

import com.rch.fuelcounter.cars.Car;
import com.rch.fuelcounter.cars.CarPark;
import com.rch.fuelcounter.cars.ParsedSessionData;
import com.rch.fuelcounter.exceptions.*;
import com.rch.fuelcounter.util.Util;

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

    public static void closeSession() throws CloseSessionError {
        try (FileWriter fw =  new FileWriter(SessionDataManager.getSessionPath(instance.sessionName),true)) {
            for (String s : instance.sessionData)
                fw.append(s).append("\n");
            fw.close();
            instance = null;
        } catch (IOException e) {
            throw new CloseSessionError("Ошибка при сохранении данных смены!");
        }
    }

    public static void addData(String data) throws IncorrectInputData, CarExistException, AppointedDriverExistException, SessionError {
        if (!isSessionOpen())
            throw new SessionError("Смена не открыта!");

       ParsedSessionData parsedSessionData = Util.sessionDataParse(data);

        Car car = CarPark.getCar(parsedSessionData.type, parsedSessionData.licence);
        if ( car == null)
            throw new CarExistException("Указанная машина не зарегистрирована!");//todo недотестировано

        if (!car.hasDriver())
            throw new AppointedDriverExistException("Машина не назначена водителю!");

        instance.sessionData.add(data);
    }

    public static boolean isSessionOpen(){
        return instance != null;
    }
}
