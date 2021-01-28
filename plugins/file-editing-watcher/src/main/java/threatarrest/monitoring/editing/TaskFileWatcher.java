package threatarrest.monitoring.editing;

import threatarrest.monitoring.Message;

import java.io.File;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
/***
 * Initialize an instance of class Message and
 * prepare the message to be send when the event monitored occurs
 * Send the message string to the caller using a BlockingQueue (asynchronously)
 */
public class TaskFileWatcher extends AbFileWatcher {
    Timer timer;
    BlockingQueue<Message> bQueue;

    public TaskFileWatcher(File file, Timer timer, BlockingQueue<Message> bQueue) {
        super(file);
        this.timer = timer;
        this.bQueue = bQueue;
    }

    @Override
    protected void onChange(File file) {
        String separator = System.getProperty("file.separator");
        try {
            Message msg = new Message();
            msg.setMsg( " File : <" + file.getPath()  + ">  has been modified");
            bQueue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.cancel();
    }
}

