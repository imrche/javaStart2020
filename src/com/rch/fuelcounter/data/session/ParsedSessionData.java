package com.rch.fuelcounter.data.session;

import com.rch.fuelcounter.data.cars.CarType;
import com.rch.fuelcounter.exceptions.IncorrectInputData;

/**
 * Класс для парсинга хранимых данных смен
 * <p><b>type</b> @String - тип ТС</p>
 * <p><b>licence</b> @String - номер ТС</p>
 * <p><b>mileage</b> @Integer - километраж</p>
 * <p><b>additional</b> @Integer - значение доп. параметра</p>
 */
public class ParsedSessionData {
    public String type;
    public String license;
    public Integer mileage;
    public Integer additional;

    public ParsedSessionData(String str) throws IncorrectInputData {
        if (str == null)
            throw new IncorrectInputData("Отсутствуюют данные!");

        if (!str.startsWith("C"))
            throw new IncorrectInputData("Несоответствие строки формату");

        String[] arr = str.substring(1).split("[_\\-]");

        if (arr.length < 3)
            throw new IncorrectInputData("Отсутствуют обязательные данные");

        String type = arr[0];
        String license = arr[1];
        String mileage = arr[2];
        String additional = arr.length > 3 ? arr[3] : null;

        if (!CarType.isExists(type))
            throw new IncorrectInputData("Неизвестный тип авто - " + type);

        this.type = type;
        this.license = license;

        try {
            this.mileage = mileage != null ? Integer.parseInt(mileage) : null;
            this.additional = additional != null ? Integer.parseInt(additional) : null;
        } catch (NumberFormatException e){
            throw new IncorrectInputData("Ошибка конвертации числовых значений");
        }
    }
}
