package com.rch.exercises.ex4;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        //------------------------<Задача 2>--------------------------
        String data2 = "Какое-то невероятное предложение, I'm impressed!";

        Map<Character, Integer> result2 = UtilForEx4.getCharDictionary(data2);

        System.out.println("--------Результат задания 2--------");
        for(Character key : result2.keySet()){
            System.out.println(key + " - " + result2.get(key));
        }


        //------------------------<Задача 3>--------------------------
        Collection<String> data3 = new HashSet<>();
        data3.add("qwe");
        data3.add("asd");
        data3.add("zxc");
        data3.add("asd");
        data3.add("zxc");
        data3.add("qwe");

        Collection<String> collection1 = UtilForEx4.removeDuplicates(data3);

        System.out.println("--------Результат задания 3--------");
        for(String s : collection1)
            System.out.println(s);

        //------------------------<Задача 4>--------------------------
        Map<String, String> data4 = new HashMap<>();
        data4.put("123","qwe");
        data4.put("234","asd");
        data4.put("345","qwe");
        data4.put("456","zxc");
        data4.put("567","asd");

        Map<String, Collection<String>> result4 = UtilForEx4.reverseMap(data4);

        System.out.println("--------Результат задания 4--------");
        for(String key : result4.keySet()){
            System.out.println(key + " " + result4.get(key));
        }


        //------------------------<Задача 5>--------------------------
        String[] data5 = {"Ivan 5", "Petr 3", "Alex 10", "Petr 8", "Ivan 6", "Alex 5", "Ivan 1", "Petr 5", "Alex 1"};

        String result5 = UtilForEx4.run5(data5);

        System.out.println("--------Результат задания 5--------");
        System.out.println(result5);
    }
}