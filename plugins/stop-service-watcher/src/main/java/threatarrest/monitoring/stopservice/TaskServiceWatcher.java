package threatarrest.monitoring.stopservice;

import threatarrest.monitoring.Message;

import java.util.Timer;
import java.util.concurrent.BlockingQueue;

/***
 * prepare the message to be send when the event monitored occurs
 * Send the message string to the caller using a BlockingQueue (asynchronously)
 */
public class TaskServiceWatcher extends AbServiceWatcher {
    private Timer timer;
    private BlockingQueue<Message> bQueue;

    public TaskServiceWatcher(String  host, int port, Timer timer, BlockingQueue<Message> bQueue){
        super(host,port);
        this.bQueue = bQueue;
        this.timer = timer;
    }
    @Override
    protected void onChange() {
        Message msg = new Message();
        msg.setMsg("Service Telnet Disabled");
        try {
            bQueue.put(msg);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.cancel();
    }
}
