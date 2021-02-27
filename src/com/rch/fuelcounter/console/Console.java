package com.rch.fuelcounter.console;

import com.rch.fuelcounter.exceptions.ConsoleCommandException;
import com.rch.fuelcounter.ui.UserType;

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
            ParsedInput parsedInput = new ParsedInput(s.nextLine());

            try {
                ConsoleCmd consoleCmd = ConsoleCmd.valueOf(parsedInput.getCommand().toLowerCase());
                if (consoleCmd != ConsoleCmd.exit) {
                    if (consoleCmd.hasRights(ut))
                        try {
                            consoleCmd.run(parsedInput);
                        } catch (ConsoleCommandException e){
                            System.out.println(e.getMessage());
                        }
                    else
                        System.out.println("Команда недоступна под данным пользователем");
                } else {
                    consoleCmd.run(null);
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Команда не разобрана! (Команда help для помощи)");
            } catch (com.rch.fuelcounter.exceptions.ConsoleCommandException e) {
                e.printStackTrace();
            }

        }
    }
}
