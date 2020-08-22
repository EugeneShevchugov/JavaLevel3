package task1;

import java.util.ArrayList;
import java.util.Arrays;

public class GenericClass<T> {
    private T[] obj;

    public GenericClass(T[] obj) {
        this.obj = obj;
    }

    public T[] getObj() {
        return obj;
    }

    public void setObj(T[] obj) {
        this.obj = obj;
    }

    // task 1
    public void changePositionsInArray() {
        T x = obj[0];
        obj[0] = obj[1];
        obj[1] = x;
    }

    // task 2
    public ArrayList<T> arrayToArrayList() {
        return new ArrayList<>(Arrays.asList(obj));
    }
}
