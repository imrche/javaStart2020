package com.rch.fuelcounter.cars;

import com.rch.fuelcounter.util.Util;
import com.rch.fuelcounter.cars.CarPark;
import com.rch.fuelcounter.cars.CarType;
import com.rch.fuelcounter.cars.RegData;

public class Car {
    private final CarType carType;
    private final String licence;
    private Integer mileage;
    private Integer additional;

    public Car(CarType carType, String licence, Integer mileage, Integer additional){
        this.carType = carType;
        this.licence = licence;
        this.mileage = mileage;
        this.additional = additional;
    }

    public String getType(){
        return carType.getType();
    }
    public String getLicence() {
        return licence;
    }
    public Integer getMileage() {
        return mileage;
    }
    public Integer getAdditional() {
        return additional;
    }

    public Integer getSorterParam(String type){
        if(type == null || type.equals("mile"))
            return getMileage();
        else if (type.equals("add"))
            return getAdditional();
        else
            return null;
    }

    public void incrementMileage(Integer mileage){
        if (this.mileage == null)
            this.mileage = mileage;
        else
            this.mileage += Util.nvl(mileage,0);
    }

    public void incrementAdditional(Integer additional){
        if (this.additional == null)
            this.additional = additional;
        else
            this.additional += Util.nvl(additional,0);
    }

    public float getFullCost(){
        return carType.getCostOnHundred() * mileage / 100;
    }
}
