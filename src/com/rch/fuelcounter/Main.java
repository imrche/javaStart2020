package com.rch.fuelcounter;

import com.rch.fuelcounter.data.StoredData;
import com.rch.fuelcounter.ui.console.Console;
import com.rch.fuelcounter.exceptions.LoadDataException;
import com.rch.fuelcounter.exceptions.SaveDataException;
import com.rch.fuelcounter.util.FirstInitApp;

public class Main {
    public static void main(String[] args) {
        try { StoredData.loadStoredData();
        } catch (LoadDataException e) {
            try { FirstInitApp.run(e.getMessage());
            } catch (SaveDataException e1) {
                System.out.println("Ошибка инициализации приложения: " + e1.getMessage());
                return;
            }
            System.out.println();
        }

        Console.run();

        try { StoredData.saveStoredData();
        } catch (SaveDataException e) {
            System.out.println(e.getMessage());
        }
    }
}