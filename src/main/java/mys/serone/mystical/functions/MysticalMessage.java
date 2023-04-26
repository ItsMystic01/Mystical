package mys.serone.mystical.functions;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MysticalMessage {

    public enum messageENUM {
        INFORMATION("&3[&bMystical&3] &l| &r{message}", true),
        COMMAND_PERMISSION_ERROR("&3You do not have the permission to perform this command.", false),
        CREATE_KIT_CONFIGURATION_ERROR("&3[&bMystical&3] &l| &rAn error occurred while saving the kit.", false),
        CREATE_KIT_SUCCESSFUL("&3[&bMystical&3] &l| &r{kit} kit has been created successfully.", true),
        DELETE_KIT_RANK_DOES_NOT_EXIST_ERROR("&3[&bMystical&3] &l| &r{kit} rank kit does not exist.", true),
        DELETE_KIT_SUCCESSFUL("&3[&bMystical&3] &l| &r{kit} kit has been deleted successfully.", true),
        DELETE_KIT_UNSUCCESSFUL("&3[&bMystical&3] &l| &r{kit} kit has been deleted unsuccessfully.", true),
        EDIT_KIT_SUCCESSFUL("&3[&bMystical&3] &l| &aKit saved successfully.", false),
        CLAIM_KIT_SUCCESSFUL("&3[&bMystical&3] &l| &rYou have claimed {kit} kit.", true),
        SET_KIT_PREFIX_SUCCESSFUL("&3[&bMystical&3] &l| &rPrefix has been set successfully.", false),
        USER_INVALID_RANK_CONFIGURATION_ERROR("&3[&bMystical&3] &l| &r{player} has invalid ranks on player_info.yml", true),
        RANK_HAS_NO_EXISTING_KIT_CONFIGURATION_ERROR("&3[&bMystical&3] &l| &r{kit} does not have an existing kit. " +
                "(Read the README.txt file in the Mystical Folder in the Plugins Folder)", true);

        private final String message;
        private final boolean hasPlaceholders;

        messageENUM(String message, boolean hasPlaceholders) {
            this.message = message;
            this.hasPlaceholders = hasPlaceholders;
        }

        public String formatMessage(String... values) {
            if (!hasPlaceholders) {
                return ChatColor.translateAlternateColorCodes('&', message);
            }

            List<String> listOfPlaceHolders = new ArrayList<>();

            Pattern pattern = Pattern.compile("\\{([^{}]+)}");
            Matcher matcher = pattern.matcher(message);

            while (matcher.find()) {
                String placeholder = matcher.group(1);
                listOfPlaceHolders.add(placeholder);
            }

            String newMessage = null;
            for (int i = 0; i < values.length; i++) {
                newMessage = message.replace("{" + listOfPlaceHolders.get(i) + "}", values[i]);
            }

            assert newMessage != null;
            return ChatColor.translateAlternateColorCodes('&', newMessage);
        }
    }

}
