package task7;

public class Main {
    public static void main(String[] args) {
        Car car = new Car("Lexus LX570");
        Money money = new Money("Dollar");
        Testing.start(car.getClass());
        Testing.start(money.getClass());
    }
}
