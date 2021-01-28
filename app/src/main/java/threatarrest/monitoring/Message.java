package threatarrest.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.net.InetAddress;

/***
 * An instance of Message class represents a message that a plugin returns
 * when  a particular events occurs
 */
public class Message {
    private String hostName;
    private String pluginName;
    private Date timestamp;
    private String msg;
    private double executionTime;
    private static Logger LOGGER = LoggerFactory.getLogger(Boot.class);

    /***
     * constructor that  initialise the timestamp and the hostname of the machine sending the message
     */
    public Message() {
        timestamp = new Date();
        try {
            InetAddress myHost = InetAddress.getLocalHost();
            hostName = myHost.getHostName();
        } catch (Exception e){
            LOGGER.info("Message class constructor : " + e.getMessage());
        }

    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public double getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(double executionTime) {
        this.executionTime = executionTime;
    }
}