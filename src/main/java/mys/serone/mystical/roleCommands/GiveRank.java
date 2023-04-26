package mys.serone.mystical.roleCommands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GiveRank implements CommandExecutor {
    private final Mystical PLUGIN;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final RanksManager RANKS_MANAGER;

    public GiveRank(Mystical plugin, PlayerInfoManager playerInfoManager, RanksManager ranksManager) {
        this.PLUGIN = plugin;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
        this.RANKS_MANAGER = ranksManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player playerSender = (Player) sender;

        if (!playerSender.hasPermission(MysticalPermission.permissionENUM.GIVE_RANK.getPermission())) {
            playerSender.sendMessage(MysticalMessage.messageENUM.COMMAND_PERMISSION_ERROR.formatMessage());
            return true;
        }
        if (args.length < 2) {
            playerSender.sendMessage(MysticalMessage.messageENUM.INFORMATION.formatMessage("/giveRank <player> <rank name>"));
            return true;
        }

        Player player = PLUGIN.getServer().getPlayer(args[0]);

        if (player == null) { playerSender.sendMessage(MysticalMessage.messageENUM.INFORMATION.formatMessage("Invalid User")); return true; }

        String rankName = args[1];

        Rank rank = RANKS_MANAGER.getRank(rankName);

        if (rank == null) { playerSender.sendMessage(MysticalMessage.messageENUM.INFORMATION.formatMessage("Rank does not exist")); return true; }

        PLAYER_INFO_MANAGER.addRankToPlayer(player, playerSender, rank.getName());

        return true;
    }
}
