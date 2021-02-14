package com.rch.fuelcounter.cars;

import java.util.HashMap;
import java.util.Map;


public class CarType {
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

    public String getType() {return type;}
    public String getName() {return name;}
    public String getNameAdditional() {return nameAdditional;}
    public Float getCostOnHundred(){
        return consumption * fuelCost;
    }
}
