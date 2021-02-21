package com.rch.fuelcounter.session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SessionDataManager {

    private static final String sessionsStorePath = Session.class.getClassLoader().getResource("").getPath() + "sessions/";
    private static final String sessionFileExtension = ".txt";

    public static String getTodaySessionName() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public static String getSessionPath(){
        return getSessionPath(getTodaySessionName());
    }

    public static String getSessionPath(String session){
        return sessionsStorePath + session + sessionFileExtension;
    }

    public static boolean isTodaySessionExists(){
        return new File(getSessionPath()).exists();
    }

    public static List<String> getSessionData(String onDate){
        return getSessionData(onDate, null);
    }

    private static boolean isAccording(String fileName, Integer start, Integer end){
        if (!Pattern.compile("[0-9]{8}\\" + SessionDataManager.sessionFileExtension)
                    .matcher(fileName)
                    .matches())
            return false;

        Integer fileNumber = Integer.parseInt(fileName.replace(SessionDataManager.sessionFileExtension,""));

        if (end == null)
            return fileNumber.equals(start);
        else
            return fileNumber >= start && fileNumber <= end;
    }

    public static List<String> getSessionData(String startDate, String endDate) {
        File dir = new File(sessionsStorePath);
        List<String> list = new ArrayList<>();
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (isAccording(file.getName(), Integer.parseInt(startDate), endDate != null ? Integer.parseInt(endDate) : null))//todo сделать по красоте
                try {
                    System.out.println(file.getName() + "->");
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    list.addAll(reader.lines().collect(Collectors.toList()));
                } catch (Exception e) {
                    e.printStackTrace();//todo описать
                }
        }
        return list;
    }

}
