package mys.serone.mystical.listeners;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import java.util.List;

public class ChatListener implements Listener {
    private final Mystical PLUGIN;

    public ChatListener(Mystical plugin) {
        this.PLUGIN = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        PlayerInfoManager playerInfoManager = new PlayerInfoManager(PLUGIN);
        RanksManager ranksManager = new RanksManager(PLUGIN);

        Player player = event.getPlayer();
        String message = event.getMessage();
        String playerUUID = player.getUniqueId().toString();

        List<String> playerRankList = playerInfoManager.getPlayerRankList(playerUUID);

        String prefix;
        try {
            prefix = ranksManager.getRank(playerRankList.get(0)).getPrefix();
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

