package com.rch.fuelcounter.util;

import java.util.LinkedList;

public class MyLinkedList<E> extends LinkedList<E> {
    public void addForSort(E e, String type){
        if (e == null)
            return;
        type = type != null ? type : "asc";
        switch (type){
            case ("asc"):
                addFirst(e);
                break;
            case("desc"):
                addLast(e);
                break;
            default:
                add(e);
        }
    }
}
