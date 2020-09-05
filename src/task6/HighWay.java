package task6;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HighWay {

    public static void main(String[] args) {
        createTenVehicle();
    }

    public static void createTenVehicle() {
        Auto[] autos = {
                new Car("01CAR"), new Bus("02BUS"), new Truck("03TRU"),
                new Car("04CAR"), new Car("05CAR"), new Bus("06BUS"),
                new Truck("07TRU"), new Car("08CAR"), new Bus("09BUS"),
                new Truck("10TRU")};

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        FuelStation fuelStation = new FuelStation(1000f);
        for (Auto v : autos) executorService.submit(() -> v.ride(fuelStation));
        executorService.shutdown();
    }

}
