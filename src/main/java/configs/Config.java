package configs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// читаем config.properties один раз.
// Дополнение: значение из параметра запуска (-Dserver=...) важнее файла — так CI может подменить адрес.
public class Config {
    private static final Config INSTANCE = new Config();
    private final Properties properties = new Properties();

    private Config() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found in resources");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Fail to load config.properties", e);
        }
    }

    public static String getProperty(String key) {
        String fromCommandLine = System.getProperty(key);
        if (fromCommandLine != null) {
            return fromCommandLine;
        }
        return INSTANCE.properties.getProperty(key);
    }
}
