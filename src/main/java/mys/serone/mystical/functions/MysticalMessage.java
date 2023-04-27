package mys.serone.mystical.functions;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;
import java.util.Objects;

public enum MysticalMessage {

    INFORMATION("&3[&bMystical&3] &l| &r{message}", "information", true),
    COMMAND_PERMISSION_ERROR("&3[&bMystical&3] &l| &3You do not have the permission to perform this command.", "command_permission_error", false),
    CREATE_KIT_CONFIGURATION_ERROR("&3[&bMystical&3] &l| &rAn error occurred while saving the kit.", "create_kit_configuration_error", false),
    CREATE_KIT_SUCCESSFUL("&3[&bMystical&3] &l| &r{kit} kit has been created successfully.", "create_kit_successful", true),
    DELETE_KIT_RANK_DOES_NOT_EXIST_ERROR("&3[&bMystical&3] &l| &r{kit} rank kit does not exist.", "delete_kit_rank_does_not_exist_error", true),
    DELETE_KIT_SUCCESSFUL("&3[&bMystical&3] &l| &r{kit} kit has been deleted successfully.", "delete_kit_successful", true),
    DELETE_KIT_UNSUCCESSFUL("&3[&bMystical&3] &l| &r{kit} kit has been deleted unsuccessfully.", "delete_kit_unsuccessful", true),
    EDIT_KIT_SUCCESSFUL("&3[&bMystical&3] &l| &a{kit} kit saved successfully.", "edit_kit_successful", false),
    CLAIM_KIT_SUCCESSFUL("&3[&bMystical&3] &l| &rYou have claimed {kit} kit.", "claim_kit_successful", true),
    SET_KIT_PREFIX_SUCCESSFUL("&3[&bMystical&3] &l| &rPrefix for {kit} has been set successfully.", "set_kit_prefix_successful", false),
    USER_INVALID_RANK_CONFIGURATION_ERROR("&3[&bMystical&3] &l| &r{player} has invalid ranks on player_info.yml", "user_invalid_rank_configuration_error", true),
    RANK_HAS_NO_EXISTING_KIT_CONFIGURATION_ERROR("&3[&bMystical&3] &l| &r{kit} does not have an existing kit. " +
            "(Read the README.txt file in the Mystical Folder in the Plugins Folder)", "rank_has_no_existing_kit_configuration_error", true);

    private final String MESSAGE;
    private final String ENUM_NAME;
    private final boolean HAS_PLACEHOLDERS;
    private final String PLUGIN_PREFIX = "&3[&bMystical&3] &l| &r";

    MysticalMessage(String message, String enumName, boolean hasPlaceholders) {
        this.MESSAGE = message;
        this.ENUM_NAME = enumName;
        this.HAS_PLACEHOLDERS = hasPlaceholders;
    }

    public String formatMessage(Map<String, String> placeholderValues, FileConfiguration langFile) {
        String newMessage;
        if (langFile.get(ENUM_NAME) != null && !Objects.equals(langFile.get(ENUM_NAME), "")) {
            newMessage = Objects.requireNonNull(langFile.get(ENUM_NAME)).toString();
            if (!HAS_PLACEHOLDERS) {
                return ChatColor.translateAlternateColorCodes('&', MESSAGE);
            }

            for (Map.Entry<String, String> entry : placeholderValues.entrySet()) {
                String placeholder = entry.getKey();
                String value = entry.getValue();
                newMessage = newMessage.replace("{" + placeholder + "}", value);
            }

        } else {
            newMessage = MESSAGE;
            for (Map.Entry<String, String> entry : placeholderValues.entrySet()) {
                String placeholder = entry.getKey();
                String value = entry.getValue();
                newMessage = newMessage.replace("{" + placeholder + "}", value);
            }
            return ChatColor.translateAlternateColorCodes('&', newMessage);
        }
        return ChatColor.translateAlternateColorCodes('&', PLUGIN_PREFIX + newMessage);
    }

    public String formatMessage(FileConfiguration langFile) {
        if (langFile.get(ENUM_NAME) != null && !Objects.equals(langFile.get(ENUM_NAME), "")) {
            String commandPermissionError = Objects.requireNonNull(langFile.get(ENUM_NAME)).toString();
            return ChatColor.translateAlternateColorCodes('&', PLUGIN_PREFIX + commandPermissionError);
        }
        return ChatColor.translateAlternateColorCodes('&', PLUGIN_PREFIX + MESSAGE);
    }
}
