package com.rch.fuelcounter.data.session;

import com.rch.fuelcounter.exceptions.ApplicationException;
import com.rch.fuelcounter.exceptions.IncorrectInputData;
import com.rch.fuelcounter.exceptions.LoadDataException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Класс для работы с данными смены
 * каждая смена сохраняется в resources/sessions в файл с именем формата yyyyMMdd.txt
 */
public class SessionDataManager {

    private static final String sessionsStorePath = Objects.requireNonNull(Session.class.getClassLoader().getResource("")).getPath() + "sessions/";
    private static final String sessionFileExtension = ".txt";

    /**
     * Собрать данные смен за период
     * @param startPeriod - начало периода сбора
     * @param endPeriod - окончание периода сбора
     * @return готовые к анализу данные смен
     */
    public static AnalysisData collectAnalysisData(String startPeriod, String endPeriod) throws ApplicationException {
        return new AnalysisData(toNameSessionFormat(startPeriod),toNameSessionFormat(endPeriod));
    }

    /**
     * Сбор сырых данных из файлов смен, соттветствующих запрошенному периоду
     * @param startDate - начало периода
     * @param endDate - окончание периода
     * @return список собранных данных
     */
    public static List<String> getSessionData(Integer startDate, Integer endDate) throws ApplicationException {

        File dir = new File(sessionsStorePath);
        List<String> list = new ArrayList<>();
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (isMatching(file.getName(), startDate, endDate)) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    list.addAll(reader.lines().collect(Collectors.toList()));
                } catch (FileNotFoundException e) {
                    throw new LoadDataException("Ошибка открытия файла с данными смены " + file.getName() + "!");
                }
            }

        }
        return list;
    }

    /**
     * Создание имени для файла смены текущего дня
     * @return String в формате yyyyMMdd
     */
    public static String getTodaySessionName() {
        return getSessionName(new Date());
    }

    /**
     * Создание имени файла для смены
     * @param date - день смены
     * @return String в формате yyyyMMdd
     */
    public static String getSessionName(Date date) {
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    /**
     * Создать абсолютный путь для файла текущей смены
     * @return путь к файлу смены
     */
    public static String getSessionPath(){
        return getSessionPath(getTodaySessionName());
    }

    /**
     * Создать абсолютный путь для файла заданной смены
     * @return путь к файлу смены
     */
    public static String getSessionPath(String session){
        return sessionsStorePath + session + sessionFileExtension;
    }

    /**
     * Проверка на существование файла текущей смены
     * @return boolean факт наличия файла смены
     */
    public static boolean isTodaySessionExists(){
        return new File(getSessionPath()).exists();
    }

    /**
     * Проверка имени файла соответствует формату и запрошенному периоду
     * @param fileName - имя проверяемого файла
     * @param start - начало интересующего периода
     * @param end - конец интересующего периода
     * @return факт соответствия
     */
    private static boolean isMatching(String fileName, Integer start, Integer end){
        if (!Pattern.matches("[0-9]{8}\\" + SessionDataManager.sessionFileExtension, fileName))
            return false;

        Integer fileNumber = Integer.parseInt(fileName.replace(SessionDataManager.sessionFileExtension,""));

        if (end == null)
            return start == null || fileNumber.equals(start);
        else
            return fileNumber >= start && fileNumber <= end;
    }

    /**
     * Привести формат дат запрашиваемого периода к формату, используемому в названии файлов
     * @param unformattedString - строка с ожидаемым форматом dd/MM/yy
     * @return число полученное из даты форматом yyyyMMdd
     */
    private static Integer toNameSessionFormat(String unformattedString) throws IncorrectInputData {
        if (unformattedString == null) return null;
        unformattedString = unformattedString.replaceAll("[:/;]",".");
        try {
            Date date = new SimpleDateFormat("dd.MM.yy").parse(unformattedString);
            return Integer.parseInt(getSessionName(date));
        } catch (ParseException e) {
            throw new IncorrectInputData("Ошибка при попытке разбора даты -> " + unformattedString);
        }
    }
}
