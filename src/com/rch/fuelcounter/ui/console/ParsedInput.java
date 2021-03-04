package com.rch.fuelcounter.ui.console;

import com.rch.fuelcounter.exceptions.NullCommandValueException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс для разобранной команды из консоли
 * <p><b>command</b> @String - управляющая команда</p>
 * <p><b>value</b> @String - значение команды</p>
 * <p><b>keys</b> @Map - список ключей</p>
 */
public class ParsedInput {

    private String command;
    private String value;
    private final Map<String,String> keys = new HashMap<>();

    public ParsedInput(String commandLine){
        //шаблон поиска "-ключ значение"
        Pattern commandKeyPattern = Pattern.compile("-[a-z]+\\s+[a-z0-9_./]+");
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
        this.command = command;

        int firstSpace = command.indexOf(" ");

        if (firstSpace > 0) {
            this.command = this.command.substring(0, firstSpace);
            this.value = command.substring(firstSpace).trim();
        }
    }

    /**
     * Получить имя управляющей команды
     * @return имя управляющей команды
     */
    public String getCommand() {
        return command;
    }

    /**
     * Получение значения команды
     * @param def значение по-умолчанию
     * @return значение команды (если пусто, то переданное значение)
     */
    public String getValue(String def) {
        return hasValue() ? getValue() : def;
    }

    public String getValue() {
        return value;
    }

    /**
     * Безопастное получение значение команды
     * @return значение команды
     * @throws NullCommandValueException если значение пустое
     */
    public String safeGetValue() throws NullCommandValueException {
        if (!hasValue())
            throw new NullCommandValueException("Для " + this.command + " не получено значения");
        return value;
    }

    public boolean hasValue(){
        return value != null;
    }

    /**
     * Получить значение ключа
     * @param key имя ключа
     * @param def значение по умолчанию, если для ключа не задано значение
     * @return значение ключа (если пусто, то переданное в @def значение)
     */
    public String getKeyValue(String key, String def){
        return keys.getOrDefault(key, def);
    }

    public String getKeyValue(String key){
        return keys.getOrDefault(key, null);
    }
}
