package threatarrest.monitoring.dbpasswdmonitoring;


import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimerTask;

/***
 * Allows to execute the monitoring action: connection to the root user
 * of MySQL server with default/weak password
 */
public abstract class AbPasswdMysqlWatcher extends TimerTask {
    Logger logger;
    private final String url;
    private final String user;
    private final String passwd;


    public AbPasswdMysqlWatcher(String url, String user, String passwd, Logger logger) {
        super();
        this.url = url;
        this.user = user;
        this.passwd = passwd;
        this.logger = logger;
    }

    @Override
    public void run() {
        Boolean accessDenied = false;
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, passwd);
            logger.info("connected to db...");
            //System.out.println(" connected..");
            connection.close();
        } catch (SQLException | ClassNotFoundException  e) {
            if (e.getMessage().contains("Access denied for user 'root'@'localhost'")) {
                accessDenied= true;
            } else System.out.println("Different Exception" + e.getCause());
        }
        if (connection == null && accessDenied)
            onChange();
    }

    protected abstract void onChange();

    public String getUrl() {
        return url;
    }


}
