package client.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UTest {
    @Test
    public void myTest() {
        assertEquals(1, 11);
        System.out.println("111");
    }

    public static void main(String[] args) {
        UTest test = new UTest();
        test.myTest();
    }
}