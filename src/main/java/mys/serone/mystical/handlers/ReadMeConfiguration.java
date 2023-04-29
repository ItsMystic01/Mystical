package mys.serone.mystical.handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class for creating a README.txt file in the Mystical Plugin Data Folder
 */
public class ReadMeConfiguration {

    private final File READ_ME_FILE;

    /**
     * @param readMeFile : README.txt located by the onEnable Function in Mystical Main Class
     * @see mys.serone.mystical.Mystical
     */
    public ReadMeConfiguration(File readMeFile) { this.READ_ME_FILE = readMeFile; }

    /**
     * Writes the README.txt file in plugin's folder
     */
    public void writeToFile() {

        String[] messageList = {
                "================================================",
                "ranks.yml (Sample)",
                "- name: \"Rank Name\"",
                "  prefix: \"&c[&fRank Prefix&c]\"",
                "  priority: 1",
                "  permissions:",
                "  - \"mystical.kill\"",
                "  - \"mystical.op\"",
                "  isDefault: false",
                "  kit:",
                "    - netherite_helmet:",
                "      - \"protection:4\"",
                "      - \"unbreaking:3\"",
                "    - netherite_chestplate:",
                "      - \"protection:4\"",
                "      - \"unbreaking:3\"",
                "    - netherite_leggings:",
                "      - \"protection:4\"",
                "      - \"unbreaking:3\"",
                "    - netherite_boots:",
                "      - \"protection:4\"",
                "      - \"unbreaking:3\"",
                "    - netherite_pickaxe:",
                "      - \"efficiency:5\"",
                "  kitName: \"&4[&cAdmin&4]\"",
                " ",
                "* The lower the number on the priority, the higher it is in the tier list.",
                "* permissions per rank are automatically applied to every users that has the rank. You can add permissions from other plugins in the permission list for it will be applied as well.",
                "* kit item must be minecraft format, as well as its enchantments.",
                "* kit name is just like the prefix for a rank.",
                " ",
                "================================================",
                "player_info.yml (Sample)",
                " ",
                "853b37d5-591e-4546-a7f4-d23dc84122d8:",
                "  userRankList:",
                "  - \"Admin\"",
                "  - \"Moderator\"",
                "  - \"Member\"",
                "  userAdditionalPermission: ",
                "  - mystical.*",
                " ",
                "* the 853b37d5-591e-4546-a7f4-d23dc84122d8 represents the player's UUID",
                "* userRankList is list of ranks, it will sort the rank list by priority set in the ranks.yml, every server restart or reload.",
                "* userAdditionalPermission is list of permissions that would be given to the user upon joining.",
                " ",
                "================================================",
                "mystical_configuration (Sample)",
                " ",
                "- defaultRank: \"Member\"",
                " ",
                "* mystical_configuration only requires 2 configuration as of now.",
                "* defaultRank is the rank to be given to every player that joins on their first time. The rank must be present in the ranks.yml for it to be given properly and the player's rank won't be null.",
        };

        try {
            if (READ_ME_FILE.length() == 0) {
                FileWriter writer = new FileWriter(READ_ME_FILE, true);
                for (String m : messageList) {
                    writer.write(m);
                    writer.write(System.lineSeparator());
                }
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
