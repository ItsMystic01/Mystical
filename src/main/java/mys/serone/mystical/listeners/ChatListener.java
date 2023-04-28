package mys.serone.mystical.listeners;

import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import java.util.List;

public class ChatListener implements Listener {
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final RanksManager RANKS_MANAGER;

    public ChatListener(PlayerInfoManager playerInfoManager, RanksManager ranksManager) {
        this.PLAYER_INFO_MANAGER = playerInfoManager;
        this.RANKS_MANAGER = ranksManager;
    }

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

