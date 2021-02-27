package com.rch.fuelcounter.util;

import com.rch.fuelcounter.cars.ParsedSessionData;
import com.rch.fuelcounter.exceptions.IncorrectInputData;

public class Util {

    public static ParsedSessionData sessionDataParse(String sessionData) throws IncorrectInputData {//todo переместить
        try {
            return new ParsedSessionData(sessionData);
        } catch (IncorrectInputData e) {
            throw new IncorrectInputData("Некорректный формат строки " + sessionData + ": " + e.getMessage());
        }
    }

    public static boolean compare(Float f1, Float f2, boolean max){
        return (f1 >= f2 && max) || (f1 <= f2 && !max);
    }

}
