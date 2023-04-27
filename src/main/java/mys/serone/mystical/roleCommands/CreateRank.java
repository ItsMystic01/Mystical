package mys.serone.mystical.roleCommands;

import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.handlers.RankConfigurationHandler;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CreateRank implements CommandExecutor {

    private final RanksManager RANKS_MANAGER;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final FileConfiguration LANG_CONFIG;

    public CreateRank(RanksManager ranksManager, PlayerInfoManager playerInfoManager, FileConfiguration langConfig) {
        this.RANKS_MANAGER = ranksManager;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
        this.LANG_CONFIG = langConfig;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;

        if (!player.hasPermission(MysticalPermission.CREATE_RANK.getPermission())) {
            player.sendMessage(MysticalMessage.COMMAND_PERMISSION_ERROR.formatMessage(LANG_CONFIG));
            return true;
        }
        if (args.length < 4) {
            player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "/createRank <rank name> <prefix> <priority> <permission...>"), LANG_CONFIG));
            return true;
        }

        List<String> finalizedArguments = new ArrayList<>();

        Collections.addAll(finalizedArguments, Arrays.copyOfRange(args, 3, args.length));

        Rank rankChecker = RANKS_MANAGER.getRank(args[0]);

        if (rankChecker != null) {
            player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Rank already exists"), LANG_CONFIG));
            return true;
        }

        RANKS_MANAGER.createRank(UUID.randomUUID(), args[0], args[1], Integer.parseInt(args[2]), finalizedArguments, null, null);
        player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", args[0] + " Rank has been created successfully."), LANG_CONFIG));

        new RankConfigurationHandler(RANKS_MANAGER, PLAYER_INFO_MANAGER);
        return true;
    }
}
