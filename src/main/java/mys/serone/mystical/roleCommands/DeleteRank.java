package mys.serone.mystical.roleCommands;

import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.playerInfoSystem.PlayerInfo;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class responsible for rank deletion
 */
public class DeleteRank implements CommandExecutor {

    private final RanksManager RANKS_MANAGER;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final FileConfiguration LANG_CONFIG;

    /**
     * @param ranksManager : Ranks Manager used in accessing its functions.
     * @param playerInfoManager : Player Info Manager used in accessing its functions.
     * @param langConfig : langConfig (lang.yml) used for its ENUM messages in MysticalMessage.
     * @see RanksManager
     * @see PlayerInfoManager
     * @see MysticalMessage
     */
    public DeleteRank(RanksManager ranksManager, PlayerInfoManager playerInfoManager, FileConfiguration langConfig) {
        this.RANKS_MANAGER = ranksManager;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
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

        if (!player.hasPermission(MysticalPermission.DELETE_RANK.getPermission())) {
            player.sendMessage(MysticalMessage.COMMAND_PERMISSION_ERROR.formatMessage(LANG_CONFIG));
            return true;
        }
        if (args.length < 1) {
            player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "/deleteRank <rank name>"), LANG_CONFIG));
            return true;
        }

        String rankToRemove = args[0];

        Rank rankChecker = RANKS_MANAGER.getRank(rankToRemove);

        if (rankChecker == null) {
            player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Rank does not exist"), LANG_CONFIG));
            return true;
        }

        RANKS_MANAGER.removeRank(rankToRemove);
        player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", rankToRemove + " Rank has been deleted successfully."), LANG_CONFIG));

        HashMap<String, PlayerInfo> allPlayerInfo = PLAYER_INFO_MANAGER.getAllPlayerInfo();
        for (Map.Entry<String, PlayerInfo> playerInfo : allPlayerInfo.entrySet()) {
            PlayerInfo playerInfoValues = playerInfo.getValue();
            List<String> playerRankList = playerInfoValues.getUserRankList();
            if(playerRankList.contains(rankToRemove)) {
                playerRankList.remove(args[0]);
            }
        }

        PLAYER_INFO_MANAGER.updatePlayerRankList(rankToRemove);

        return true;
    }
}
