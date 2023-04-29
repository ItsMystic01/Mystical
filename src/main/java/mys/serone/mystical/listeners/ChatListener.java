package mys.serone.mystical.listeners;

import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import java.util.List;

/**
 * Class for updating chat messages
 */
public class ChatListener implements Listener {
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final RanksManager RANKS_MANAGER;

    /**
     * @param playerInfoManager : Player Info Manager used in accessing its functions.
     * @param ranksManager : Ranks Manager used in accessing its functions.
     * @see PlayerInfoManager
     * @see RanksManager
     */
    public ChatListener(PlayerInfoManager playerInfoManager, RanksManager ranksManager) {
        this.PLAYER_INFO_MANAGER = playerInfoManager;
        this.RANKS_MANAGER = ranksManager;
    }

    /**
     * @param event : Event responsible for collecting chat messages calls before sending it to the server
     */
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        String message = event.getMessage();
        String playerUUID = player.getUniqueId().toString();

        List<String> playerRankList = PLAYER_INFO_MANAGER.getPlayerRankList(playerUUID);
        String prefix;

        if (playerRankList == null) {
            prefix = "&4[&cRank Not Found&4]";
            String displayName = prefix + " " + player.getDisplayName();
            String formattedMessage = ChatColor.translateAlternateColorCodes('&',  displayName + ": " + message);
            event.setFormat(formattedMessage);
            return;
        }

        try {
            prefix = RANKS_MANAGER.getRank(playerRankList.get(0)).getPrefix();
            if (prefix == null) {
                prefix = "&4[&cRank Not Found&4]";
            }
        } catch (Exception e) {
            prefix = "&4[&cRank Not Found&4]";
        }

        String displayName = prefix + " " + player.getDisplayName();
        String formattedMessage = ChatColor.translateAlternateColorCodes('&',  displayName + ": " + message);
        event.setFormat(formattedMessage);
    }
}

