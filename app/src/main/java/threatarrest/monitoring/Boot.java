package threatarrest.monitoring;

import org.pf4j.*;

import java.util.*;
import java.io.FileNotFoundException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that boot the application
 * of events monitoring in <strong>thread-arrest</strong>
 * training session
 *
 * @author  Paolo Di Prima
 * @version 1.0
 */
public class Boot {
    private static final int KEY_ROUTING_SIZE = 2;
    private static Logger LOGGER = LoggerFactory.getLogger(Boot.class);

    /***
     * Method that set RabbitMQ configuration and launch the Monitoring-Agent
     *
     * @param args contains routing  keys strings and list of plugins parameters
     */
    public static void main(String[] args) {

        for (String arg : args) LOGGER.info("INPUT >>>>>>>>  " + arg);

        // set broker's uri  and exchange name from config.txt
        try {
            Configuration.init();
        } catch (FileNotFoundException e) { LOGGER.error(e.getMessage()); }
        String  uriRabbitMQ = Configuration.getPropertyValue("uriRabbitMQ");
        String exchangeName = Configuration.getPropertyValue("exchangeName");

        // create instance of sender for rabbitMQ
        String routingKey = Arrays.stream(args).limit(KEY_ROUTING_SIZE).collect(Collectors.joining("."));
        Sender sender = new Sender(uriRabbitMQ, routingKey, exchangeName);
        LOGGER.info("RabbitMQ initialization at" + uriRabbitMQ + " routing key " + routingKey);

        // create the plugin manager
        PluginManager pluginManager = new DefaultPluginManager();

        // get plugin List from arguments in input
        String[] pluginList = new String[args.length - KEY_ROUTING_SIZE];
        System.arraycopy(args, KEY_ROUTING_SIZE, pluginList, 0, pluginList.length);

        // create monitoring agent instance and start monitoring
        MonitoringAgent monitoringAgent = new MonitoringAgent(sender,pluginManager);
        monitoringAgent.startMonitoring(pluginList);

    }

    // second main for plugin test
    public static  void main2(String[] args){
        try {
            Configuration.init();
        } catch (FileNotFoundException e) { LOGGER.error(e.getMessage()); }
        System.out.println(Configuration.getPropertyValue("uriRabbitMQ"));
        System.out.println(Configuration.getPropertyValue("uriGit"));
        System.out.println(Configuration.getPropertyValue("gitUser"));
        System.out.println(Configuration.getPropertyValue("exchangeName"));
    }

}
