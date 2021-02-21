package com.rch.fuelcounter.ui;

public enum StoredPoint {
    driver("drivers.data", "Файл с данными водителей");


    private final String path;
    private final String description;

    StoredPoint(String path, String description) {
        this.path = path;
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public String getDescription() {
        return description;
    }

}
