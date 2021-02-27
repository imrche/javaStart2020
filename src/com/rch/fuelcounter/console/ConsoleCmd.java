package com.rch.fuelcounter.console;

import java.util.*;

import com.rch.fuelcounter.kpi.KPI;
import com.rch.fuelcounter.cars.Car;
import com.rch.fuelcounter.cars.CarPark;
import com.rch.fuelcounter.cars.CarType;
import com.rch.fuelcounter.drivers.Driver;
import com.rch.fuelcounter.exceptions.*;
import com.rch.fuelcounter.kpi.CarTypeGroupedData;
import com.rch.fuelcounter.session.Session;
import com.rch.fuelcounter.ui.UserType;

/**
 * Список комманд доступных из консоли
 */
public enum ConsoleCmd {
    /**
     * Просмотр общих затрат
     * - ограничение по типу авто
     * - выборка данных за дату/период
     */
    showfullcost(new UserType[]{UserType.admin}){
        @Override
        public void showDescription() {
            System.out.println(this.name().toUpperCase() + " - вывести общие затраты по видам транспорта");
            System.out.println("    -t ограничение по типу авто");
            System.out.println("    Задание периода анализа (dd/mm/yy) [если не указаны, анализируются все данные]:");
            System.out.println("        -s начало периода (если -e не указан, то берутся данные только за этот день)");
            System.out.println("        -e окончание периода");
            System.out.println("");
        }

        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            Map<CarType, Float> fullCostData = KPI.getTypeFullCost( command.getKeyValue("t"),
                                                                    command.getKeyValue("s"),
                                                                    command.getKeyValue("e"));
            for (Map.Entry<CarType, Float> e : fullCostData.entrySet())
                System.out.println(e.getKey().getName() + " " + e.getValue());

        }
    },

    /**
     * Просмотр общей статистики
     * - сортировка с указанием поля для сортировки и направления сортировки
     * - выборка данных за дату/период
     */
    showstat(new UserType[]{UserType.admin}){
         @Override
         public void showDescription() {
             System.out.println(this.name().toUpperCase() + " - вывести общую статистику");
             System.out.println("    -f поле сортировки [mile(default)/add]");
             System.out.println("    -t направление сортировки [asc(default)/desc]");
             System.out.println("    Задание периода анализа (dd/mm/yy) [если не указаны, анализируются все данные]:");
             System.out.println("        -s начало периода (если -e не указан, то берутся данные только за этот день)");
             System.out.println("        -e окончание периода");
             System.out.println("");
         }
        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {

             CarTypeGroupedData statistic =  KPI.getStatistic(command.getKeyValue("s"), command.getKeyValue("e"));

             if (statistic.getTypeList().isEmpty())
                 throw new ConsoleCommandException("Не найдено данных для отображения!");

             for (CarType carType : statistic.getTypeList()){
                 System.out.println("------ " + carType.getName() + " ------");
                 System.out.println("Номер    Пробег      " + carType.getNameAdditional());
                 List<CarTypeGroupedData.CarData> list = statistic.getDataList(carType);
                 list.sort(new CarTypeGroupedData.CarDataComparator(command.getKeyValue("f","mile"),command.getKeyValue("t","asc")));

                 for (CarTypeGroupedData.CarData carData : list){
                     System.out.printf("%s        %s         %s%n", carData.car.getLicence(), carData.mileage == null ? "" : carData.mileage, carData.additional);
                 }
             }
        }
    },

    /**
     * Просмотр максимальных и минимальных затрат в разрезе типа транспорта
     * - выборка данных за дату/период
     */
    showextr(new UserType[]{UserType.admin}){
        @Override
        public void showDescription() {
            System.out.println(this.name().toUpperCase() + " [max(default)/min]  - вывести максимальные/минимальные затраты на тип транспорта");
            System.out.println("    Задание периода анализа (формат dd/mm/yy):");
            System.out.println("        -s начало периода (если -e не задан, то берутся данные только за этот день)");
            System.out.println("        -e окончание периода");
            System.out.println("");
        }

        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            KPI.Extrema extrema = KPI.getExtremaCost(   command.getValue("max"),
                                                        command.getKeyValue("s"),
                                                        command.getKeyValue("e"));
            if (extrema.value == null)
                throw new ConsoleCommandException("Не удалось определить запрошенный экстремум");

            System.out.println((extrema.isMaxima ? "Наибольшая" : "Наименьшая") +  " стоимость расхода у \"" + extrema.vehicleType.getName() + "\" -> " + extrema.value);
        }
    },

    /**
     * Просмотр описания доступных комманд
     */
    help(new UserType[]{UserType.admin, UserType.user}){
        @Override
        public void showDescription() {
        }
        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            for (ConsoleCmd com : ConsoleCmd.values()){
                com.showDescription();
            }
        }
    },

    /**
     * Начать смену
     */
    opensession( new UserType[]{UserType.admin, UserType.user}){
        @Override
        public void showDescription() {
            System.out.println(this.name().toUpperCase() + " - начать смену");
            System.out.println("");
        }

        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            Session.openSession();
            System.out.println("Смена открыта");
        }
    },

    /**
     * Закончить смену
     */
    closesession(new UserType[]{UserType.admin, UserType.user}){
        @Override
        public void showDescription() {
            System.out.println(this.name().toUpperCase() + " - закончить смену");
            System.out.println("");
        }

        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            if (Session.isSessionOpen())
                try {
                    Session.closeSession();
                    System.out.println("Смена закрыта");
                } catch (ApplicationException e) {
                    throw new ConsoleCommandException(e.getMessage());
                }
        }
    },

    /**
     * Добавить данные в открытую смену
     */
    add(new UserType[]{UserType.admin, UserType.user}){
        @Override
        public void showDescription() {
            System.out.println(this.name().toUpperCase() + " [данные] - добавить данные в смену (формат С{тип авто}_{номер авто}-{пробег}-{доп.параметр})");
            System.out.println("");
        }
        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            try {
                Session.addData(command.getValue());
            } catch (ApplicationException e) {
                throw new ConsoleCommandException(e.getMessage());
            }
        }
    },

    /**
     * Добавить машину в парк
     */
    addcar(new UserType[]{UserType.admin}){
        @Override
        public void showDescription() {
            System.out.println(this.name().toUpperCase() + " [уникальный номер] - добавить машину в парк");
            System.out.println("        -t тип авто [ОБЯЗАТЕЛЬНО]");
            System.out.println("");
        }
        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            try {
                CarPark.addCar(command.getKeyValue("t"), command.getValue());
            } catch (ApplicationException e) {//todo типизация исключения
                throw new ConsoleCommandException(e.getMessage());
            }
        }
    },

    /**
     * Вывести список машин в парке
     * - с ограничением по типу
     */
    showcars(new UserType[]{UserType.admin}){
        @Override
        public void showDescription() {
            System.out.println(this.name().toUpperCase() + " - вывести список машин");
            System.out.println("        -t ограничение по типу");
            System.out.println("");
        }
        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            for(Car car : CarPark.getCars(command.getKeyValue("t"))){
                System.out.println(car.getIdentifier() + " - " + car.getLicence());
            }
        }
    },

    /**
     * Вывести список нанятых водителей
     */
    showdrivers(new UserType[]{UserType.admin}){
        @Override
        public void showDescription() {
            System.out.println(this.name().toUpperCase() + " - вывести список водителей");
        }
        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            for ( Driver driver : Driver.getDriversList().values()){
                System.out.printf("%s (%s) - %s",driver.getName(), driver.getLogin(), driver.getAppointedDescription());
                System.out.println("");
            }
        }
    },

    /**
     * Нанять водителя
     */
    hiredriver(new UserType[]{UserType.admin}){
        @Override
        public void showDescription() {
            System.out.println(this.name().toUpperCase() + " [ФИО] - ннанять водителя");
            System.out.println("        -l логин");
            System.out.println("");
        }
        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            try {
                new Driver(command.getKeyValue("l"), command.getValue());
            } catch (ApplicationException e) {
                throw new ConsoleCommandException(e.getMessage());
            }
        }
    },

    /**
     * Назначить водителю машину
     */
    appoint(new UserType[]{UserType.admin}){
        @Override
        public void showDescription() {
            System.out.println(this.name().toUpperCase() + " [логин] - назначить водителю машину");
            System.out.println("        -v код авто в формате ТИП_НОМЕР");
            System.out.println("");
        }
        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            try {
                Driver.getDriverByLogin(command.getValue(true)).appointToVehicle(CarPark.getCar(command.getKeyValue("v")));
            } catch (ApplicationException e) {
                throw new ConsoleCommandException(e.getMessage());
            }
        }
    },

    /**
     * Снять водителя с машины
     */
    removeappoint( new UserType[]{UserType.admin}){
        @Override
        public void showDescription() {
            System.out.println(this.name().toUpperCase() + " [логин] - снять водителя с машины");
            System.out.println("");
        }
        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            try {
                Driver.getDriverByLogin(command.getValue(true)).removeFromVehicle();
            } catch (NullCommandValueException e) {
                throw new ConsoleCommandException("Не указано сетевое имя водителя!");
            } catch (ApplicationException e){
                throw new ConsoleCommandException(e.getMessage());
            }
        }
    },

    exit(new UserType[]{}){
        @Override
        public void showDescription() {
            System.out.println(this.name().toUpperCase() + " завершить работу приложения");
            System.out.println("");
        }

        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            ConsoleCmd.closesession.run(null);
        }
    }
    ;

    private final HashSet<UserType> userList;

    ConsoleCmd(UserType[] userType){
        this.userList = new HashSet<>();
        this.userList.addAll(Arrays.asList(userType));
    }

    public boolean hasRights(UserType userType){
        return userList.contains(userType);
    }

    public abstract void run(ParsedInput command) throws ConsoleCommandException;
    public abstract void showDescription();


}
