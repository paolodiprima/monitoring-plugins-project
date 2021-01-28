package threatarrest.monitoring;

import com.google.gson.GsonBuilder;
import threatarrest.monitoring.exceptions.MissingPluginsInputException;
import threatarrest.monitoring.exceptions.WrongArgumentsFormat;
import org.pf4j.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/***
 * This is the class that run the monitoring of a specified threat-arrest training session
 * <p>
 * Run in sequence the plugin given in the  plugin list. When a plugin returns
 * a result, it is send to RabbitMQ by an instance of the class Sender
 *
 */
public class MonitoringAgent {
    private static Logger LOGGER = LoggerFactory.getLogger(MonitoringAgent.class);
    private Sender sender;
    private PluginManager pluginManager;

    public MonitoringAgent(Sender sender, PluginManager pluginManager) {
        this.sender = sender;
        this.pluginManager = pluginManager;
    }

    /***
     * Start a loop that follow the same order of the plugin list.
     * First download all the plugins file into  /plugins folder
     * Than load start and stop each plugin
     *
     * @param pluginList list of plugin to load start and stop
     */
    void startMonitoring(String...pluginList) {
        Message msg = null;
        //Gson gson = new Gson();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        IPluginsDownloader gitConnect = new GitConnect();
        try {
            String[] pluginNames = InputParser.parseArgumentList(pluginList);
            gitConnect.downloadPlugins("plugins", pluginNames);
            pluginManager.loadPlugins();

            // main loop that load and start the sequence of plugins
            List<IExtensionPoint> extensions;
            String pluginName, pluginArguments;
            for (int i = 0; i < pluginList.length; i++) {
                pluginName = InputParser.getPluginName(pluginList[i]);
                pluginArguments = InputParser.getParamString(pluginList[i]);

                pluginManager.startPlugin(pluginName + "-id");
                extensions = pluginManager.getExtensions(IExtensionPoint.class,
                        InputParser.getPluginName(pluginName + "-id"));

                // log parameters sent to run method
                for (String arg : InputParser.getParamList(pluginArguments))
                    LOGGER.info(pluginName + " parameter: " + arg);

                msg = extensions.get(0).run(InputParser.getParamList(pluginArguments));
                msg.setPluginName(pluginName);
                LOGGER.info("OUTPUT PLUG-IN " + pluginName + " >>>  " + msg);
                LOGGER.info("STRING SENT TO RABBITMQ" + gson.toJson(msg));
                sender.sendMsg(gson.toJson(msg));
                pluginManager.stopPlugin(pluginName + "-id");
            }

        } catch (MissingPluginsInputException | WrongArgumentsFormat | IOException | TimeoutException | NoSuchAlgorithmException | KeyManagementException | URISyntaxException e) {
            LOGGER.error("FROM MONITORING EXCEPTION "+ e.getMessage());
        }
    }
}
