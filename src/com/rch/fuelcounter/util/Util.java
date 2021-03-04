package com.rch.fuelcounter.util;

import com.sun.istack.internal.Nullable;

import java.util.Arrays;

/**
 * Утильная библиотека
 */
public class Util {
    /**
     * Расширение строки до указанного размера пробелами
     * @param str строка для обработки
     * @param length длина итоговой строки (>0 - справа, <0 - слева
     *               )
     * @return строку длиной == length
     */
    public static String toLength(@Nullable String str, int length){
        boolean leftPlacement = length < 0;
        length = Math.abs(length);
        if (str != null) {
            if (str.length() == length)
                return str;
            if (length < str.length())
                return str.substring(0, length);
        }

        char[] spaces = new char[100];
        Arrays.fill(spaces, (char) 0x20);
        if (str == null)
            return String.valueOf(spaces).substring(0,length);

        String spacesString = String.valueOf(spaces).substring(0, length - str.length());
        if (leftPlacement)
            return spacesString + str;
        else
            return str + spacesString;
    }


    /**
     * Округление до 2х знаков после запятой
     * @param f число
     * @return строка в формате
     */
    public static String round(Float f){
        return String.format("%.2f",f);
    }
}
