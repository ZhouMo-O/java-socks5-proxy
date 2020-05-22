package client;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class cfgFileProcess {
    private String[] args;
    private Map<String, String> configMap = new HashMap<String, String>();
    private String serverIp;
    private String serverPort;
    private String password;
    private String localIp = "0.0.0.0";
    private String localPort;
    private boolean udp = false;
    private String configFilePath;
    private Logger log = Logger.getGlobal();
    // private String encryptMethod; //加密方式唯一，不支持更多加密方式

    public cfgFileProcess(String[] args) {
        processCmdInput(this.args = args);
        log.setLevel(Level.INFO);
    }

    private void processCmdInput(String[] args) {
        if (this.args.length > 0) {
            for (int i = 0; i < this.args.length; i++) {

                if (this.args[i].equals("-c")) {
                    configFilePath = this.args[i + 1];
                    this.openConfigFile(configFilePath);
                    break;// if file break
                }

                if (this.args[i].equals("-s")) {
                    serverIp = this.args[i + 1];
                }

                if (this.args[i].equals("-p")) {
                    serverPort = this.args[i + 1];
                }

                if (this.args[i].equals("-l")) {
                    localPort = this.args[i + 1];
                }

                if (this.args[i].equals("-k")) {
                    password = this.args[i + 1];
                }

                if (this.args[i].equals("-u")) {
                    udp = true;
                }
            }
        }
    }

    private void openConfigFile(String filePath) {
        File jsonFile = new File(filePath);
        try {
            if (jsonFile.exists() && jsonFile.isFile()) {
                log.info("open " + jsonFile + " is sucessfull");
                Scanner cfgFileData = new Scanner(Paths.get(jsonFile.getAbsolutePath()), "UTF-8");
                while (cfgFileData.hasNextLine()) {
                    this.processConfigData(cfgFileData.nextLine());
                }
            } else {
                log.severe(jsonFile + "not exists or not is a file");
            }
        } catch (Exception e) {
            log.severe(e.getMessage());
        }
    }

    private void processConfigData(String cfgFileData) {
        String cfgReg = "(.*)=(.*)";
        Pattern cfgPattern = Pattern.compile(cfgReg);
        Matcher cfgMatcher = cfgPattern.matcher(cfgFileData);

        if (cfgMatcher.matches()) {
            String key = cfgMatcher.group(1);
            String value = cfgMatcher.group(2);
            log.info("put data :" + key + value);
            configMap.put(key, value);
        } else {
            log.info(" No match found");
        }
    }

    public Map<String, String> getConfigMap() {
        return configMap;
    }
}