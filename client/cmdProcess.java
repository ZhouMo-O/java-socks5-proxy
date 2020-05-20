package client;

import java.io.*;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class cmdProcess {
    private String[] args;
    private String serverIp;
    private String serverPort;
    private String key;
    private String localAddr;
    private boolean udp = false;
    private String configFilePath;
    private Logger log = Logger.getGlobal();
    // private String encryptMethod; //加密方式唯一，不支持更多加密方式

    public cmdProcess(String[] args) {
        processCmdInput(this.args = args);
        log.setLevel(Level.INFO);
    }

    private void processCmdInput(String[] args) {
        if (this.args.length > 0) {
            for (int i = 0; i < this.args.length; i++) {

                if (this.args[i].equals("-c")) {
                    configFilePath = this.args[i + 1];
                    this.openJsonConfigFile(configFilePath);
                    break;
                }

                if (this.args[i].equals("-s")) {
                    serverIp = this.args[i + 1];
                }

                if (this.args[i].equals("-p")) {
                    serverPort = this.args[i + 1];
                }

                if (this.args[i].equals("-l")) {
                    localAddr = this.args[i + 1];
                }

                if (this.args[i].equals("-k")) {
                    key = this.args[i + 1];
                }

                if (this.args[i].equals("-u")) {
                    udp = true;
                }
            }
        }
    }

    private void openJsonConfigFile(String filePath) {
        File jsonFile = new File(filePath);
        try {
            if (jsonFile.exists() && jsonFile.isFile()) {
                log.info("open " + jsonFile + " is sucessfull");
                Scanner jsonFileData = new Scanner(Paths.get(jsonFile.getAbsolutePath()), "UTF-8");
                while (jsonFileData.hasNextLine()) {
                    this.processJsonConfigData(jsonFileData.nextLine());
                }
            } else {
                log.severe(jsonFile + "not exists or not is a file");
            }
        } catch (Exception e) {
            log.severe(e.getMessage());
        }
    }

    private void processJsonConfigData(String jsonFileData) {
        System.out.println(jsonFileData);
    }

    public String getServerIp() {
        return serverIp;
    }

    public String getServerPort() {
        return serverPort;
    }

    public String getlocalAddr() {
        return localAddr;
    }

    public String getkey() {
        return key;
    }

    public boolean getUpd() {
        return udp;
    }
}