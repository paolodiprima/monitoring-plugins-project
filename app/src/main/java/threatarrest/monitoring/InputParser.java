package threatarrest.monitoring;
import threatarrest.monitoring.exceptions.MissingPluginsInputException;
import threatarrest.monitoring.exceptions.WrongArgumentsFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/***
 * This class allows to check the syntax of plugins list given as input parameters to the application
 */
public class InputParser {
    //regex for arguments list syntax check
    final static String  regex = "([^\\:]{1}([^\\:]*))+:\\[(([^,])*)(,((\\w)+))*\\]$";
    //first check syntax then return list of plugins name

    /***
     * first check syntax of each plugin into the list of plugins
     * then return list of plugins name
     *
     * @param args list of parameters given as input parameters to the jvm
     *
     * @return if the syntax is correct returns the list of plugins
     * @throws MissingPluginsInputException is launched if occurs if the list of parameters is missing
     * @throws WrongArgumentsFormat is launched if there is a syntax error in the list of parameters
     */
    public static String[] parseArgumentList(String[] args) throws MissingPluginsInputException, WrongArgumentsFormat {
        List<String> pluginNames= new ArrayList<>();
        if (args.length == 0) throw new MissingPluginsInputException();
        for (String arg : args) {
            if (!(Pattern.matches(regex, arg))) throw new WrongArgumentsFormat();
            pluginNames.add(getPluginName(arg));
        }
        String[] arrayPlunginNames = new String[pluginNames.size()];
        return pluginNames.toArray(arrayPlunginNames);
    }

    /***
     * return the name of a plugins
     *
     * @param pluginString string the contains all the plugins specification (name:[param_1,param_2,...,param_n]
     * @return plugin name
     */
    public static  String getPluginName(String pluginString) {
        return pluginString.split(":")[0];
    }

    /***
     * return the list of parameters for a single plugin
     *
     * @param pluginString string the contains all the plugins specification (name:[param_1,param_2,...,param_n]
     * @return string that represents a plugin's parameters list
     */
    public static String getParamString(String pluginString) {
        int indexBegParam;
        indexBegParam = pluginString.indexOf(':');
        return pluginString.substring(indexBegParam + 1);
    }

    /***
     * return an array of parameters for a single plugin
     *
     * @param paramString string that represents a plugin's parameters list
     * @return array of string that represents a list of parameters for a single plugin
     */
    public static String[] getParamList(String paramString) {
        return paramString.substring(1, paramString.length()-1).split(",");
    }

}
