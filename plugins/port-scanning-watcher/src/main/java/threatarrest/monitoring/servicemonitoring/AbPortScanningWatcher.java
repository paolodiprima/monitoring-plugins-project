package threatarrest.monitoring.servicemonitoring;

import threatarrest.monitoring.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/***
 * intercept first TCP package send to the port requested at the interface localhost
 * Command line tools are TCPDump for linux and Tshark for Win10
 */
public abstract class AbPortScanningWatcher {
    private static Logger LOGGER = LoggerFactory.getLogger(AbPortScanningWatcher.class);
    private String node;
    private int port;

    public AbPortScanningWatcher(String node, int port) {

        this.port = port;
        this.node = node;

    }

    /***
     * this method intercepts TCP package send to the host at a defined port
     *
     */
    public Message runSniffing() {
        ProcessBuilder sniffingProcessBuilder = null;

        String operatingSystem = System.getProperty("os.name");
        if (operatingSystem.toLowerCase().contains("windows")) {
            String winCmd = String.format("tshark -i %s -c1 -f \"dst port %s\"", getLoopBackId(), port);
            sniffingProcessBuilder = new ProcessBuilder("cmd", "/c", winCmd);

        } else {
            String linuxCmd = String.format("/usr/sbin/tcpdump -v -i lo -c 1 \"dst %s and dst port %d\" ", node, port);
            sniffingProcessBuilder = new ProcessBuilder("/bin/bash", "-c", linuxCmd);
        }
        try {
            Process sniffingProcess = sniffingProcessBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(sniffingProcess.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null)
                LOGGER.info("network scanning output : " + line);
            return onPortScanning();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getPort() {
        return port;
    }

    /***
     * Return the ID of loopback. Since in Win10 the ID of loopback may change.
     *
     */
    private String getLoopBackId() {
        ProcessBuilder processBuilder = null;
        String winCmd = "tshark -D  | findstr \"loopback\"";
        processBuilder = new ProcessBuilder("cmd", "/c", winCmd);
        Process downloadProcess = null;
        try {
            downloadProcess = processBuilder.start();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(downloadProcess.getInputStream()));
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        String interfaceNum = line.substring(0, 1);
        return interfaceNum;
    }

    abstract Message onPortScanning();

}



