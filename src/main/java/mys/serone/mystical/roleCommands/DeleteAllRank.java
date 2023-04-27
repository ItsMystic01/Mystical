package mys.serone.mystical.roleCommands;

import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class DeleteAllRank implements CommandExecutor {

    private final RanksManager RANKS_MANAGER;
    private final FileConfiguration LANG_CONFIG;

    public DeleteAllRank(RanksManager ranksManager, FileConfiguration langConfig) {
        this.RANKS_MANAGER = ranksManager;
        this.LANG_CONFIG = langConfig;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;

        if (!player.hasPermission(MysticalPermission.DELETE_ALL_RANK.getPermission())) {
            player.sendMessage(MysticalMessage.COMMAND_PERMISSION_ERROR.formatMessage(LANG_CONFIG));
            return true;
        }

        RANKS_MANAGER.deleteAllRank();

        player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "All ranks has been deleted successfully."), LANG_CONFIG));

        return true;
    }
}
