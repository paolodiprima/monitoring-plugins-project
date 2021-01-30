package threatarrest.monitoring.stopservice;


import threatarrest.monitoring.IExtensionPoint;
import threatarrest.monitoring.Message;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class ServiceWatcher extends Plugin {

    public ServiceWatcher(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() { log.info("TelnetWatcher.start()"); }

    @Override
    public void stop() {
        log.info("TelnetWatcher.stop()");
    }

    @Extension
    public static class TelnetWatcherExtension implements IExtensionPoint {
        private static Logger LOGGER = LoggerFactory.getLogger(TelnetWatcherExtension.class);
        private BlockingQueue<Message> bQueue = new SynchronousQueue<>();
        private double time_0, time_f, executionTime;

        @Override
        public Message run(String...args) {
            LOGGER.info("start run with parameters: " + args[0] + " " + args[1]);
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            Timer timer = new Timer();
            TimerTask task = new TaskServiceWatcher(host,port,timer,bQueue);
            time_0 = System.nanoTime();
            // check every second
            timer.schedule(task, new Date(), 1000);
            Message msg = null;
            try {
                msg = bQueue.take();
                time_f = System.nanoTime();
            } catch (InterruptedException e){
                LOGGER.error(e.getMessage());
            }
            // convert time in seconds
            executionTime = (time_f - time_0)/1000000000.0 ;
            msg.setExecutionTime(executionTime);
            return msg;
            //+ " -- executed in " + executionTime + " sec";
        }
    }
}
