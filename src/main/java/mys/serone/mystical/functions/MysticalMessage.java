package mys.serone.mystical.functions;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;
import java.util.Objects;

/**
 * ENUM for Default Messages for every function, and functions for every function available in the lang.yml file
 */
public enum MysticalMessage {

    /**
     * General Message Format
     */
    INFORMATION("&3[&bMystical&3] &l| &r{message}", "information", true),
    /**
     * Insufficient Permission Message Format
     */
    COMMAND_PERMISSION_ERROR("&3[&bMystical&3] &l| &3You do not have the permission to perform this command.", "command_permission_error", false),
    /**
     * Kit Creation Error Message Format
     */
    CREATE_KIT_CONFIGURATION_ERROR("&3[&bMystical&3] &l| &rAn error occurred while saving the kit.", "create_kit_configuration_error", false),
    /**
     * Kit Creation Successful Message Format
     */
    CREATE_KIT_SUCCESSFUL("&3[&bMystical&3] &l| &r{kit} &rkit has been created successfully.", "create_kit_successful", true),
    /**
     * Kit Deletion Error Message Format
     */
    DELETE_KIT_RANK_DOES_NOT_EXIST_ERROR("&3[&bMystical&3] &l| &r{kit} &rrank kit does not exist.", "delete_kit_rank_does_not_exist_error", true),
    /**
     * Kit Deletion Successful Message Format
     */
    DELETE_KIT_SUCCESSFUL("&3[&bMystical&3] &l| &r{kit} &rkit has been deleted successfully.", "delete_kit_successful", true),
    /**
     * Kit Deletion Unsuccessful Message Format
     */
    DELETE_KIT_UNSUCCESSFUL("&3[&bMystical&3] &l| &r{kit} &rkit has been deleted unsuccessfully.", "delete_kit_unsuccessful", true),
    /**
     * Kit Modification Successful Message Format
     */
    EDIT_KIT_SUCCESSFUL("&3[&bMystical&3] &l| &a{kit} &rkit saved successfully.", "edit_kit_successful", true),
    /**
     * Kit Claim Successful Message Format
     */
    CLAIM_KIT_SUCCESSFUL("&3[&bMystical&3] &l| &rYou have claimed &r{kit} &rkit.", "claim_kit_successful", true),
    /**
     * Kit Prefix Update Successful Message Format
     */
    SET_KIT_PREFIX_SUCCESSFUL("&3[&bMystical&3] &l| &rPrefix for {kit} &rhas been set successfully.", "set_kit_prefix_successful", true),
    /**
     * Player Invalid Rank Configuration Error Message Format
     */
    USER_INVALID_RANK_CONFIGURATION_ERROR("&3[&bMystical&3] &l| &r{player} &rhas invalid ranks on player_info.yml", "user_invalid_rank_configuration_error", true),
    /**
     * No Existing Kit For Rank Message Format
     */
    RANK_HAS_NO_EXISTING_KIT_CONFIGURATION_ERROR("&3[&bMystical&3] &l| &r{rank} &rdoes not have an existing kit. " +
            "(Read the README.txt file in the Mystical Folder in the Plugins Folder)", "rank_has_no_existing_kit_configuration_error", true);

    private final String MESSAGE;
    private final String ENUM_NAME;
    private final boolean HAS_PLACEHOLDERS;
    private final String PLUGIN_PREFIX = "&3[&bMystical&3] &l| &r";

    /**
     * @param message : Message to send for a certain function
     * @param enumName : Name of the function the message is used for
     * @param hasPlaceholders : Indicates if the message contains a placeholder
     */
    MysticalMessage(String message, String enumName, boolean hasPlaceholders) {
        this.MESSAGE = message;
        this.ENUM_NAME = enumName;
        this.HAS_PLACEHOLDERS = hasPlaceholders;
    }

    /**
     * @param placeholderValues : Placeholders with its designated replacement
     * @param langFile : langFile (lang.yml) used for its ENUM messages in MysticalMessage
     * @return String : Formatted String where all available and valid placeholders are replaced, and colors are implemented
     */
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

    /**
     * @param langFile : langFile (lang.yml) used for its ENUM messages in MysticalMessage
     * @return String : Formatted String where no placeholder is required to be replaced, just implementing colors
     */
    public String formatMessage(FileConfiguration langFile) {
        if (langFile.get(ENUM_NAME) != null && !Objects.equals(langFile.get(ENUM_NAME), "")) {
            String commandPermissionError = Objects.requireNonNull(langFile.get(ENUM_NAME)).toString();
            return ChatColor.translateAlternateColorCodes('&', PLUGIN_PREFIX + commandPermissionError);
        }
        return ChatColor.translateAlternateColorCodes('&', PLUGIN_PREFIX + MESSAGE);
    }
}
