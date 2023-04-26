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
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class SetKitPrefix implements CommandExecutor {
    private final Mystical PLUGIN;
    private final PersonalKitManager PERSONAL_KIT_MANAGER;
    private final RanksManager RANKS_MANAGER;

    public SetKitPrefix(Mystical plugin, PersonalKitManager personalKitManager, RanksManager ranksManager) {
        this.PLUGIN = plugin;
        this.PERSONAL_KIT_MANAGER = personalKitManager;
        this.RANKS_MANAGER = ranksManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;

        if (!player.hasPermission(MysticalPermission.permissionENUM.SET_KIT_PREFIX.getPermission())) {
            player.sendMessage(MysticalMessage.messageENUM.COMMAND_PERMISSION_ERROR.formatMessage());
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(MysticalMessage.messageENUM.INFORMATION.formatMessage("Usage: /setKitPrefix <rank> <prefix>"));
            return true;
        }

        String rankKitToGet = args[0];
        Rank rank = RANKS_MANAGER.getRank(rankKitToGet);
        File kitFile = new File(PLUGIN.getDataFolder().getAbsolutePath(), "kits/" + rankKitToGet + ".yml");

        if (rank != null) { RANKS_MANAGER.setKitName(player, rankKitToGet, args[1]); }
        else if (kitFile.exists()) { PERSONAL_KIT_MANAGER.setKitPrefix(player, rankKitToGet, args[1]); }
        else { player.sendMessage(MysticalMessage.messageENUM.INFORMATION.formatMessage("Kit does not exist.")); return true; }

        return true;
    }

}
