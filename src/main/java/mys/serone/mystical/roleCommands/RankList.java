package mys.serone.mystical.roleCommands;

import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.handlers.Gradient;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

/**
 * Class for checking all ranks in ranks.yml
 */
public class RankList implements CommandExecutor {
    private final RanksManager RANKS_MANAGER;
    private final FileConfiguration LANG_CONFIG;

    /**
     * @param ranksManager : Ranks Manager used in accessing its functions.
     * @param langConfig : langConfig (lang.yml) used for its ENUM messages in MysticalMessage.
     * @see RanksManager
     * @see MysticalMessage
     */
    public RankList(RanksManager ranksManager, FileConfiguration langConfig) {
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

        HashMap<UUID, Rank> allRanks = RANKS_MANAGER.getRanks();
        Player player = (Player) sender;

        if (!player.hasPermission(MysticalPermission.RANK_LIST.getPermission())) {
            player.sendMessage(MysticalMessage.COMMAND_PERMISSION_ERROR.formatMessage(LANG_CONFIG));
            return true;
        }

        StringBuilder rankList = new StringBuilder();
        rankList.append("&e&lAll Existing Ranks for the Server!\n");
        for (UUID rank : allRanks.keySet()) {
            Rank rankAccount = allRanks.get(rank);
            String rankName = rankAccount.getName();
            String rankPrefix = rankAccount.getPrefix();

            String startingHexCode = RANKS_MANAGER.getRank(rankName).getStartingHexColor();
            String endingHexCode = RANKS_MANAGER.getRank(rankName).getEndingHexColor();
            if (startingHexCode != null && endingHexCode != null) {
                String gradientRank = String.valueOf(Gradient.displayName(rankName, startingHexCode, endingHexCode, false));
                rankPrefix = ChatColor.translateAlternateColorCodes('&', "&7&l[" + gradientRank + "&7&l]");
            } else if (rankPrefix == null) {
                rankPrefix = "&c[&fInvalid Rank&c]";
                System.out.println("[Mystical] Incomplete/Invalid rank format in ranks.yml");
            }
            rankList.append("&e&l- &7").append(rankName).append(": ").append(rankPrefix).append("\n");
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(rankList)));

        return true;
    }
}
