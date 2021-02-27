package com.rch.fuelcounter.cars;

import com.rch.fuelcounter.exceptions.ApplicationException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class CarType implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String type;
    private final String name;
    private final float consumption;
    private final float fuelCost;
    private final String nameAdditional;

    public static Map<String, CarType> types = new HashMap<>();

    public CarType(String type, String name, float consumption, float fuelCost, String nameAdditional){
        this.type = type;
        this.name = name;
        this.consumption = consumption;
        this.fuelCost = fuelCost;
        this.nameAdditional = nameAdditional;
    }

    public static CarType getType(String type) throws ApplicationException {
        if (type == null)
            throw new ApplicationException("Пустой тип!");
        CarType carType = types.get(type);
        if (carType == null)
            throw new ApplicationException("Вид траспорта с кодом " + type + " отсутствует");

        return carType;

    }

    public String getType() {return type;}
    public String getName() {return name;}
    public String getNameAdditional() {return nameAdditional;}
    public Float getCostOnHundred(){
        return consumption * fuelCost;
    }
}
