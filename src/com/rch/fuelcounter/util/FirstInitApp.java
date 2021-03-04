package com.rch.fuelcounter.util;

import com.rch.fuelcounter.data.StoredData;
import com.rch.fuelcounter.data.cars.CarType;
import com.rch.fuelcounter.exceptions.ConsoleCommandException;
import com.rch.fuelcounter.ui.console.Console;
import com.rch.fuelcounter.exceptions.SaveDataException;
import com.rch.fuelcounter.ui.console.ConsoleCmd;
import com.rch.fuelcounter.ui.console.ParsedInput;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

/**
 * Инициализация данных приложения при первом запуске
 */
public class FirstInitApp {
    /**
     * Заполнение типов ТС по-умолчанию
     * Установка паролей для пользовательских профилей
     * Сохранение в файл приложения
     * @throws SaveDataException при ошибках записи в файл
     */
    public static void run(String mess) throws SaveDataException {
        Console.showInitAppHeader(mess);
        fillDefaultCarType();
        Console.setCredential();
        StoredData.saveStoredData();
    }

    private static void fillDefaultCarType(){
        new CarType("100","Легковой авто",          12.5F,  46.10F, "");
        new CarType("200","Грузовой авто",          12F,    48.90F, "Объем перевезенного груза(м3)");
        new CarType("300","Пассажирский транспорт", 11.5F,  47.50F, "Число перевезенных пассажиров");
        new CarType("400","Тяжелая техника(краны)", 20F,    48.90F, "Вес поднятых грузов(т)");
    }

    /**
     * Заполенение приложения тестовыми данными из файла testData.txt
     * @throws IOException при ошибках работы с testData.txt
     * @throws ConsoleCommandException при выполнении консольных команд из файла
     */
    public static void fillAppWithTestData() throws IOException, ConsoleCommandException {
        String filePath = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("")).getPath() + "testData.txt";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

        String ln;
        while ((ln = bufferedReader.readLine()) != null){
            ParsedInput parsedInput = new ParsedInput(ln);
            ConsoleCmd.valueOf(parsedInput.getCommand().toLowerCase()).run(parsedInput);
        }
        try {
            StoredData.saveStoredData();
        } catch (SaveDataException e) {
            System.out.println(e.getMessage());
        }
    }
}
