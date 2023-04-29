package mys.serone.mystical.kit;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.kitSystem.PersonalKitManager;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;

/**
 * Class for Implementation of Kit Prefix for a kit that is saved in personal_kit_configuration.yml
 */
public class SetKitPrefix implements CommandExecutor {
    private final Mystical PLUGIN;
    private final PersonalKitManager PERSONAL_KIT_MANAGER;
    private final RanksManager RANKS_MANAGER;
    private final FileConfiguration LANG_CONFIG;

    /**
     * @param plugin : Mystical Plugin
     * @param personalKitManager : Personal Kit Manager used in accessing its functions.
     * @param ranksManager : Personal Kit Manager used in accessing its functions.
     * @param langConfig : langConfig (lang.yml) used for its ENUM messages in MysticalMessage.
     * @see PersonalKitManager
     * @see RanksManager
     * @see MysticalMessage
     */
    public SetKitPrefix(Mystical plugin, PersonalKitManager personalKitManager, RanksManager ranksManager, FileConfiguration langConfig) {
        this.PLUGIN = plugin;
        this.PERSONAL_KIT_MANAGER = personalKitManager;
        this.RANKS_MANAGER = ranksManager;
        this.LANG_CONFIG = langConfig;
    }

    /**
     * @param sender : CommandExecutor
     * @param command : Command Used
     * @param label : Aliases
     * @param args : String List Arguments
     * @return boolean true or false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;

        if (!player.hasPermission(MysticalPermission.SET_KIT_PREFIX.getPermission())) {
            player.sendMessage(MysticalMessage.COMMAND_PERMISSION_ERROR.formatMessage(LANG_CONFIG));
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Usage: /setKitPrefix <rank> <prefix>"), LANG_CONFIG));
            return true;
        }

        String rankKitToGet = args[0];
        Rank rank = RANKS_MANAGER.getRank(rankKitToGet);
        File kitFile = new File(PLUGIN.getDataFolder().getAbsolutePath(), "kits" + File.separator + ".yml");

        if (rank != null) { RANKS_MANAGER.setKitName(player, rankKitToGet, args[1]); }
        else if (kitFile.exists()) { PERSONAL_KIT_MANAGER.setKitPrefix(player, rankKitToGet, args[1]); }
        else { player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Kit does not exist."), LANG_CONFIG)); return true; }

        return true;
    }

}
