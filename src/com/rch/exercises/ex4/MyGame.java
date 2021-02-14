package com.rch.exercises.ex4;

import java.util.HashMap;
import java.util.Map;

public class MyGame {
    private Map<String, Integer> players = new HashMap<>();

    private String leader = "unknown";
    private Integer maxScore = 0;

    public void makeMove(String[] arr){
        try {
            makeMove(arr[0], Integer.valueOf(arr[1]));
        } catch (NumberFormatException e) {
            System.out.printf("Ход \"%s\" имеет ошибочное значение очков и будет проигнорирован%n", String.join(" ",arr));
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.printf("В ходе \"%s\" не указано значение очков и он будет проигнорирован", String.join(" ",arr));
        }

    }

    public void makeMove(String name, Integer currentScore){
        players.merge(name, currentScore, Integer::sum);
        if (players.get(name) > maxScore){
            leader = name;
            maxScore = players.get(name);
        }
    }

    public String getResult(){
        return String.format("%s выиграл со счетом в %d очков!", leader, maxScore);
    }
}