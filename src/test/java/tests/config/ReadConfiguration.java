package tests.config;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ReadConfiguration {
    /**
     * @author elkosci
     *
     */
    private static String propFileName = "configuration.properties";
    private static InputStream inputStream;
    static Map<String, String> configProperties = new HashMap<String, String>();
//    public static void main(String[] args) throws IOException {
//        getPropValues();
//    }
    public static Map getPropValues() throws IOException {
        try {
            Properties prop = new Properties();
            inputStream = new FileInputStream(propFileName);
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found!");
            }
            // get the property value and print it out
//            System.out.println("baseUrl: " + prop.getProperty("baseUrl"));
            configProperties.put("baseUrl", prop.getProperty("baseUrl"));
//            System.out.println("logFile: " + prop.getProperty("logFile"));
            configProperties.put("logFile", prop.getProperty("logFile"));
        } catch (IOException e) {
            System.out.println("FileNotFoundException thrown: " + e);
        }
        catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return configProperties;
    }
}
