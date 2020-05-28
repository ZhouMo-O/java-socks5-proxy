package client.test.cmdLineProcess;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import client.cmdLineProcess.cmdLineProcess;

public class cmdLineProcessTest {
    private String[] mockArgs;
    private cmdLineProcess cmdLineProcessing;

    @Test
    public void inputParameter() {
        mockArgs = new String[] { "-s", "192.168.1.165" };
        cmdLineProcessing = new cmdLineProcess(mockArgs);
        String serverIp = cmdLineProcessing.getParameter().get("-s");
        assertEquals("192.168.1.165", serverIp);
    }

    @Test
    public void inputFilePath() {
        mockArgs = new String[] { "-c",
                "D:/xiaowu/ProjectForHBuiderl/javaSocks5Proxy/client/test/cfgFileProcessTest/MockcfgFile.cfg" };
        cmdLineProcessing = new cmdLineProcess(mockArgs);
        String serverIp = cmdLineProcessing.getParameter().get("serverIp");
        assertEquals("192.168.1.172", serverIp);
    }
}