package com.rch.fuelcounter.ui.console;

import java.io.IOException;
import java.util.*;

import com.rch.fuelcounter.data.StoredData;
import com.rch.fuelcounter.kpi.KPI;
import com.rch.fuelcounter.data.cars.Car;
import com.rch.fuelcounter.data.cars.CarPark;
import com.rch.fuelcounter.data.cars.CarType;
import com.rch.fuelcounter.data.drivers.Driver;
import com.rch.fuelcounter.exceptions.*;
import com.rch.fuelcounter.kpi.statistic.CarData;
import com.rch.fuelcounter.kpi.statistic.CarDataComparator;
import com.rch.fuelcounter.kpi.statistic.CarTypeStatistic;
import com.rch.fuelcounter.data.session.Session;
import com.rch.fuelcounter.kpi.extrema.Extrema;
import com.rch.fuelcounter.kpi.extrema.ExtremaType;
import com.rch.fuelcounter.ui.UserType;
import com.rch.fuelcounter.util.FirstInitApp;
import com.rch.fuelcounter.util.Util;

/**
 * Список комманд доступных из консоли
 */
@SuppressWarnings("ALL")
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
            System.out.println();
        }

        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            try {
                Map<CarType, Float> fullCostData;
                fullCostData = KPI.getTypeFullCost(
                        command.getKeyValue("t"),
                        command.getKeyValue("s"),
                        command.getKeyValue("e"));

                if (fullCostData.size() == 0)
                    throw new ConsoleCommandException("Отсутствуют данные для отображения!");

                for (Map.Entry<CarType, Float> e : fullCostData.entrySet())
                    System.out.println(Util.toLength(e.getKey().getName(),25) + " " + Util.round(e.getValue()));
            } catch (ApplicationException e) {
                throw new ConsoleCommandException(e.getMessage());
            }
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
             System.out.println();
         }

        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            try {
                CarTypeStatistic statistic;
                statistic = new CarTypeStatistic(command.getKeyValue("s"), command.getKeyValue("e"));

                if (statistic.getTypeList().isEmpty())
                    throw new ConsoleCommandException("Отсутствуют данные для отображения!");

                System.out.println();
                for (CarType carType : statistic.getTypeList()){
                    System.out.println("------ " + carType.getName() + " ------");
                    System.out.println(Util.toLength("НОМЕР", 10) + Util.toLength("ПРОБЕГ", 10) + carType.getNameAdditional());
                    List<CarData> list = statistic.getDataList(carType);
                    list.sort(new CarDataComparator(command.getKeyValue("f","mile"),
                                                    command.getKeyValue("t","asc")));

                    for (CarData carData : list)
                        System.out.println(
                            Util.toLength(carData.car.getLicense(),10) +
                            Util.toLength(carData.mileage == null ? "" : String.valueOf(carData.mileage), 10) +
                                    (carData.additional == null ? "" : String.valueOf(carData.additional)));
                }
                System.out.println();
            } catch (ApplicationException e) {
                throw new ConsoleCommandException(e.getMessage());
            }
        }
    },

    /**
     * Просмотр максимальных и минимальных затрат в разрезе типа транспорта
     * - выборка данных за дату/период
     */
    showextrema(new UserType[]{UserType.admin}){
        @Override
        public void showDescription() {
            System.out.println(this.name().toUpperCase() + " [max(default)/min]  - вывести максимальные/минимальные затраты на тип транспорта");
            System.out.println("    Задание периода анализа (формат dd/mm/yy):");
            System.out.println("        -s начало периода (если -e не задан, то берутся данные только за этот день)");
            System.out.println("        -e окончание периода");
            System.out.println();
        }

        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            try {
                Extrema extrema = new Extrema(
                        command.getValue("max"),
                        command.getKeyValue("s"),
                        command.getKeyValue("e"));

                System.out.println((extrema.extremaType.equals(ExtremaType.max) ? "Наибольшая" : "Наименьшая") +  " стоимость расхода у \"" + extrema.carType.getName() + "\" -> " + Util.round(extrema.value));
            } catch (ApplicationException e) {
                throw new ConsoleCommandException(e.getMessage());
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
            System.out.println();
        }

        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            if (Session.isSessionOpen())
                throw new ConsoleCommandException("Сессия уже открыта!");

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
            System.out.println();
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
            System.out.println();
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
            System.out.println();
        }
        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            try {
                CarPark.addCar(command.getKeyValue("t"), command.getValue());
            } catch (ApplicationException e) {
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
            System.out.println();
        }
        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            Collection<Car> cars = CarPark.getCars(command.getKeyValue("t"));

            if(cars.isEmpty())
                throw new ConsoleCommandException("В систему не доблено транспортных средств" +
                        (command.getKeyValue("t") == null ? "!" : " соответствующих фильтру!"));

            for(Car car : cars){
                System.out.println(car.getIdentifier());
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
            Collection<Driver> drivers = Driver.getDriversList().values();

            if (drivers.isEmpty())
                throw new ConsoleCommandException("В системе не заведено ни одного водителя!");

            System.out.println();
            System.out.println(Util.toLength("ЛОГИН", 10) + "ФИО");
            System.out.println();
            for (Driver driver : Driver.getDriversList().values()) {
                System.out.println(Util.toLength(driver.getLogin(), 10) + driver.getName());
            }
            System.out.println();
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
            System.out.println();
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
     * Показать список назначений
     */
    showappointment(new UserType[]{UserType.admin}){
        @Override
        public void showDescription() {
            System.out.println(this.name().toUpperCase() + " показать список назначений");
            System.out.println();
        }

        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            try {
                int firstColumnSize = 30;
                int licenseColumnSize = 6;
                int loginColumnSize = 3;

                System.out.println(Util.toLength("ТРАНСПОРТ", -20) +
                        Util.toLength("ВОДИТЕЛИ", -35));

                System.out.println();
                CarPark.getCarsList().values()
                        .stream()
                        .filter(car -> !car.hasDriver())
                        .forEach(car -> System.out.println(
                                Util.toLength(car.getCarType().getName() + " " + Util.toLength(car.getLicense(), -loginColumnSize),
                                        -firstColumnSize) + " | " +
                                        Util.toLength("", licenseColumnSize) + " НЕ НАЗНАЧЕН"));
                CarPark.getCarsList().values()
                        .stream()
                        .filter(Car::hasDriver)
                        .forEach(car -> System.out.println(
                                Util.toLength(car.getCarType().getName() + " " + Util.toLength(car.getLicense(), -loginColumnSize),
                                        -firstColumnSize) + " | " +
                                        Util.toLength(car.getDriver().getLogin(), licenseColumnSize) + " " + car.getDriver().getName()));
                Driver.getDriversList().values()
                        .stream()
                        .filter(driver -> !driver.hasAppointment())
                        .forEach(driver -> System.out.println(
                                Util.toLength("НЕ НАЗНАЧЕН " + Util.toLength("", loginColumnSize), -firstColumnSize) +
                                        " | " +
                                        Util.toLength(driver.getLogin(), 6) + " " + driver.getName()));
                System.out.println();
            } catch (Exception e){
                throw new ConsoleCommandException("При обработке информации возникли ошибки " + e.getMessage());
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
            System.out.println();
        }
        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            try {
                Driver.getDriverByLogin(command.safeGetValue()).appointToVehicle(CarPark.getCar(command.getKeyValue("v")));
            } catch (ApplicationException e) {
                throw new ConsoleCommandException(e.getMessage());
            }
        }
    },

    /**
     * Снять водителя с машины
     */
    removeappoint(new UserType[]{UserType.admin}){
        @Override
        public void showDescription() {
            System.out.println(this.name().toUpperCase() + " [логин] - снять водителя с машины");
            System.out.println();
        }
        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            try {
                Driver.getDriverByLogin(command.safeGetValue()).removeFromVehicle();
            } catch (NullCommandValueException e) {
                throw new ConsoleCommandException("Не указано сетевое имя водителя!");
            } catch (ApplicationException e){
                throw new ConsoleCommandException(e.getMessage());
            }
        }
    },

    /**
     * Сохранить данные
     */
    save(new UserType[]{UserType.admin}){
        @Override
        public void showDescription() {
            System.out.println(this.name().toUpperCase() + " сохраняет все изменения в данных");
            System.out.println();
        }

        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            try { StoredData.saveStoredData();
            } catch (SaveDataException e) {
                throw new ConsoleCommandException("При сохранении возникли ошибки!");
            }
        }
    },

    /**
     * Просмотр описания доступных комманд
     */
    help(new UserType[]{UserType.admin, UserType.user}){
        @Override
        public void showDescription() {
            System.out.println(this.name().toUpperCase() + " список доступных комманд");
            System.out.println();
        }
        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            try {
                for (ConsoleCmd com : ConsoleCmd.values())
                    com.showDescription();
            } catch (Exception e){
                throw new ConsoleCommandException("Непредвиденная ошибка");
            }
        }
    },

    /**
     * Выход из приложения
     */
    exit(new UserType[]{}){
        @Override
        public void showDescription() {
            System.out.println(this.name().toUpperCase() + " завершить работу приложения");
            System.out.println();
        }

        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            ConsoleCmd.closesession.run(null);
        }
    },

    /**
     * Загрузка тестовых данных
     */
    loadtestdata(new UserType[]{UserType.admin}){
        @Override
        public void showDescription() {
            System.out.println(this.name().toUpperCase() + " загрузка тестовых данных");
            System.out.println();
        }

        @Override
        public void run(ParsedInput command) throws ConsoleCommandException {
            try {
                FirstInitApp.fillAppWithTestData();
            } catch (IOException e) {
                throw new ConsoleCommandException("В процессе работы с файлом с тестовыми данными возникли ошибки " + e.getMessage());
            }
        }
    }
    ;

    //список пользователей имеющих доступ к команде
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
