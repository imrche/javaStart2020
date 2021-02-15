package com.rch.fuelcounter.ui;

import com.rch.fuelcounter.cars.CarPark;

import java.util.Scanner;

public class Console {
    public static void run(){
        Scanner s = new Scanner(System.in);
        System.out.println("****************************************************************");
        System.out.println("****************************************************************");
        System.out.println("**  ______          _    _____                  _             **");
        System.out.println("** |  ____|        | |  / ____|                | |            **");
        System.out.println("** | |__ _   _  ___| | | |     ___  _   _ _ __ | |_ ___ _ __  **");
        System.out.println("** |  __| | | |/ _ \\ | | |    / _ \\| | | | '_ \\| __/ _ \\ '__| **");
        System.out.println("** | |  | |_| |  __/ | | |___| (_) | |_| | | | | ||  __/ |    **");
        System.out.println("** |_|   \\__,_|\\___|_|  \\_____\\___/ \\__,_|_| |_|\\__\\___|_|    **");
        System.out.println("****************************************************************");
        System.out.println("**       ©richy Inc special 4 javaStart2020. (lolkekCHEburek) **");
        System.out.println("****************************************************************");

        String userLogin = null;
        UserType ut = null;

        while (true){
            System.out.println("");
            System.out.print("login as: ");
            userLogin = s.nextLine();
            try {
                ut = UserType.valueOf(userLogin);
                break;
            } catch (IllegalArgumentException e){
                System.out.println("Некорректное имя!");
            }
        }

        while (true){
            System.out.print("password: ");
            String userPassword = s.nextLine();
            if (ut.checkCredential(userPassword)) break;
            System.out.println("Неправильный пароль!");
        }

        while(true){
            System.out.print(userLogin + "> ");
            String[] command = s.nextLine().split(" ");
            if (!command[0].equals("exit")) {
                try {
                    UserCommand userCommand = UserCommand.valueOf(command[0]);
                    if (userCommand.hasRights(ut))
                        userCommand.run(command);
                    else
                        System.out.println("Команда недоступна под данным пользователем");
                } catch (IllegalArgumentException e) {
                    System.out.println("Команда не разобрана! (Команда help для помощи)");
                }
            } else
                break;
        }
    }
}
