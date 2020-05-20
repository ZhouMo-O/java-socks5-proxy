package client;

// import java.util.Scanner;

public class client {
    public static void main(String[] args) {
        cmdProcess cmdProcessing = new cmdProcess(args);
        String serverIp = cmdProcessing.getServerIp();
        String serverPort = cmdProcessing.getServerPort();
        String key = cmdProcessing.getkey();
        String localHost = cmdProcessing.getlocalAddr();
        Boolean udpStatus = cmdProcessing.getUpd();
        System.out.println(serverIp + serverPort + key + localHost + udpStatus);
    }
}