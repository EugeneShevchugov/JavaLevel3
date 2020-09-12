package task7;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class TestMoney {
    private Class<Money> mClass;
    private boolean isMoney;

    @BeforeSuit
    public boolean checkClassIsCar(Class<Money> mClass) {
        this.mClass = mClass;
        if (mClass == null) {
            isMoney = false;
            System.out.println(false);
            return false;
        } else if (mClass.getSimpleName().equals("Money")) {
            isMoney = true;
            System.out.println(true);
            return true;
        } else {
            isMoney = false;
            return false;
        }
    }

    @Test
    @Priority()
    public boolean checkIfMoneyHasPublicFields() {
        if (!isMoney) {
            return false;
        }
        Field[] fields = mClass.getFields();
        for (Field field : fields) {
            if (field.getName().equals("currency")) {
                System.out.println(true);
                return true;
            }
        }
        System.out.println(false);
        return false;
    }

    @Test
    @Priority()
    public boolean checkIfMoneyHasPrivateFields() {
        if (!isMoney) {
            return false;
        }
        try {
            Field privateField = mClass.getDeclaredField("amountToTransfer");
            int modifiers = privateField.getModifiers();
            if (Modifier.isPrivate(modifiers)) {
                System.out.println(true);
                return true;
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        System.out.println(false);
        return false;
    }

    public boolean isMoney() {
        return isMoney;
    }

    @AfterSuit
    public void ending() {
        System.out.println("TestCar Ending");
    }
}
