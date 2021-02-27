package com.rch.fuelcounter.cars;

import com.rch.fuelcounter.exceptions.IncorrectInputData;

public class ParsedSessionData {
    public String type;
    public String licence;
    public Integer mileage;
    public Integer additional;

    public ParsedSessionData(String type, String licence, String mileage, String additional) throws IncorrectInputData {
        if (!CarType.types.containsKey(type))
            throw new IncorrectInputData("Неизвестный тип авто - " + type);

        this.type = type;
        this.licence = licence;
        try {
            this.mileage = mileage != null ? Integer.parseInt(mileage) : null;
            this.additional = additional != null ? Integer.parseInt(additional) : null;
        } catch (NumberFormatException e){
            throw new IncorrectInputData("Ошибка конвертации числовых значений");
        }
    }

    public ParsedSessionData(String[] str) throws IncorrectInputData {
        this(str[0],str[1],str[2],str[3]);
    }

    public ParsedSessionData(String str) throws IncorrectInputData {
        this(parse(str));
    }

    public static String[] parse(String str) throws IncorrectInputData {
        if (str == null)
            throw new IncorrectInputData("Отсутствуюют данные!");

        String[] result = new String[4];
        StringBuilder builder = new StringBuilder();
        int i = 0;

        for (Character c : str.toCharArray()) {
            if (c == 'C') {continue;}
            if (c == '-' || c == '_'){
                result[i++] = builder.toString();
                builder = new StringBuilder();
                continue;
            }
            builder.append(c);
        }

        result[i]= builder.toString();

        return result;
    }
}
