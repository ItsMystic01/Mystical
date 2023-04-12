package mys.serone.mystical.commands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.kitSystem.KitManager;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.Objects;

public class Kit implements CommandExecutor {
    private final Mystical PLUGIN;
    private final KitManager KIT_MANAGER;
    private final RanksManager RANKS_MANAGER;
    private final FileConfiguration LANG_FILE;

    public Kit(Mystical plugin, KitManager kitManager, RanksManager ranksManager, FileConfiguration langFile) {
        this.PLUGIN = plugin;
        this.KIT_MANAGER = kitManager;
        this.RANKS_MANAGER = ranksManager;
        this.LANG_FILE = langFile;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;
        String langMessage = LANG_FILE.getString("information");
        String langPermissionMessage = LANG_FILE.getString("command_permission_error");

        if (!sender.hasPermission(MysticalPermission.permissionENUM.KIT.getPermission())) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                Objects.requireNonNull(langPermissionMessage))); return true; }
        if (args.length < 1) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "Usage: /kit <rank>"))); return true;}

        String rankKitToGet = args[0];
        Rank rank = RANKS_MANAGER.getRank(rankKitToGet);
        File kitFile = new File(PLUGIN.getDataFolder().getAbsolutePath(), "kits/" + rankKitToGet + ".yml");

        if (rank != null) { KIT_MANAGER.kitOne(player, args); }
        else if (kitFile.exists()) { KIT_MANAGER.claimKit(player, rankKitToGet); }
        else { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "Kit does not exist."))); return true; }

        return true;
    }
}
