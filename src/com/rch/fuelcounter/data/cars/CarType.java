package com.rch.fuelcounter.data.cars;

import com.rch.fuelcounter.exceptions.ApplicationException;
import com.rch.fuelcounter.exceptions.IncorrectInputData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс типа ТС
 * <p><b>types</b> @Map - список типов ТС (ключ - код типа, значение - ссылка на тип)</p>
 * <p><b>type</b> @String - код типа ТС</p>
 * <p><b>name</b> @String - название типа ТС</p>
 * <p><b>consumption</b> @float - расход топлива ТС на 100км</p>
 * <p><b>fuelCost</b> @float - стоимость литра топлива</p>
 * * <p><b>nameAdditional</b> @String - название дополнительного параметра</p>
 */
public class CarType implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final float consumption;
    private final float fuelCost;
    private final String nameAdditional;

    public static Map<String, CarType> types = new HashMap<>();

    public CarType(String type, String name, float consumption, float fuelCost, String nameAdditional){
        this.name = name;
        this.consumption = consumption;
        this.fuelCost = fuelCost;
        this.nameAdditional = nameAdditional;
        types.put(type, this);
    }

    public static CarType getTypeRef(String type) throws ApplicationException {
        if (type == null)
            throw new IncorrectInputData("Пустой тип!");
        CarType carType = types.get(type);
        if (carType == null)
            throw new IncorrectInputData("Вид траспорта с кодом " + type + " отсутствует");

        return carType;

    }

    public static boolean isExists(String type){
        return types.containsKey(type);
    }

    public Float getCostOnHundred(){
        return consumption * fuelCost;
    }

    public String getName() {return name;}
    public String getNameAdditional() {return nameAdditional;}
}