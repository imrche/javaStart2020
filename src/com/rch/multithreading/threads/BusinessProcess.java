package com.rch.multithreading.threads;

import com.rch.multithreading.entity.Warehouse;

/** Абстрактный класс бизнес-процесса
 * <p><b>name</b> - имя инстанса процесса</p>
 * <p><b>warehouse</b> - склад</p>
 * <p><b>frequency</b> - частота, с которой повторяется действие процесса</p>
 * <p><b>counterForStatusDemonstration</b> - накопленное количество обработанной (но еще не показанной) продукции</p>
 */

public abstract class BusinessProcess extends Thread{
    protected final String name;
    protected final Warehouse warehouse;
    protected final int frequency = 1000;
    protected int counterForStatusDemonstration = 0;


    public BusinessProcess(Warehouse warehouse, String name){
        this.warehouse = warehouse;
        this.name = name;
    }

    public void showStatus(){
        System.out.println(getHeader() + counterForStatusDemonstration);
        counterForStatusDemonstration = 0;
    }

    /**
     * Действия в рамках работы процесса, специфичные для конкретных видов БП
     */
    public void action(){}

    /**
     * Заголовок для вывода специфичного статуса БП
     * @return заголовок
     */
    public String getHeader(){return "";}

    @Override
    public void run() {
        super.run();
        while (true){
            try {
                sleep(frequency);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            action();
        }
    }
}
