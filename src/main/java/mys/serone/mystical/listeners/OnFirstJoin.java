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
        Configuration configuration = new Configuration();

        File checkRanksFile = new File(PLUGIN.getDataFolder().getAbsolutePath() + "/ranks.yml");
        if (checkRanksFile.length() == 0) {
            List<String> newRankPermission = new ArrayList<>();
            newRankPermission.add("mystical.help");

            List<Map<String, Object>> kit = new ArrayList<>();
            Map<String, Object> kitMap = new HashMap<>();
            kitMap.put("leather_boots", "protection:1");
            kit.add(kitMap);

            RanksManager ranksManager = new RanksManager(PLUGIN);
            ranksManager.createRank("Member", "&f[&7Member&f]", 1, newRankPermission, kit, "&f[&7Member&f]");
        }

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
            List<String> playerAdditionalPermission = playerInfoManager.getPlayerAdditionalPermission(uuid);
            if (playerAdditionalPermission.size() > 0) {
                for (String permissionToAdd : playerAdditionalPermission) {
                    player.addAttachment(PLUGIN, permissionToAdd, true);
                }
            }

        } else {
            List<String> defaultRank = new ArrayList<>();
            List<String> defaultAdditionalPermission = new ArrayList<>();
            defaultRank.add(finalDefaultRank);
            playerInfoManager.createPlayerInfo(uuid, finalDefaultCoins, defaultRank, defaultAdditionalPermission);

            List<String> playerJoinedRankList = playerInfoManager.getPlayerRankList(uuid);
            RanksManager ranksManager = new RanksManager(PLUGIN);
            for (String rank : playerJoinedRankList) {
                for (String rankPerm : ranksManager.getRank(rank).getPermissions()) {
                    player.addAttachment(PLUGIN, rankPerm, true);
                }
            }
            chatFunctions.informationChat(player, "You have received &a$" + finalDefaultCoins  + "&f coins for your first join.");
        }
    }
}

