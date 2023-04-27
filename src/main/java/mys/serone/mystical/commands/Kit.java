package mys.serone.mystical.commands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.kitSystem.KitManager;
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

public class Kit implements CommandExecutor {
    private final Mystical PLUGIN;
    private final KitManager KIT_MANAGER;
    private final RanksManager RANKS_MANAGER;
    private final FileConfiguration LANG_CONFIG;

    public Kit(Mystical plugin, KitManager kitManager, RanksManager ranksManager, FileConfiguration langConfig) {
        this.PLUGIN = plugin;
        this.KIT_MANAGER = kitManager;
        this.RANKS_MANAGER = ranksManager;
        LANG_CONFIG = langConfig;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;

        if (!sender.hasPermission(MysticalPermission.KIT.getPermission())) { player.sendMessage(
                MysticalMessage.COMMAND_PERMISSION_ERROR.formatMessage(LANG_CONFIG)); return true; }
        if (args.length < 1) {
            player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Usage: /kit <rank>"), LANG_CONFIG));
            return true;
        }

        String rankKitToGet = args[0];
        Rank rank = RANKS_MANAGER.getRank(rankKitToGet);
        File kitFile = new File(PLUGIN.getDataFolder().getAbsolutePath(), "kits"  + File.separator + rankKitToGet + ".yml");

        if (rank != null) { KIT_MANAGER.kitOne(player, args); }
        else if (kitFile.exists()) { KIT_MANAGER.claimKit(player, rankKitToGet); }
        else {
            player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Kit does not exist."), LANG_CONFIG));
            return true;
        }

        return true;
    }
}
