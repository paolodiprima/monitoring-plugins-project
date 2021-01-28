package threatarrest.monitoring;

import org.pf4j.ExtensionPoint;

/***
 * Interface ExtensionsPoint used by framework pf4j
 * in order to create plugins
 */

public interface IExtensionPoint extends ExtensionPoint {
    /***
     * Execute the monitoring of a particular action
     * in the context of the threat-arrest training session
     *
     * @param args list or arguments needed by the plugin  to perform the monitoring
     * @return an instance of the class message once the event monitored occurs
     */
    Message run(String...args);

}
