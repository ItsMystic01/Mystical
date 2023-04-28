package mys.serone.mystical.listeners;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.configurationSystem.Configuration;
import mys.serone.mystical.configurationSystem.ConfigurationManager;
import mys.serone.mystical.handlers.RankConfigurationHandler;
import mys.serone.mystical.playerInfoSystem.PlayerInfo;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.util.*;

public class OnFirstJoinListener implements Listener {
    private final Mystical PLUGIN;
    private final ConfigurationManager CONFIGURATION_MANAGER;
    private final Configuration CONFIGURATION;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final RanksManager RANKS_MANAGER;
    private final File CHECK_RANKS_FILE;

    public OnFirstJoinListener(Mystical plugin, ConfigurationManager configurationManager, Configuration configuration,
                               PlayerInfoManager playerInfoManager, RanksManager ranksManager, File ranksFile) {
        this.PLUGIN = plugin;
        this.CONFIGURATION_MANAGER = configurationManager;
        this.CONFIGURATION = configuration;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
        this.RANKS_MANAGER = ranksManager;
        this.CHECK_RANKS_FILE = ranksFile;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        new RankConfigurationHandler(RANKS_MANAGER, PLAYER_INFO_MANAGER);
        if (CHECK_RANKS_FILE.length() == 0) {
            List<String> newRankPermission = new ArrayList<>();
            newRankPermission.add("mystical.help");

            List<Map<String, Object>> kit = new ArrayList<>();
            Map<String, Object> kitMap = new HashMap<>();
            kitMap.put("leather_boots", "protection:1");
            kit.add(kitMap);

            RANKS_MANAGER.createRank(UUID.randomUUID(), "Member", "&f[&7Member&f]", 50, newRankPermission, kit, "&f[&7Member&f]");
        }

        File checkFile = new File(PLUGIN.getDataFolder().getAbsolutePath() + File.separator + "mystical_configuration.yml");
        String finalDefaultRank = null;

        if (checkFile.length() == 0) {
            CONFIGURATION.setDefaultRank("Member");
            CONFIGURATION_MANAGER.createConfigurationInfo(CONFIGURATION);
        }

        List<Configuration> allConfig = CONFIGURATION_MANAGER.getAllConfiguration();

        for (Configuration configInfo : allConfig) {
            finalDefaultRank = configInfo.getDefaultRank();
        }

        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();

        HashMap<String, PlayerInfo> allPlayerInfo = PLAYER_INFO_MANAGER.getAllPlayerInfo();

        List<String> uuidList = new ArrayList<>();

        for (Map.Entry<String, PlayerInfo> playerInfoEntry : allPlayerInfo.entrySet()) {
            uuidList.add(playerInfoEntry.getKey());
        }

        if (uuidList.contains(uuid)) {
            PlayerInfo playerInfo = allPlayerInfo.get(uuid);
            List<String> playerJoinedRankList = playerInfo.getUserRankList();
            for (String rank : playerJoinedRankList) {
                Rank rankToGet = RANKS_MANAGER.getRank(rank);
                if (rankToGet == null) { continue; }
                for (String rankPerm : rankToGet.getPermissions()) {
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
            PLAYER_INFO_MANAGER.createPlayerInfo(uuid, defaultRank, defaultAdditionalPermission);
            PlayerInfo playerInfo = allPlayerInfo.get(uuid);
            List<String> playerJoinedRankList = playerInfo.getUserRankList();

            for (String rank : playerJoinedRankList) {
                for (String rankPerm : RANKS_MANAGER.getRank(rank).getPermissions()) {
                    player.addAttachment(PLUGIN, rankPerm, true);
                }
            }
        }
    }
}

