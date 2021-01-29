package threatarrest.monitoring.servicemonitoring;

import threatarrest.monitoring.Message;
import org.pf4j.PluginWrapper;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import threatarrest.monitoring.IExtensionPoint;


/***
 * This plugin executes package sniffing on the given port and host.
 * The objective is to understand that a port-scanning has been executed.
 */
public class PortScanningWatcher extends Plugin {

    public PortScanningWatcher(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() { log.info("MonitorPortScanning.start()");  }

    @Override
    public void stop() {
        log.info("MonitorPortScanning.stop()");
    }

    @Extension
    public static class PortScanningExtension implements IExtensionPoint {
        private double time_0, time_f, executionTime;

        @Override
        public Message run(String... args) {
            String node = args[0];
            int port = Integer.parseInt(args[1]);

            TaskPortScanning taskPortScanning = new TaskPortScanning(node, port);
            time_0 = System.nanoTime();
            Message msg = taskPortScanning.runSniffing();
            time_f = System.nanoTime();
            //convert time in seconds
            executionTime = (time_f - time_0) / 1000000000.0;
            msg.setExecutionTime(executionTime);
            return msg;

        }
    }
}
