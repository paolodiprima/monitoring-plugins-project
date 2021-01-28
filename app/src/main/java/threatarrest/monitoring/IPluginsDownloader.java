package threatarrest.monitoring;

import org.slf4j.Logger;

/***
 * Represents the structure of a class that
 * allows to download the .jar plugins files
 */
public interface IPluginsDownloader {
    Logger LOGGER = null;
    String urlRoot = null;

    /***
     * Download from a source of no specified server type,(git, gitHub, ftp) the  list of plugins files requested
     * @param source source address
     * @param listPlugin list of plugin files to download
     */
    public  void downloadPlugins(String source, String... listPlugin);

}
