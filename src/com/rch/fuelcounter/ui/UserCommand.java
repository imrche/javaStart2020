package com.rch.fuelcounter.ui;

import java.util.Arrays;
import java.util.HashSet;

import com.rch.fuelcounter.cars.Car;
import com.rch.fuelcounter.cars.CarPark;
import com.rch.fuelcounter.drivers.Driver;
import com.rch.fuelcounter.session.Session;
import com.rch.fuelcounter.util.Util;

public enum UserCommand {
    showfullcost("Показать полные затраты", new UserType[]{UserType.admin}){
        //System.out.println("showfullcost [type]     - вывести общие затраты по всем видам транспорта [по указанному выду транспорта(null- по всем)]");
        @Override
        public void run(String[] command){
            CarPark.showFullCost(command.length > 1 ? command[1] : null);
        }
    },

    showstat("Показать статистику", new UserType[]{UserType.admin}){
         //System.out.println("showstat [field] [type] - вывести общую статистику [mile(default)/add] [asc(default)/desc]");
        @Override
        public void run(String[] command){
            CarPark.showStat(command.length > 1 ? command[1] : null, command.length > 2 ? command[2] : null);
        }
    },

    showextr("Показать min/max по затратам", new UserType[]{UserType.admin}){
        //System.out.println("showextr [max/min]      - вывести максимальные/минимальные затраты на вид транспорта");
        @Override
        public void run(String[] command){
            if (command.length > 1)
                CarPark.showExtremumCost(command[1].equals("max"));
            else
                System.out.println("Команда должна иметь параметр! (Команда help для помощи)");
        }
    },

    help("Помощь по командам", new UserType[]{UserType.admin, UserType.user}){
        @Override
        public void run(String[] command) {
            for (UserCommand com : UserCommand.values()){
                System.out.println(com + " - " + com.description);
            }
        }
    },

    opensession("Начать смену", new UserType[]{UserType.admin, UserType.user}){
        @Override
        public void run(String[] command) {
            Session.openSession();
            System.out.println("Смена открыта");
        }
    },

    closesession("Закончить смену", new UserType[]{UserType.admin, UserType.user}){
        @Override
        public void run(String[] command) {
            Session.closeSession();
        }//todo проверить перед выходом, что сессия сохранена
    },

    add("Добавить данные", new UserType[]{UserType.admin, UserType.user}){
        @Override
        public void run(String[] command) {
            if (Session.isSessionOpen())
                Session.addData(command.length > 1 ? command[1] : null);
            else
                System.out.println("Данные можно добавлять только в рамках открытой смены!");

        }
    },

    inputcar("Добавить машину", new UserType[]{UserType.admin}){
        @Override
        public void run(String[] command) {
            if (command.length < 3)
                System.out.println("Недостаточно параметров");
            CarPark.addCar(command[1],command[2]);
        }
    },

    showcars("Показать список машин", new UserType[]{UserType.admin}){
        @Override
        public void run(String[] command) {
            for(Car car : CarPark.getCars(command.length > 1 ? command[1] : null)){
                System.out.println(car.getIdentifier() + " - " + car.getLicence());
            }
        }
    },

    showdrivers("Показать список водителей", new UserType[]{UserType.admin}){
        @Override
        public void run(String[] command) {
            for ( Driver driver : Driver.getDriversList().values()){
                System.out.printf("%s (%s) - %s",driver.getName(), driver.getLogin(), driver.getAppointedDescription());
                System.out.println();
            }
        }
    },

    hiredriver("Нанять водителя", new UserType[]{UserType.admin}){
        @Override
        public void run(String[] command) {
            if (command.length < 3)
                System.out.println("Недостаточно параметров");
            else
                new Driver(command[1], Util.splitArrayPart(command, 2, command.length-1));//todo не везде выход где нужно
        }
    },

    appoint("Назначить водителя на машину", new UserType[]{UserType.admin}){
        @Override
        public void run(String[] command) {
            if (command.length < 2)
                System.out.println("Недостаточно параметров");
            //todo докинуть проверки
            String[] carData = command[2].split("_");
            Driver.getDriverByLogin(command[1]).appointToVehicle(CarPark.getCar(carData[0],carData[1]));//todo обработка NPE
            //todo очистить у старой машины водителя если он был
        }
    },

    removeappoint("Снять водителя с машины", new UserType[]{UserType.admin}){
        @Override
        public void run(String[] command) {
            if (command.length == 0)
                System.out.println("Недостаточно параметров");
            //todo докинуть проверки
            Driver.getDriverByLogin(command[1]).removeFromVehicle();
        }
    }
    ;


    private final HashSet<UserType> userList;
    private final String description;

    UserCommand(String description, UserType[] userType){
        this.userList = new HashSet<>();
        this.userList.addAll(Arrays.asList(userType));
        this.description = description;
    }

    public boolean hasRights(UserType userType){
        return userList.contains(userType);
    }

    public abstract void run(String[] command);
}
