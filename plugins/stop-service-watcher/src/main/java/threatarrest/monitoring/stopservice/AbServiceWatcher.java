package threatarrest.monitoring.stopservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TimerTask;
import java.net.Socket;

/***
 * Check if a service is running
 * @property host: host that provide  the service
 * @property  port: port where the service if provided
 */
public abstract class AbServiceWatcher extends TimerTask {
    private String host;
    private int port;

    public AbServiceWatcher(String host, int port){
        this.port = port;
        this.host = host;
    }
    @Override
    public void run() {
        if (!(serverIsListening())) {
            String operatingSystem = System.getProperty("os.name");

            // in Linux:  send immediately the msg
            if (!operatingSystem.toLowerCase().contains("windows"))
                onChange();
                // in windows: make sure that StartUp Type is not Automatic
            else {
                if (!isAutomatic()) onChange();
            }
        }
    }

    /***
     * check if the server is listening from a port
     * @return true if server is listening, false otherwise.
     *
     */
    public boolean serverIsListening(){
        Socket socket = null;
        try {
            socket = new Socket(host,port);
            return true;
        } catch (Exception e){
            return false;
        }
        finally {
            if (socket != null)
                try { socket.close();}
                catch (Exception e ) {}
        }
    }

    /***
     * check if start type of the service telnet
     * @return true if start type is "automatic"
     *
     */
    public  boolean isAutomatic() {
        ProcessBuilder checkStartUpType = new ProcessBuilder("powershell","-Command","(Get-Service 'KpyM Telnet SSH Server v1.19c').StartType");
        try {
            Process checkStartUpProcess = checkStartUpType.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(checkStartUpProcess.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null ) {
                if (line.contains("Automatic")) return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected abstract void onChange();
}
