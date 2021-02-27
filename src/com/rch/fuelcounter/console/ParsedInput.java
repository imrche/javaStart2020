package com.rch.fuelcounter.console;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс для разобранной команды из консоли
 */
public class ParsedInput {
    //шаблон поиска "-ключ значение"
    private final Pattern commandKeyPattern = Pattern.compile("-[a-z]+\\s+[a-z0-9]+");

    //Управляющая команда
    private String command;

    //значение команды
    private String value;

    //список ключей команды
    private Map<String,String> keys = new HashMap<>();

    public ParsedInput(String commandLine){
        Matcher commandKeyMatcher = commandKeyPattern.matcher(commandLine);
        String command = commandLine;

        //выбираем все найденные ключи в map и удаляем их из финальной строки
        while (commandKeyMatcher.find()) {
            String commandKey = commandLine.substring(commandKeyMatcher.start(), commandKeyMatcher.end());
            addKey(commandKey);
            command = command.replace(commandKey,"");
        }
        //сохраняем оставшуюся строку как управляющую команду и ее значение
        this.addCommand(removeDuplicatedSpaces(command));
    }

    /**
     * Удаление дубликатов символа ПРОБЕЛ
     * @param str - строка для очистки от дубликатов
     * @return строка без дублирующихся пробелов
     */
    private String removeDuplicatedSpaces(String str){
        return str.replaceAll("\\s+"," ");
    }

    /**
     * Добавление найденного ключа в map
     * @param key - неразобраная строка с ключом и его значением
     */
    private void addKey(String key){
        String[] array = removeDuplicatedSpaces(key).replace("-","").split(" ");
        keys.put(array[0],array.length > 1 ? array[1] : null);
    }

    /**
     * Добавление команды и значения
     * @param command - управляющая команда со значением
     */
    private void addCommand(String command){
        String[] array = command.split(" ");
        this.command = array[0];
        this.value = array.length > 1 ? array[1] : null;
    }

    public String getCommand() {
        return command;
    }

    public String getValue(String def) {
        return hasValue() ? getValue() : def;
    }

    public String getValue() {
        return value;
    }

    public boolean hasValue(){
        return value != null;
    }

    public boolean hasKeys(){
        return keys.size() > 0;
    }

    public Collection<String> getKeys(){
        return keys.keySet();
    }

    public String getKeyValue(String key, String def){
        return keys.getOrDefault(key, def);
    }

    public String getKeyValue(String key){
        return keys.getOrDefault(key, null);
    }
}
