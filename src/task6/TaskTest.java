package task6;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test(expected = RuntimeException.class)
    public void testExceptionInFirstMethod() {
        Task.getArrayAfterLastFour(new int[]{1,2,3,5});
    }

    @Test
    public void testAllGoodInFirstMethod() {
        assertEquals(new int[]{1,7}, Task.getArrayAfterLastFour(new int[]{1,2,3,4,1,7}));
    }

    @Test
    public void testAllGoodButEmptyArray() {
        assertEquals(new int[]{}, Task.getArrayAfterLastFour(new int[]{4}));
    }

    @Test
    public void testAllGoodInSecondMethod() {
        assertTrue(Task.checkFourAndOne(new int[]{1, 4}));
    }

    @Test
    public void testAllBadInSecondMethod() {
        assertFalse(Task.checkFourAndOne(new int[]{1,4,5}));
    }
}