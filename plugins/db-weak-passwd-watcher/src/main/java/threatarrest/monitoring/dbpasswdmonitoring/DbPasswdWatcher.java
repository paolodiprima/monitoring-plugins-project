package threatarrest.monitoring.dbpasswdmonitoring;

import threatarrest.monitoring.IExtensionPoint;
import threatarrest.monitoring.Message;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/***
 * This class contains that extends the class pf4j Plugin
 * contains an inner class that extends the Extensions point IExtensionPoint
 * <p>
 *  The inner class execute a check root password change into MySQL server
 */
public class DbPasswdWatcher extends Plugin {

    public DbPasswdWatcher(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() { log.info("DbPasswdWatcher.start()"); }

    @Override
    public void stop() {
        log.info("DbPasswdWatcher.stop()");
    }

    /***
     * This inner class use the classes Timer and TimeTask in order
     * to perform a connection to the root user of MySQL server
     * each second.
     */
    @Extension
    public static class DbPasswdWatcherExtension implements IExtensionPoint {

        protected BlockingQueue<Message> bQueue = new SynchronousQueue<>();
        private double time_0, time_f, executionTime;

        /***
         * Initialize the task and schedule it for execution period 1 sec starting from now
         * @param args list or arguments needed by the plugin  to perform the monitoring
         * @return instance of class Message
         */
        @Override
        public Message run(String...args) {
            String url = args[0];
            String user = args[1];
            String passwd = args[2];
            Timer timer = new Timer();
            TimerTask task = new TaskPasswdMysqlWatcher(url, user, passwd, timer, bQueue);
            time_0 = System.nanoTime();
            timer.schedule(task, new Date(),1000);
            Message  msg = null ;
            try {
                msg = bQueue.take();
                time_f = System.nanoTime();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executionTime = (time_f - time_0) / 1000000000.0;
            msg.setExecutionTime(executionTime);
            return msg;
            //+ " -- executed in " + executionTime + " sec";
        }
    }
}
