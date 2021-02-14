package com.rch.fuelcounter.cars;

public class RegData {
    public String type;
    public String licence;
    public Integer mileage;
    public Integer additional;

    public RegData(String type, String licence, String mileage, String additional) {
        this.type = type;
        this.licence = licence;
        this.mileage = mileage != null ? Integer.parseInt(mileage) : null;
        this.additional = additional != null ? Integer.parseInt(additional) : null;
    }

    public RegData(String[] str){
        this(str[0],str[1],str[2],str[3]);
    }
}
