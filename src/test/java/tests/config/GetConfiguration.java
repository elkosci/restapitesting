package tests.config;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class GetConfiguration {
    /**
     * @author elkosci
     *
     */
    public static final String propertyFileName = "configuration.properties";
    private static InputStream inputStream;
    static Map<String, String> configProperties = new HashMap<String, String>();
    public static Map getProperites() throws IOException {
        try {
            Properties prop = new Properties();
            inputStream = new FileInputStream(propertyFileName);
            if (inputStream == null) {
                throw new FileNotFoundException("property file '" + propertyFileName + "' not found!");
            } else {
                prop.load(inputStream);
                Enumeration<String> enums = (Enumeration<String>) prop.propertyNames();
                while (enums.hasMoreElements()) {
                    String key = enums.nextElement();
                    configProperties.put(key, prop.getProperty(key));
                }
            }
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
