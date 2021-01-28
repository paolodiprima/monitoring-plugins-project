package threatarrest.monitoring.editing;

import threatarrest.monitoring.IExtensionPoint;
import threatarrest.monitoring.Message;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/***
 * Plugin that check file editing
 */
public class EditingWatcher extends Plugin {
    public EditingWatcher(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() { log.info("EditingWatcher.start()"); }

    @Override
    public void stop() {
        log.info("EditingWatcher.stop()");
    }

    @Extension
    public static class FileEditingExtension implements IExtensionPoint {
        private static Logger LOGGER = LoggerFactory.getLogger(FileEditingExtension.class);
        protected BlockingQueue<Message> bQueue = new SynchronousQueue<>();
        private double time_0, time_f, executionTime;

        @Override
        public Message run(String... args) {
            LOGGER.info("start run with parameters: " + args[0]);
            Timer timer = new Timer();
            String path = args[0];
            TimerTask task = new TaskFileWatcher(new File(path), timer, bQueue);
            //check every second
            time_0 = System.nanoTime();
            timer.schedule(task, new Date(), 1000);
            Message msg = null;
            try {
                msg = bQueue.take();
                time_f = System.nanoTime();
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
            //convert time in seconds
            executionTime = (time_f - time_0) / 1000000000.0;
            msg.setExecutionTime(executionTime);
            return msg;

        }
    }
}
