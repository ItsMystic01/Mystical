package mys.serone.mystical.kit;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.kitSystem.PersonalKitManager;
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

public class SetKitPrefix implements CommandExecutor {
    private final Mystical PLUGIN;
    private final PersonalKitManager PERSONAL_KIT_MANAGER;
    private final RanksManager RANKS_MANAGER;
    private final FileConfiguration LANG_FILE;

    public SetKitPrefix(Mystical plugin, PersonalKitManager personalKitManager, RanksManager ranksManager, FileConfiguration langFile) {
        this.PLUGIN = plugin;
        this.PERSONAL_KIT_MANAGER = personalKitManager;
        this.RANKS_MANAGER = ranksManager;
        this.LANG_FILE = langFile;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;
        String langMessage = LANG_FILE.getString("information");
        String langPermissionMessage = LANG_FILE.getString("command_permission_error");

        if (!player.hasPermission(MysticalPermission.permissionENUM.SET_KIT_PREFIX.getPermission())) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                Objects.requireNonNull(langPermissionMessage))); return true; }
        if (args.length < 2) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "Usage: /setKitPrefix <rank> <prefix>"))); return true;}

        String rankKitToGet = args[0];
        Rank rank = RANKS_MANAGER.getRank(rankKitToGet);
        File kitFile = new File(PLUGIN.getDataFolder().getAbsolutePath(), "kits/" + rankKitToGet + ".yml");

        if (rank != null) { RANKS_MANAGER.setKitName(player, rankKitToGet, args[1]); }
        else if (kitFile.exists()) { PERSONAL_KIT_MANAGER.setKitPrefix(player, rankKitToGet, args[1]); }
        else { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                String.format(Objects.requireNonNull(langMessage), "Kit does not exist."))); return true; }

        return true;
    }

}
