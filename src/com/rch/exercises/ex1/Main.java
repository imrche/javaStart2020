package com.rch.exercises.ex1;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        //------------------------<Задача 2>--------------------------
        MyArray array = new MyArray(new int[]{1,2,3,4,5});
        array.edit(4,41);
        array.add(31,4);
        array.remove(6);


        System.out.println("--------Результат задания 2--------");
        array.print();
        System.out.println("Максиммальное значение: "+ array.getExtremum("max"));
        System.out.println("Минимальное значение: "+ array.getExtremum("min"));
        System.out.println("Количество элементов в массиве " + array.count());
        System.out.println("Сортированный по убыванию: " + Arrays.toString(array.sort("desc")));


    }
}
