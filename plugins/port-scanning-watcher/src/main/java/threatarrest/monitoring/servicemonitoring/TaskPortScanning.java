package threatarrest.monitoring.servicemonitoring;

import threatarrest.monitoring.Message;

/***
 * prepare the message to be send when the event monitored occurs
 * Send the message string as return value of the method onPortScanning
 */
public class TaskPortScanning extends AbPortScanningWatcher {
    public TaskPortScanning(String node,int port) {
        super(node,port);
    }

    @Override
    protected Message onPortScanning() {

        Message msg = new Message();
        msg.setMsg("scanning port " + getPort() + " Executed ");
        return msg;
    }

}

