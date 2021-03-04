package com.rch.fuelcounter.data;

import com.rch.fuelcounter.data.cars.Car;
import com.rch.fuelcounter.data.cars.CarPark;
import com.rch.fuelcounter.data.cars.CarType;
import com.rch.fuelcounter.data.drivers.Driver;
import com.rch.fuelcounter.exceptions.LoadDataException;
import com.rch.fuelcounter.exceptions.SaveDataException;
import com.rch.fuelcounter.ui.UserType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс для сохранения и загрузки данных приложения
 */
public class StoredData implements Serializable {
    private static final String dataFilePath = ClassLoader.getSystemResource("").getPath() + "data.bin";
    private static final long serialVersionUID = 1L;

    private Map<String, Car> cars;                  //транспортные средства
    private Map<String, Driver> drivers;            //водители
    private Map<String, byte[]> userTypePasswords;  //пользовательские пароли
    private Map<String, CarType> carTypes;          //типы ТС

    /**
     * Загрузка данных приложения из файла
     * @throws LoadDataException файл с данными не существует
     */
    public static void loadStoredData() throws LoadDataException {
        try(FileInputStream r = new FileInputStream(dataFilePath)) {
            if (r.available() > 0) {
                ObjectInputStream o = new ObjectInputStream(r);
                StoredData storedData = (StoredData) o.readObject();

                Driver.setDrivers(storedData.drivers);
                CarPark.setCarsList(storedData.cars);
                CarType.types = storedData.carTypes;

                for (Map.Entry<String, byte[]> entry : storedData.userTypePasswords.entrySet())
                    UserType.valueOf(entry.getKey()).setPassword(decode64(entry.getValue()));
            }
        } catch (FileNotFoundException e) {
            throw new LoadDataException("Файл с сохраненными данными отсутствует!");
        } catch (Exception e) {
            throw new LoadDataException(e.getMessage());
        }
    }

    /**
     * Сохранение данных приложения в файл
     * @throws SaveDataException ошибки IO при попытке записи в файл
     */
    public static void saveStoredData() throws SaveDataException {
        try (FileOutputStream fo = new FileOutputStream(dataFilePath)){
            ObjectOutputStream o = new ObjectOutputStream(fo);
            StoredData storedData = new StoredData();

            storedData.drivers = Driver.getDriversList();
            storedData.cars = CarPark.getCarsList();
            storedData.carTypes = CarType.types;

            storedData.userTypePasswords = new HashMap<>();
            for (UserType userType : UserType.values())
                storedData.userTypePasswords.put(userType.name(), encode64(userType.getPassword()));

            o.writeObject(storedData);
            o.close();
        } catch (IOException e) {
            throw new SaveDataException("Ошибка при сохранении данных приложения!");
        }
    }

    /**
     * Кодирование base64
     * @param str строка для кодирования
     * @return байтовый массив
     */
    private static byte[] encode64(String str){
        return Base64.getEncoder().encode(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Декодирование base64
     * @param str байтовый массив для декодирования
     * @return строка в UTF-8
     */
    private static String decode64(byte[] str){
        return new String(Base64.getDecoder().decode(str));
    }
}
