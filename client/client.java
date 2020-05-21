package client;

// import java.util.Scanner;

public class client {
    public static void main(String[] args) {
        cmdProcess cmdProcessing = new cmdProcess(args);
        String serverIp = cmdProcessing.getServerIp();
        String serverPort = cmdProcessing.getServerPort();
        String password = cmdProcessing.getPassword();
        String localHost = cmdProcessing.getlocalIp();
        Boolean udpStatus = cmdProcessing.getUpd();
        System.out.println(serverIp + serverPort + password + localHost + udpStatus);
    }
}