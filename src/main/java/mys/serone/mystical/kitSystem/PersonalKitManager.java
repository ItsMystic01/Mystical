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

/**
 * Class responsible for managing kits
 */
public class PersonalKitManager {

    private final File KIT_FILE;
    private final List<PersonalKit> KITS;
    private final FileConfiguration LANG_CONFIG;

    /**
     * @param kitFile : personal_kit_configuration.yml located by the onEnable Function in Mystical Main Class
     * @param langConfig : langConfig (lang.yml) used for its ENUM messages in MysticalMessage.
     * @see mys.serone.mystical.Mystical
     * @see MysticalMessage
     */
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

    /**
     * @return List<PersonalKit> : All personal kits in personal_kit_configuration.yml
     */
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

    /**
     * @return List of PersonalKit : All personal kits in personal_kit_configuration.yml
     */
    public List<PersonalKit> getKITS() {
        return KITS;
    }

    /**
     * @param name : name of the kit placed in the parameter by the CommandExecutor.
     * @return PersonalKit
     */
    public PersonalKit getKit(String name) {
        return KITS.stream()
                .filter(kit -> kit.getKitName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * @param player : CommandExecutor
     * @param kitName : Kit Name to be updated
     * @param newPrefix : Prefix to be set for the kit
     */
    public void setKitPrefix(Player player, String kitName, String newPrefix) {
        PersonalKit kitInYML = getKit(kitName);
        kitInYML.setKitCodeName(newPrefix);
        saveKitsToFile();
        player.sendMessage(MysticalMessage.SET_KIT_PREFIX_SUCCESSFUL.formatMessage(Collections.singletonMap("kit", kitName), LANG_CONFIG));
    }

    /**
     * @param player : CommandExecutor
     * @param kitName : Kit name to be created
     * @param kitCodeName : Prefix to be set for the kit
     */
    public void createKit(Player player, String kitName, String kitCodeName) {
        PersonalKit personalKit = new PersonalKit();
        personalKit.setKitName(kitName);
        personalKit.setKitCodeName(kitCodeName);
        KITS.add(personalKit);
        saveKitsToFile();
        player.sendMessage(MysticalMessage.CREATE_KIT_SUCCESSFUL.formatMessage(Collections.singletonMap("kit", kitName), LANG_CONFIG));
    }

    /**
     * @param kitName : Name of the kit to delete from personal_kit_configuration.yml
     */
    public void deleteKit(PersonalKit kitName) {
        KITS.remove(kitName);
        saveKitsToFile();
    }

    /**
     * Saves all changes in personal_kit_configuration.yml
     */
    public void saveKitsToFile() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            mapper.writeValue(KIT_FILE, KITS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
