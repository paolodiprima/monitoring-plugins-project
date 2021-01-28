package threatarrest.monitoring;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/***
 * Static Class that import a list of configurations properties
 * from configuration files config.txt.
 * Properties (one for each line) are couples key=value
 */
public class Configuration {
    private static Map<String, String> configurationMap = new HashMap<>();

    /***
     * Import properties name value from config.txt
     * @throws FileNotFoundException
     */
    public  static void init() throws FileNotFoundException {
        File config = new File("config.txt");
        Scanner configFile = new Scanner(config);
        String line;
        while (configFile.hasNextLine()) {
            line = configFile.nextLine().trim();
            configurationMap.put(line.split("=")[0],line.split("=")[1]);
        }
    }

    /***
     * Take in input the properties name and return the value
     * @param propertyName name of the property
     * @return correspondent value of propertyName
     */
    public static String getPropertyValue(String propertyName){
        return configurationMap.get(propertyName);
    }

}
