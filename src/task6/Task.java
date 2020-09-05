package task6;

import java.util.Arrays;

public class Task {
    public static int[] getArrayAfterLastFour(int[] array) {
        int pos = Arrays.toString(array)
                .replaceAll("\\[", "")
                .replaceAll("]", "")
                .replaceAll(" ", "")
                .replaceAll(",", "")
                .lastIndexOf('4');
        if (pos == -1) {
            throw new RuntimeException("No four in this array");
        }
        if (pos == array.length - 1) {
            return new int[]{};
        }
        int[] result = new int[array.length - pos - 1];
        pos++;
        if (result.length >= 0) System.arraycopy(array, pos, result, 0, result.length);
        return result;
    }

    public static boolean checkFourAndOne(int[] array) {
        for (int value : array) {
            if (value == 4 || value == 1) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
}
