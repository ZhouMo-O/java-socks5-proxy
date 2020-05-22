package client;

import java.util.Map;

// import java.util.Scanner;

public class client {
    public static void main(String[] args) {
        cfgFileProcess cmdProcessing = new cfgFileProcess(args);
        Map<String, String> cfgMap = cmdProcessing.getConfigMap();
        System.out.println(cfgMap.get("serverIp"));
    }
}