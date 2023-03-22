package mys.serone.mystical.listeners;

import mys.serone.mystical.ConfigurationSystem.Configuration;
import mys.serone.mystical.ConfigurationSystem.ConfigurationManager;
import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.playerInfoSystem.PlayerInfo;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
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
        Configuration configuration = new Configuration();

        File checkFile = new File(PLUGIN.getDataFolder().getAbsolutePath() + "/mystical_configuration.yml");

        if (checkFile.length() == 0) {
            configuration.setDefaultRank("Member");
            configurationManager.createConfigurationInfo(configuration);
        }
        
        String finalDefaultRank = null;
        
        List<Configuration> dot = configurationManager.getAllConfiguration();
        
        for (Configuration configInfo : dot) {
            finalDefaultRank = configInfo.getDefaultRank();
        }

        PlayerInfoManager playerInfoManager = new PlayerInfoManager(PLUGIN);
        PlayerInfo playerInfo = new PlayerInfo();


        ChatFunctions chatFunctions = new ChatFunctions(PLUGIN);
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();

        List<String> uuidList = new ArrayList<>();

        List<PlayerInfo> UUID = playerInfoManager.getAllPlayerInfo();

        for (PlayerInfo uu : UUID) {
            String userUUID = uu.getPlayerUUID();
            uuidList.add(userUUID);
        }

        if (!(uuidList.contains(uuid))) {
            List<String> defaultRank = new ArrayList<>();
            defaultRank.add(finalDefaultRank);
            playerInfo.setUserCoins(1000.0);
            playerInfo.setUserRankList(defaultRank);
            playerInfo.setPlayerUUID(uuid);
            playerInfoManager.createPlayerInfo(playerInfo);
            chatFunctions.informationChat(player, "You have received &a$1000&f coins for your first join.");
        }
    }
}

