package task6;

import java.util.concurrent.Semaphore;

public class FuelStation {
    private final Semaphore smp = new Semaphore(3, true);
    private float reserve;

    public FuelStation(float reserve) {
        this.reserve = reserve;
    }

    public float refuel(Auto auto) {
        if (reserve > 0) {
            try {
                System.out.println(String.format("%s заехал на заправку.", auto));
                smp.acquire();
                System.out.println(String.format("%s начал заправляться.", auto));
                synchronized (this) {
                    if (reserve >= auto.getCapacity()) {
                        auto.setRemainder(auto.getCapacity());
                        reserve -= auto.getCapacity();
                        System.out.println(String.format("На станци осталось %s единиц топлива.", reserve));
                        System.out.println(String.format("%s заправился.", auto));
                    } else if (reserve != 0) {
                        auto.setRemainder(reserve);
                        reserve = 0;
                        System.out.println("На станции нет больше топлива.");
                        System.out.println(String.format("%s заправил неполный бак. В баке %s/%s единиц топлива.",
                                auto, auto.getRemainder(), auto.getCapacity()));
                    } else {
                        System.out.println(String.format("%s в пустую заехал. Пока оплачивал топливо, оно закончилось.", auto));
                    }
                }
                Thread.sleep(5000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            smp.release();
        }
        return reserve;
    }

}
