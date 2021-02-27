package com.rch.fuelcounter.kpi;

import com.rch.fuelcounter.cars.Car;
import com.rch.fuelcounter.cars.CarType;
import com.rch.fuelcounter.session.AnalysisData;

import java.util.*;

public class CarTypeGroupedData {
    public static class CarData {
        public final Car car;
        public final Integer mileage;
        public final Integer additional;

        CarData(Car car, Integer mileage, Integer additional){
            this.car = car;
            this.mileage = mileage;
            this.additional = additional;
        }
    }

    public static class CarDataComparator implements Comparator<CarData>{
        String field;
        String direction;

        public CarDataComparator(String field, String direction){
            this.field = field;
            this.direction = direction;
        }

        @Override
        public int compare(CarData o1, CarData o2) {
            Integer first = null;
            Integer second = null;
            int nullValue = Integer.MAX_VALUE;
            if (field.equals("mile")) {
                first = o1.mileage;
                second = o2.mileage;
            } else if (field.equals("add")){
                first = o1.additional;
                second = o2.additional;
            }

            if (direction.equals("desc")){
                nullValue = 0;
                Integer tmp = second;
                second = first;
                first = tmp;
            }

            return (first != null ? first : nullValue) -
                    (second != null ? second : nullValue);
        }
    }

    Map<CarType, List<CarData>> data = new HashMap<>();

    public CarTypeGroupedData(AnalysisData analysisData){
        for (Map.Entry<Car,AnalysisData.Data> e : analysisData.map.entrySet()){
            add(e.getKey(),e.getValue().mileage,e.getValue().additional);
        }
    }

    public void add(Car car, Integer mileage, Integer additional) {
        if (!data.containsKey(car.getCarType()))
            data.put(car.getCarType(), new ArrayList<>());
        data.get(car.getCarType()).add(new CarData(car, mileage, additional));
    }

    public Set<CarType> getTypeList(){
        return data.keySet();
    }

    public List<CarData> getDataList(CarType carType){
        return data.get(carType);
    }

}
