package com.rch.fuelcounter.data.session;

import com.rch.fuelcounter.data.cars.Car;
import com.rch.fuelcounter.data.cars.CarPark;
import com.rch.fuelcounter.exceptions.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Класс для работы со сменами
 * <p><b>instance</b> @static @Session - инстанс текущей (открытой) смены</p>
 * <p><b>sessionData</b> @ArrayList - данные текущей (открытой) смены</p>
 * <p><b>sessionName</b> @ArrayList - имя текущей (открытой) смены (формат {YYYYMMDD})</p>
 */
public class Session {
    private static Session instance;
    final ArrayList<String> sessionData;
    final String sessionName;

    Session(){
        sessionData = new ArrayList<>();
        sessionName = SessionDataManager.getTodaySessionName();
    }

    /**
     * Открыть смену
     * Если смена на сегодня уже создана, то вновь внесенные данные будут дозаписаны
     */
    public static void openSession(){
        instance = new Session();

        if (SessionDataManager.isTodaySessionExists())
            System.out.println("Смена сегодняшнего дня уже существует и будет открыта для дозаписи.");
    }

    /**
     * Закрыть смену
     * @throws CloseSessionError при ошибках в работе с файлом смены
     */
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

    /**
     * Добавить данные в рамках смены
     * @param data строковое значение (формат С{ТИП_АВТО}_{НОМЕР_АВТО}-{ПРОБЕГ}-{ДОП_ЗНАЧЕНИЕ})
     * @throws IncorrectInputData некорректный формат в data
     * @throws CarExistException указанное авто не зарегистрировано
     * @throws AppointedDriverExistException указанное авто не назначено водителю
     * @throws SessionError смена не открыта
     */
    public static void addData(String data) throws IncorrectInputData, CarExistException, AppointedDriverExistException, SessionError {
        if (!isSessionOpen())
            throw new SessionError("Смена не открыта!");

        ParsedSessionData parsedSessionData  = new ParsedSessionData(data);

        Car car = CarPark.getCar(parsedSessionData.type, parsedSessionData.license);
        if ( car == null)
            throw new CarExistException("Указанная машина не зарегистрирована!");

        if (!car.hasDriver())
            throw new AppointedDriverExistException("Машина не назначена водителю!");

        instance.sessionData.add(data);
    }

    /**
     * Проверка на открытую смену
     * @return true - если смена открыта
     */
    public static boolean isSessionOpen(){
        return instance != null;
    }
}
