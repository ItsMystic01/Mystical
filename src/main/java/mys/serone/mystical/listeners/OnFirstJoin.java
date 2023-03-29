package mys.serone.mystical.listeners;

import mys.serone.mystical.ConfigurationSystem.Configuration;
import mys.serone.mystical.ConfigurationSystem.ConfigurationManager;
import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.playerInfoSystem.PlayerInfo;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OnFirstJoin implements Listener {
    private final Mystical PLUGIN;

    public OnFirstJoin(Mystical plugin) {
        this.PLUGIN = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        ConfigurationManager configurationManager = new ConfigurationManager(PLUGIN);
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(PLUGIN);
        ChatFunctions chatFunctions = new ChatFunctions(PLUGIN);
        PlayerInfo playerInfo = new PlayerInfo();
        Configuration configuration = new Configuration();

        File checkFile = new File(PLUGIN.getDataFolder().getAbsolutePath() + "/mystical_configuration.yml");
        String finalDefaultRank = null;
        Double finalDefaultCoins = null;

        if (checkFile.length() == 0) {
            configuration.setDefaultRank("Member");
            configuration.setDefaultCoins(1000.0);
            configurationManager.createConfigurationInfo(configuration);
        }

        List<Configuration> allConfig = configurationManager.getAllConfiguration();

        for (Configuration configInfo : allConfig) {
            finalDefaultRank = configInfo.getDefaultRank();
            finalDefaultCoins = configInfo.getDefaultCoins();
        }

        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();

        List<String> uuidList = new ArrayList<>();

        List<PlayerInfo> UUID = playerInfoManager.getAllPlayerInfo();

        for (PlayerInfo uu : UUID) {
            String userUUID = uu.getPlayerUUID();
            uuidList.add(userUUID);
        }

        if (uuidList.contains(uuid)) {
            List<String> playerJoinedRankList = playerInfoManager.getPlayerRankList(uuid);
            RanksManager ranksManager = new RanksManager(PLUGIN);
            for (String rank : playerJoinedRankList) {
                for (String rankPerm : ranksManager.getRank(rank).getPermissions()) {
                    player.addAttachment(PLUGIN, rankPerm, true);
                }
            }
        } else {
            List<String> defaultRank = new ArrayList<>();
            defaultRank.add(finalDefaultRank);
            playerInfo.setUserCoins(finalDefaultCoins);
            playerInfo.setUserRankList(defaultRank);
            playerInfo.setPlayerUUID(uuid);
            playerInfoManager.createPlayerInfo(playerInfo);
            chatFunctions.informationChat(player, "You have received &a$" + finalDefaultCoins  + "&f coins for your first join.");
        }
    }
}

