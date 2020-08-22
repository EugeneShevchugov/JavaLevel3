package task1;

import java.util.ArrayList;

public class Box<F extends Fruit> {
    private ArrayList<F> fruits;
    private float totalWeight;

    public Box() {
        fruits = new ArrayList<>();
    }

    // Учитывая что вес одинаковый - можно брать только 1 элемент и умножать на количество фруктов(т.к. они не могут быть перемешаны)
    public float getTotalWeight() {
        if (fruits.size() != 0) {
            totalWeight = fruits.size() * fruits.get(0).getWeight();
        }
        else {
            System.out.println("Фруктов в коробке нет");
        }
        return totalWeight;
    }

    public boolean compare(Box<?> anotherBox) {
        return this.totalWeight == anotherBox.totalWeight;
    }

    public void moveFruitsToAnotherBox(Box<F> box) {
        box.fruits.addAll(fruits);
        fruits.clear();
    }

    public void addFruit(F fruit) {
        fruits.add(fruit);
    }
}
