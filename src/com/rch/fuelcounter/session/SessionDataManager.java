package com.rch.fuelcounter.session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SessionDataManager {

    private static final String sessionStorePath = Session.class.getClassLoader().getResource("").getPath() + "sessions/";
    private static final String sessionFileExtension = ".txt";

    public static String getTodaySessionName() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public static String getSessionPath(){
        return getSessionPath(getTodaySessionName());
    }

    public static String getSessionPath(String session){
        return sessionStorePath + session + sessionFileExtension;
    }

    public static boolean isTodaySessionExists(){
        return new File(getSessionPath()).exists();
    }

    public static void getSessionData(String date) {
        File dir = new File(sessionStorePath);
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            try {
                System.out.println(file.getName() + "->");
                BufferedReader reader = new BufferedReader(new FileReader(file));

                List<String> list = reader.lines().collect(Collectors.toList());
                System.out.println(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
