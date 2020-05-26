package client.test.cfgFileProcessTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Test;
import client.cfgfileProcess.cfgFileProcess;

public class cfgFileProcessTest {
    private String mockFilePath = "D:/xiaowu/ProjectForHBuiderl/javaSocks5Proxy/client/test/cfgFileProcessTest/MockcfgFile.cfg";
    private String errorPath = "errorPath";

    /* input error path */
    @Test
    public void inputErroPath() {
        cfgFileProcess cfgPro = new cfgFileProcess(errorPath);
        assertNull(null, cfgPro.getConfigMap().get("serverIp"));
    }

    @Test
    public void inputTruePath() {
        cfgFileProcess cfgPro = new cfgFileProcess(mockFilePath);
        Map<String, String> cfgFileMap = cfgPro.getConfigMap();
        assertEquals("192.168.1.172", cfgFileMap.get("serverIp"));
    }
}