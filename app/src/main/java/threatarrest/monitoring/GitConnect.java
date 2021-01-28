package threatarrest.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/***
 * Allows to download a list of plugins from a Git Server
 */
public class GitConnect implements IPluginsDownloader {
    private Logger LOGGER = LoggerFactory.getLogger(GitConnect.class);
    private  String uriGit;

    public GitConnect(){
        uriGit = Configuration.getPropertyValue("uriGit");
    }

    /***
     * Download from the git source address the  list of plugins requested
     *
     * @param source complete address of git repository (protocol,git user, ip, path to repository)
     * @param listPlugin list of plugins request, each plugin have a syntax name:[param_1,param_2,...,param_n]
     */
    public  void downloadPlugins(String source, String... listPlugin)  {

        String url;
        for (String pluginFileName : listPlugin) {
            pluginFileName = pluginFileName + "-1.0-all.jar";
            launchShellDownloadCmd(pluginFileName);
        }
    }

    /***
     * Download a single plugin file using the git shell command  git archive
     *
     * @param pluginName name of the plugin file .jar
     */
    private void launchShellDownloadCmd(String pluginName){
        ProcessBuilder downloadProcessBuilder = null;

        String operatingSystem = System.getProperty("os.name");
        if (operatingSystem.toLowerCase().contains("windows")) {
            String winCmd = String.format("git archive --remote=%s  HEAD %s --output=.\\plugins\\temp && tar -xf .\\plugins\\temp -C .\\plugins && del .\\plugins\\temp",uriGit,pluginName);
            downloadProcessBuilder =  new ProcessBuilder("cmd","/c",winCmd);
            LOGGER.info("executing cmd >> " + winCmd);
        }
        else {
            String linuxCmd = String.format("git archive --remote=%s HEAD %s | tar -xO > plugins/%s",uriGit,pluginName,pluginName);
            //System.out.println(linuxCmd);
            downloadProcessBuilder =  new ProcessBuilder("/bin/bash","-c",linuxCmd);
            LOGGER.info("executing cmd >> " + linuxCmd);
        }
        try {
            Process downloadProcess = downloadProcessBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(downloadProcess.getInputStream()));
            String line;
            while (( line = reader.readLine() ) != null)
                LOGGER.info("git std output : "+ line);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}

