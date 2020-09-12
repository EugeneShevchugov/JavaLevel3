package task5;

import static java.lang.Thread.sleep;

public abstract class Auto {
    private float capacity;
    private float consumption;
    private float remainder;
    private String vin;

    public Auto(float capacity, float consumption, String vin) {
        this.capacity = capacity;
        this.consumption = consumption;
        this.vin = vin;
    }

    public void ride(FuelStation fuelStation) {
        remainder = capacity;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(3000);
                        remainder -= consumption;
                        if (remainder <= 0) {
                            System.out.println("У транспорта " + vin + " пустой бак.");
                            refuel(fuelStation);
                            if (remainder <= 0) {
                                System.out.println("Транспорт " + vin + " никуда больше не поедет.");
                                return;
                            }
                        }
                        System.out.println(String.format("Транспорт %s в пути, в баке осталось %s единиц топлива.", vin, remainder));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void refuel(FuelStation fuelStation) {
        fuelStation.refuel(this);
    }

    public float getCapacity() {
        return capacity;
    }

    public void setRemainder(float remainder) {
        this.remainder = remainder;
    }

    public float getRemainder() {
        return remainder;
    }

    @Override
    public String toString() {
        return "Транспорт " + vin;
    }
}
