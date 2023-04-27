package mys.serone.mystical.kitSystem;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import mys.serone.mystical.functions.MysticalMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersonalKitManager {

    private final File KIT_FILE;
    private final List<PersonalKit> KITS;
    private final FileConfiguration LANG_CONFIG;

    public PersonalKitManager(File kitFile, FileConfiguration langConfig) {
        this.KIT_FILE = kitFile;
        this.LANG_CONFIG = langConfig;
        if (!KIT_FILE.exists()) {
            try {
                boolean created = KIT_FILE.createNewFile();
                if (created) {
                    System.out.println("[Mystical] personal_kit_configuration file created successfully");
                } else {
                    System.out.println("[Mystical] personal_kit_configuration file already exists");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.KITS = loadKitsFromFile();
    }

    private List<PersonalKit> loadKitsFromFile() {
        List<PersonalKit> kits = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            if (KIT_FILE.length() == 0) {
                System.out.println("[Mystical] personal_configuration file is empty.");
                return kits;
            }
            kits = mapper.readValue(KIT_FILE, new TypeReference<List<PersonalKit>>() {});
        } catch (JsonParseException e) {
            System.out.println("[Mystical] personal_configuration file has invalid formatting.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[Mystical] Error loading ranks file.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return kits;
    }

    public List<PersonalKit> getKITS() {
        return KITS;
    }

    public PersonalKit getKit(String name) {
        return KITS.stream()
                .filter(kit -> kit.getKitName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void setKitPrefix(Player player, String kitName, String newPrefix) {
        PersonalKit kitInYML = getKit(kitName);
        kitInYML.setKitCodeName(newPrefix);
        saveKitsToFile();
        player.sendMessage(MysticalMessage.SET_KIT_PREFIX_SUCCESSFUL.formatMessage(Collections.singletonMap("kit", kitName), LANG_CONFIG));
    }

    public void createKit(Player player, String kitName, String kitCodeName) {
        PersonalKit personalKit = new PersonalKit();
        personalKit.setKitName(kitName);
        personalKit.setKitCodeName(kitCodeName);
        KITS.add(personalKit);
        saveKitsToFile();
        player.sendMessage(MysticalMessage.CREATE_KIT_SUCCESSFUL.formatMessage(Collections.singletonMap("kit", kitName), LANG_CONFIG));
    }

    public void deleteKit(PersonalKit kitName) {
        KITS.remove(kitName);
        saveKitsToFile();
    }

    public void saveKitsToFile() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            mapper.writeValue(KIT_FILE, KITS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
