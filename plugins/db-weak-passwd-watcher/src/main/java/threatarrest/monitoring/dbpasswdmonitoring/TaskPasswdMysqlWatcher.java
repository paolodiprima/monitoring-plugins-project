package threatarrest.monitoring.dbpasswdmonitoring;

import threatarrest.monitoring.Message;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.concurrent.BlockingQueue;

/***
 * Initialize a instance of class Message and
 * prepare the message to be send when the event monitored occurs
 * Send the message string to the caller using a BlockingQueue (asynchronously)
 */
public class TaskPasswdMysqlWatcher extends AbPasswdMysqlWatcher {
    private Timer timer;
    private BlockingQueue<Message> bQueue;
    public TaskPasswdMysqlWatcher(String url, String user, String passwd, Timer timer, BlockingQueue<Message> bQueue) {
        super(url, user, passwd, LoggerFactory.getLogger(AbPasswdMysqlWatcher.class));
        this.bQueue = bQueue;
        this.timer = timer;
    }

    @Override
    protected void onChange() {
        Message msg = new Message();
        String serverMysqlAddress = getUrl().split("\\?")[0];
        msg.setMsg("Root Password db: <"  + serverMysqlAddress +  "> has been modified");
        try {
            bQueue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.cancel();
    }
}
