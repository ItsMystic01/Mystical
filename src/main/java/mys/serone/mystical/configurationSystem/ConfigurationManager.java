package mys.serone.mystical.configurationSystem;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationManager {
    private final List<Configuration> CONFIG_INFO;
    private final File CONFIGURATION_FILE;

    public ConfigurationManager(File configurationFile) {
        this.CONFIGURATION_FILE = configurationFile;
        if (!CONFIGURATION_FILE.exists()) {
            try {
                boolean created = CONFIGURATION_FILE.createNewFile();
                if (created) {
                    System.out.println("[Mystical] mystical_configuration file created successfully");
                } else {
                    System.out.println("[Mystical] mystical_configuration file already exists");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.CONFIG_INFO = loadPlayerInfoFromFile();
    }

    private List<Configuration> loadPlayerInfoFromFile() {
        List<Configuration> configurationInfo = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            if (CONFIGURATION_FILE.length() == 0) {
                System.out.println("[Mystical] mystical_configuration file is empty.");
                return configurationInfo;
            }
            configurationInfo = mapper.readValue(CONFIGURATION_FILE, new TypeReference<List<Configuration>>() {});
        } catch (JsonParseException e) {
            System.out.println("[Mystical] mystical_configuration file has invalid formatting.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[Mystical] Error loading mystical_configuration file.");
            e.printStackTrace();
        }
        return configurationInfo;
    }

    public List<Configuration> getAllConfiguration() {
        return CONFIG_INFO;
    }

    public void createConfigurationInfo(Configuration configurationInfo) {
        CONFIG_INFO.add(configurationInfo);
        saveConfigurationInfoToFile();
    }

    public void saveConfigurationInfoToFile() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            mapper.writeValue(CONFIGURATION_FILE, CONFIG_INFO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

