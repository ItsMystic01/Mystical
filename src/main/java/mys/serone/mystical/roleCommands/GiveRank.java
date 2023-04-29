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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * Class responsible for rank granting to user
 */
public class GiveRank implements CommandExecutor {
    private final Mystical PLUGIN;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final RanksManager RANKS_MANAGER;
    private final FileConfiguration LANG_CONFIG;

    /**
     * @param plugin : Mystical Plugin
     * @param ranksManager : Ranks Manager used in accessing its functions.
     * @param playerInfoManager : Player Info Manager used in accessing its functions.
     * @param langConfig : langConfig (lang.yml) used for its ENUM messages in MysticalMessage.
     * @see RanksManager
     * @see PlayerInfoManager
     * @see MysticalMessage
     */
    public GiveRank(Mystical plugin, PlayerInfoManager playerInfoManager, RanksManager ranksManager, FileConfiguration langConfig) {
        this.PLUGIN = plugin;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
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

        Player playerSender = (Player) sender;

        if (!playerSender.hasPermission(MysticalPermission.GIVE_RANK.getPermission())) {
            playerSender.sendMessage(MysticalMessage.COMMAND_PERMISSION_ERROR.formatMessage(LANG_CONFIG));
            return true;
        }
        if (args.length < 2) {
            playerSender.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "/giveRank <player> <rank name>"), LANG_CONFIG));
            return true;
        }

        Player player = PLUGIN.getServer().getPlayer(args[0]);

        if (player == null) {
            playerSender.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Invalid User"), LANG_CONFIG));
            return true;
        }

        String rankName = args[1];

        Rank rank = RANKS_MANAGER.getRank(rankName);

        if (rank == null) {
            playerSender.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Rank does not exist"), LANG_CONFIG));
            return true;
        }

        PLAYER_INFO_MANAGER.addRankToPlayer(player, playerSender, rank.getName());

        return true;
    }
}
