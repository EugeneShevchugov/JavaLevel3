package task7;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Testing {
    public static void start(Class someClass) { // Без сырого типа тут никак
        Class[] classes = {TestCar.class, TestMoney.class};
        for(Class c : classes) {
            Method[] methods = c.getMethods();

            List<Method> start = new ArrayList<>();
            List<Method> test = new ArrayList<>();
            List<Method> stop = new ArrayList<>();

            for(Method m : methods) {
                if (m.isAnnotationPresent(BeforeSuit.class)) {
                    start.add(m);
                } else if (m.isAnnotationPresent(Test.class)) {
                    test.add(m);
                } else if (m.isAnnotationPresent(AfterSuit.class)) {
                    stop.add(m);
                }
            }
            if (start.size() > 1 || stop.size() > 1) {
                throw new RuntimeException();
            }

            sortMethods(test);

            try {
                Object o = c.newInstance();
                start.get(0).invoke(o, someClass);
                for(Method m : methods) {
                    m.invoke(o);
                }
                stop.get(0).invoke(o);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static void sortMethods(List<Method> methods) {
        methods.sort((m1, m2) -> {
            int m1pr = m1.getAnnotation(Priority.class).priority();
            int m2pr = m2.getAnnotation(Priority.class).priority();
            return Integer.compare(m1pr, m2pr);
        });
    }
}
