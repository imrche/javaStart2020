package com.rch.fuelcounter.kpi.statistic;

import com.rch.fuelcounter.exceptions.IncorrectInputData;

import java.util.Comparator;

/**
 * Компаратор для сортировки данных по ТС по заданному полю и в заданном направлении
 * <p><b>field</b> @String - возможные значения mile/add</p>
 * <p><b>direction</b> @String - возможные значения asc/desc</p>
 */
public class CarDataComparator implements Comparator<CarData> {
    SortField field;
    SortDirection direction;

    public CarDataComparator(String field, String direction) throws IncorrectInputData {
        try {
            this.field = SortField.valueOf(field);
            this.direction = SortDirection.valueOf(direction);
        } catch (IllegalArgumentException e){
            throw new IncorrectInputData("Введены некорректные данны для компаратора!");
        }
    }

    @Override
    public int compare(CarData o1, CarData o2) {
        Integer first = null;
        Integer second = null;
        int nullValue = Integer.MAX_VALUE;

        switch (field){
            case mile:
                first = o1.mileage;
                second = o2.mileage;
                break;
            case add:
                first = o1.additional;
                second = o2.additional;
                break;
        }

        if (direction.equals(SortDirection.desc)){
            nullValue = 0;
            Integer tmp = second;
            second = first;
            first = tmp;
        }

        return (first != null ? first : nullValue) -
                (second != null ? second : nullValue);
    }
}
