package mys.serone.mystical.listeners;

import mys.serone.mystical.configurationSystem.Configuration;
import mys.serone.mystical.configurationSystem.ConfigurationManager;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnFirstJoinListener implements Listener {
    private final Mystical PLUGIN;
    private final ConfigurationManager CONFIGURATION_MANAGER;
    private final Configuration CONFIGURATION;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final ChatFunctions CHAT_FUNCTIONS;
    private final RanksManager RANKS_MANAGER;
    private final File CHECK_RANKS_FILE;

    public OnFirstJoinListener(Mystical plugin, ConfigurationManager configurationManager, Configuration configuration,
                               PlayerInfoManager playerInfoManager, ChatFunctions chatFunctions, RanksManager ranksManager, File ranksFile) {
        this.PLUGIN = plugin;
        this.CONFIGURATION_MANAGER = configurationManager;
        this.CONFIGURATION = configuration;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
        this.CHAT_FUNCTIONS = chatFunctions;
        this.RANKS_MANAGER = ranksManager;
        this.CHECK_RANKS_FILE = ranksFile;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        if (CHECK_RANKS_FILE.length() == 0) {
            List<String> newRankPermission = new ArrayList<>();
            newRankPermission.add("mystical.help");

            List<Map<String, Object>> kit = new ArrayList<>();
            Map<String, Object> kitMap = new HashMap<>();
            kitMap.put("leather_boots", "protection:1");
            kit.add(kitMap);

            RANKS_MANAGER.createRank("Member", "&f[&7Member&f]", 1, newRankPermission, kit, "&f[&7Member&f]");
        }

        File checkFile = new File(PLUGIN.getDataFolder().getAbsolutePath() + "/mystical_configuration.yml");
        String finalDefaultRank = null;
        Double finalDefaultCoins = null;

        if (checkFile.length() == 0) {
            CONFIGURATION.setDefaultRank("Member");
            CONFIGURATION.setDefaultCoins(1000.0);
            CONFIGURATION_MANAGER.createConfigurationInfo(CONFIGURATION);
        }

        List<Configuration> allConfig = CONFIGURATION_MANAGER.getAllConfiguration();

        for (Configuration configInfo : allConfig) {
            finalDefaultRank = configInfo.getDefaultRank();
            finalDefaultCoins = configInfo.getDefaultCoins();
        }

        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();

        HashMap<String, PlayerInfo> allPlayerInfo = PLAYER_INFO_MANAGER.getAllPlayerInfo();
        PlayerInfo playerInfo = allPlayerInfo.get(uuid);

        List<String> uuidList = new ArrayList<>();

        for (Map.Entry<String, PlayerInfo> playerInfoEntry : allPlayerInfo.entrySet()) {
            uuidList.add(playerInfoEntry.getKey());
        }

        if (uuidList.contains(uuid)) {
            List<String> playerJoinedRankList = playerInfo.getUserRankList();
            for (String rank : playerJoinedRankList) {
                for (String rankPerm : RANKS_MANAGER.getRank(rank).getPermissions()) {
                    player.addAttachment(PLUGIN, rankPerm, true);
                }
            }
            List<String> playerAdditionalPermission = playerInfo.getUserAdditionalPermission();
            if (playerAdditionalPermission.size() > 0) {
                for (String permissionToAdd : playerAdditionalPermission) {
                    player.addAttachment(PLUGIN, permissionToAdd, true);
                }
            }

        } else {
            List<String> defaultRank = new ArrayList<>();
            List<String> defaultAdditionalPermission = new ArrayList<>();

            defaultRank.add(finalDefaultRank);
            PLAYER_INFO_MANAGER.createPlayerInfo(uuid, finalDefaultCoins, defaultRank, defaultAdditionalPermission);

            List<String> playerJoinedRankList = playerInfo.getUserRankList();

            for (String rank : playerJoinedRankList) {
                for (String rankPerm : RANKS_MANAGER.getRank(rank).getPermissions()) {
                    player.addAttachment(PLUGIN, rankPerm, true);
                }
            }
            
            CHAT_FUNCTIONS.informationChat(player, "You have received &a$" + finalDefaultCoins  + "&f coins for your first join.");
        }
    }
}

