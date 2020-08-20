package task1;

public class Main {
    public static void main(String[] args) {
        doDemo1();
    }

    public static void doDemo1() {
        Box<Apple> appleBox = new Box<>();
        appleBox.addFruit(new Apple());
        appleBox.addFruit(new Apple());
        appleBox.addFruit(new Apple());
        System.out.println(appleBox.getTotalWeight());

        Box<Orange> orangeBox = new Box<>();
        orangeBox.addFruit(new Orange());
        orangeBox.addFruit(new Orange());
        orangeBox.addFruit(new Orange());
        System.out.println(orangeBox.getTotalWeight());

        System.out.println(orangeBox.compare(appleBox));

        appleBox.addFruit(new Apple());
        appleBox.addFruit(new Apple());
        appleBox.addFruit(new Apple());
        orangeBox.addFruit(new Orange());
        System.out.println(appleBox.getTotalWeight());
        System.out.println(orangeBox.getTotalWeight());
        System.out.println(orangeBox.compare(appleBox));

        Box<Apple> newAppleBox = new Box<>();
        System.out.println(newAppleBox.getTotalWeight());
        appleBox.moveFruitsToAnotherBox(newAppleBox);
        System.out.println(newAppleBox.getTotalWeight());
    }
}
