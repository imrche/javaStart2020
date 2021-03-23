package com.rch.multithreading.threads;

import java.util.List;

/**
 * <p>Класс-таймер, задающий продолжительность выполнения бизнес-процесса</p>
 * <p>Один раз в такт запрашивает статусы всех потоков</p>
 * <p><b>workTime</b> - продолжительность выполнения</p>
 * <p><b>clockPeriod</b> - длительность одного такта выполнения в мс.</p>
 * <p><b>processes</b> - список всех потоков бизнес-процесса (покупатели/поставщики)</p>
 */
public class Timer extends Thread{
    private final int clockPeriod = 1000;
    private final int workTime;
    public final List<BusinessProcess> processes;

    /**
     * Класс-таймер, задающий продолжительность выполнения бизнес-процесса
     * @param workTime продолжительность выполнения
     * @param processes список всех потоков бизнес-процесса
     */
    public Timer(int workTime, List<BusinessProcess> processes){
        this.workTime = workTime;
        this.processes = processes;
    }

    @Override
    public void run() {
        super.run();
        for (int i = 0; i < workTime; i++) {
            try {
                sleep(clockPeriod);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.printf("-----------period %s-----------%n", i+1);
            for (BusinessProcess bp : processes)
                bp.showStatus();

            System.out.println("На складе -> " + processes.get(0).warehouse.volume);
        }
    }
}
