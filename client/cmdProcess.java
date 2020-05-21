package client;

import java.io.*;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class cmdProcess {
    private String[] args;
    private String serverIp;
    private String serverPort;
    private String password;
    private String localIp = "0.0.0.0";
    private String localPort;
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
                    this.openConfigFile(configFilePath);
                    break;
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

            if (key.equals("serverIp") && ipVerify(value)) {
                log.info("serverIp is: " + value);
                this.serverIp = value;
            }
            if (key.equals("serverPort") && portVerify(value)) {
                log.info("serverPort is: " + value);
                this.serverPort = value;
            }
            if (key.equals("localIp") && ipVerify(value)) {
                log.info("localIp is: " + value);
                this.localIp = value;
            }
            if (key.equals("localPort") && portVerify(value)) {
                log.info("localPort is: " + value);
                this.localPort = value;
            }
            if (key.equals("password")) {
                log.info("password is: " + value);
                this.password = value;
            }
            if (key.equals("udp") && udpVerify(value)) {
                log.info("udp is: " + value);
                this.localIp = value;
            }

        } else {
            log.info(" No match found");
        }
    }

    private Boolean ipVerify(String ip) {
        String ipReg = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern ipPattern = Pattern.compile(ipReg);
        Matcher ipVerify = ipPattern.matcher(ip);
        if (ipVerify.matches()) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean portVerify(String port) {
        int newPort = Integer.parseInt(port);
        if (newPort > 65535 || newPort < 0) {
            return false;
        } else {
            return true;
        }
    }

    private Boolean udpVerify(String udpInput) {
        Boolean udpStatus = Boolean.valueOf(udpInput);
        if (udpStatus) {
            return true;
        } else {
            return false;
        }
    }

    public String getServerIp() {
        return serverIp;
    }

    public String getServerPort() {
        return serverPort;
    }

    public String getlocalIp() {
        return localIp;
    }

    public String getlocalPort() {
        return localPort;
    }

    public String getPassword() {
        return password;
    }

    public boolean getUpd() {
        return udp;
    }
}