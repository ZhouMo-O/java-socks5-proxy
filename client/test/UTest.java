package client.test;

import static org.junit.Assert.assertEquals;
import java.util.Map;
import org.junit.Test;
import client.cmdLineProcess.cmdLineProcess;

public class UTest {
    private String[] testArgs = new String[] { "-s", "192.168.1.123" };
    private cmdLineProcess cmdLineProcessTest;

    public UTest() {
        cmdLineProcess cmdLineProcessTest = new cmdLineProcess(testArgs);
        this.cmdLineProcessTest = cmdLineProcessTest;
    }

    @Test
    public void TestIp() {
        Map<String, String> Parameter = cmdLineProcessTest.getParameter();
        assertEquals("192.168.1.123", Parameter.get("-s"));
    }

    public static void main(String[] args) {
        UTest test = new UTest();
        test.TestIp();
    }
}