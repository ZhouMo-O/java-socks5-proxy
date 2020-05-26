package client;

import java.util.Map;

import client.cmdLineProcess.cmdLineProcess;

// import java.util.Scanner;

public class client {
    public static void main(String[] args) {
        cmdLineProcess cmdProcessing = new cmdLineProcess(args);
        Map<String, String> cfgMap = cmdProcessing.getParameter();
        System.out.println(cfgMap.get("serverIp"));
    }
}