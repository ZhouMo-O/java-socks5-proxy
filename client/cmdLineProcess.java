package client;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

public class cmdLineProcess {

    private String[] args;
    private Logger log = Logger.getGlobal();
    private String configFilePath;
    private Map<String, String> parameterMap = new HashMap<String, String>();
    private Map<String, String> cmdMap = new HashMap<>();

    public cmdLineProcess(String[] args) {
        log.setLevel(Level.INFO);
        this.args = args;
        this.processCmdInput(this.args);
    }

    private void processCmdInput(String[] args) {
        if (this.args.length > 0) {
            for (int i = 0; i < this.args.length; i++) {
                if (this.args[i].equals("-c")) {// if user input -c then use file process.
                    configFilePath = this.args[i + 1];
                    cfgFileProcess cfgProcess = new cfgFileProcess(configFilePath);
                    parameterMap = cfgProcess.getConfigMap();
                    break; // out
                }

                log.info("Put Data key is: " + args[i] + " value is: " + args[i + 1]);
                parameterMap.put(args[i], args[i + 1]); // if cmdLine input parmater put on the map;
                i++;
            }
        } else {

        }
    }

    private void initParameter() {
        cmdMap.put("-s", "remoteServer");
        cmdMap.put("-p", "remotePort");
        cmdMap.put("-l", "localIp");
        cmdMap.put("-P", "localPort");
        cmdMap.put("-k", "password");
        cmdMap.put("-u", "udp");
    }

    public Map<String, String> getParameter() {
        return parameterMap;
    }

}