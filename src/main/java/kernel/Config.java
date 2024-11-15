package kernel;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private static final Map<ConfigKeys, String> VALUES = new HashMap<>();

    private Config() {
    }

    public static void loadConfig() throws Exception {
        try (BufferedReader reader = Files.newBufferedReader(Path.of("msp_config.txt"))) {
            String thisLine = null;
            while ((thisLine = reader.readLine()) != null) {
                int separatorIndex = thisLine.indexOf('=');
                if (separatorIndex <= 0) {
                    throw new IOException("No '=' separator found for line: " + thisLine);
                }
                String key = thisLine.substring(0, separatorIndex).trim();
                String value = thisLine.substring(separatorIndex + 1).trim();
                ConfigKeys configKey;
                try {
                    configKey = ConfigKeys.valueOf(key);
                } catch (IllegalArgumentException e) {
                    throw new IOException("ConfigKey invalid (does not exist): " + key);
                }
                VALUES.put(configKey, value);
            }
        }
        for (ConfigKeys configKey : ConfigKeys.values()) {
            if (!VALUES.containsKey(configKey)) {
                throw new IOException("ConfigKey must exist but is missing on your configuration file: " + configKey.toString());
            }
        }
    }

    public static String getValue(ConfigKeys configKey) {
        return VALUES.get(configKey);
    }
}