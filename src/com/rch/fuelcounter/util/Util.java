package com.rch.fuelcounter.util;

public class Util {
    public static String[] parse(String str){
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

    public static boolean compare(Float f1, Float f2, boolean max){
        return (f1 >= f2 && max) || (f1 <= f2 && !max);
    }

    public static Integer nvl(Integer a, Integer b){
        return a != null ? a : b;
    }

    public static String splitArrayPart(String[] arr, int to, int from){
        StringBuilder result = new StringBuilder();
        for (int i = to; i <= from; i++)
            result.append(arr[i]).append(" ");

        return result.toString().trim();
    }
}
