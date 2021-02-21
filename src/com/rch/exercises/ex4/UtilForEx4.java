package com.rch.exercises.ex4;

import java.util.*;

public class UtilForEx4 {
    /**ЗАДАЧА 2
     * Необходимо реализовать приложение на вход которого поступает текст,
     * а на выходе выводится частотный словарь букв латинского(английского) алфавита.
     *
     */
    public static Map<Character,Integer> getCharDictionary(String phrase){
        Map<Character,Integer> map = new TreeMap<>();
        for(Character ch :
                phrase.replace(" ","")
                      .toLowerCase()
                      .replaceAll("[^a-z^а-я]","")
                      .toCharArray())
        {
            map.merge(ch,1, Integer::sum);
        }

        return map;
    }

    /**ЗАДАЧА 3
     * Напишите метод, который на вход получает коллекцию объектов,
     * а возвращает коллекцию уже без дубликатов.
     * <T> Collection<T> removeDuplicates(Collection<T> collection)
     *
     */
    public static <T> Collection<T> removeDuplicates(Collection<T> collection){
        //через стримы совсем хорошо
        //return collection.stream().distinct().collect(Collectors.toList());

        Collection<T> result = new HashSet<>();
        for(T obj : collection)
            result.add(obj);

        return result;
    }

    /**ЗАДАЧА 4
     * Напишите метод, который получает на вход Map<K, V> и возвращает Map,
     * где ключи и значения поменяны местами. Так как значения могут совпадать,
     * то тип значения в Map будет уже не K, а Collection<K>: Map<V, Collection<K>>
     *
     */
    public static <K,V> Map<V, Collection<K>> reverseMap(Map<K, V> map){
        Map<V, Collection<K>> result = new HashMap<>();

        for(Map.Entry<K,V> entry : map.entrySet()){
            if (!result.containsKey(entry.getValue()))
                result.put(entry.getValue(), new ArrayList<>());
            result.get(entry.getValue()).add(entry.getKey());
        }

        return result;
    }

    /**ЗАДАЧА 5
     * Необходимо написать функцию, которая на вход получает массив строк, в формате имя_игрока количество_очков.
     * Необходимо вывести победителя данной игры.
     * Победителем считается тот, кто раньше набрал максимальное количество очков.
     * Входные данные = "Ivan 5", "Petr 3", "Alex 10", "Petr 8", "Ivan 6", "Alex 5", "Ivan 1", "Petr 5", "Alex 1"
     *
     */
    public static String run5(String[] inputData){
        MyGame game = new MyGame();
        Arrays.stream(inputData).forEach(input -> game.makeMove(input.split(" ")));
        return game.getResult();
    }
}
