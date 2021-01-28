package threatarrest.monitoring;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/***
 * Allows to send a message in Json format to RabbitMQ server
 * <p>
 * An instance is of this  class is initialized with
 * RabbitMQ complete address (&#60;protocol&#62;&#60;user&#62;&#60;passwd&#62;&#60;ip address&#62;)
 */
public class Sender {
    private static Logger LOGGER = LoggerFactory.getLogger(Sender.class);
    public String uriString;
    private String routingKey;
    private String exchangeName;

    public Sender(String uriString, String routingKey, String exchangeName){
        this.uriString = uriString;
        this.routingKey = routingKey;
        this.exchangeName = exchangeName;
    }

    /***
     * This method declares an Exchange for RabbitMQ, declare a queue for RabbitMQ
     * <p>
     * Than, bind the queue with the  exchange using the routing kye given as parameter to the constructor
     * @param msg message to be send to RabbitMQ server
     */
    public void sendMsg(String msg) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(uriString);
        LOGGER.info(" try to sent '" + routingKey + "':'" + msg + "'");
        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchangeName,"topic");
            channel.queueDeclare("queue-" + exchangeName, true, false, false, null);
            channel.queueBind("queue-" + exchangeName, exchangeName, routingKey);
            channel.basicPublish(exchangeName,routingKey,null,msg.getBytes(StandardCharsets.UTF_8));
            LOGGER.info(" [x] sent '" + routingKey + "':'" + msg + "'");
        }
    }
}
