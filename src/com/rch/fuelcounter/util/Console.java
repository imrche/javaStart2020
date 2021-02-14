package com.rch.fuelcounter.util;

import com.rch.fuelcounter.cars.CarPark;

import java.util.Scanner;

public class Console {
    public static void run(CarPark park){
        Scanner s = new Scanner(System.in);
        while (true){
            String[] command = s.nextLine().split(" ");
            if (!command[0].equals("exit")) {
                switch (command[0]) {
                    case ("showfullcost"):
                        park.showFullCost(command.length > 1 ? command[1] : null);
                        break;
                    case ("showstat"):
                        park.showStat(command.length > 1 ? command[1] : null, command.length > 2 ? command[2] : null);
                        break;
                    case ("showextr"):
                        if (command.length > 1)
                            park.showExtremumCost(command[1].equals("max"));
                        else
                            System.out.println("Команда должна иметь параметр! (Команда help для помощи)");
                        break;
                    case ("help"):
                        System.out.println("Приложение для учета ГСМ!");
                        System.out.println("showfullcost [type] - вывести общие затраты по всем видам транспорта [по указанному выду транспорта(null- по всем)]");
                        System.out.println("showextr [max/min]- вывести максимальные/минимальные затраты на вид транспорта");
                        System.out.println("showstat [field] [type] - вывести общую статистику [mile(default)/add] [asc(default)/desc]");
                        break;
                    default:
                        System.out.println("Команда не разобрана! (Команда help для помощи)");
                }
            }
            else
                break;
        }
    }
}
