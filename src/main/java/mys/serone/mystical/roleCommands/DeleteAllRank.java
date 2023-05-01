package mys.serone.mystical.roleCommands;

import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Class responsible for rank deletion of all or some ranks
 */
public class DeleteAllRank implements CommandExecutor {

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
    public DeleteAllRank(RanksManager ranksManager, PlayerInfoManager playerInfoManager, FileConfiguration langConfig) {
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

        if (!player.hasPermission(MysticalPermission.DELETE_ALL_RANK.getPermission())) {
            player.sendMessage(MysticalMessage.COMMAND_PERMISSION_ERROR.formatMessage(LANG_CONFIG));
            return true;
        }

        if (args.length < 1) { usageText(player); return true; }

        String deleteAllRankCondition = args[0];
        StringBuilder listOfRemovedRanks = new StringBuilder();
        List<String> listOfRankNamesToRemove = new ArrayList<>();
        List<String> listOfRankNames = new ArrayList<>();
        HashMap<UUID, Rank> rankList = RANKS_MANAGER.getRanks();

        switch (deleteAllRankCondition) {
            case "all":
                RANKS_MANAGER.deleteAllRank();
                player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "All ranks has been deleted successfully."), LANG_CONFIG));
                return true;
            case "name": {
                if (args.length < 2) {
                    player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Usage: /deleteallrank name [name]"), LANG_CONFIG));
                    player.sendMessage(MysticalMessage.INFORMATION.formatMessage(
                            Collections.singletonMap("message", "&7Example: /deleteallrank name Member -> This will delete all ranks that contains the word Member in it. Case-Sensitive."), LANG_CONFIG));
                    return true;
                }

                for (Rank rankInRankList : rankList.values()) {
                    listOfRankNames.add(rankInRankList.getName());
                }

                listOfRemovedRanks.append("List of removed ranks:\n");

                for (String rankName : listOfRankNames) {
                    if (!rankName.contains(args[1])) {
                        continue;
                    }
                    listOfRankNamesToRemove.add(rankName);
                    listOfRemovedRanks.append(rankName).append("\n");
                }

                if (listOfRemovedRanks.length() == 23) {
                    player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "No rank has a name that contains " + args[1]), LANG_CONFIG));
                    return true;
                }
                RANKS_MANAGER.deleteAllRank(listOfRankNamesToRemove);
                player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "List of ranks that have been removed: \n" + listOfRemovedRanks), LANG_CONFIG));
                player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "&c" + listOfRankNamesToRemove.size() + " &7ranks have been deleted"), LANG_CONFIG));
                break;
            }
            case "char": {
                if (args.length < 2) {
                    player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Usage: /deleteallrank char [character]"), LANG_CONFIG));
                    player.sendMessage(MysticalMessage.INFORMATION.formatMessage(
                            Collections.singletonMap("message", "&7Example: /deleteallrank char A -> This will delete all ranks that starts with the character 'A' in it. Case-Sensitive."), LANG_CONFIG));
                    return true;
                }

                char characterOfRankToCheck = args[1].charAt(0);

                for (Rank rankInRankList : rankList.values()) {
                    listOfRankNames.add(rankInRankList.getName());
                }

                listOfRemovedRanks.append("List of removed ranks:\n");

                for (String rankName : listOfRankNames) {
                    if (!rankName.startsWith(Character.toString(characterOfRankToCheck))) {
                        continue;
                    }
                    listOfRankNamesToRemove.add(rankName);
                    listOfRemovedRanks.append(rankName).append("\n");
                }

                if (listOfRemovedRanks.length() == 23) {
                    player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "No rank has a name that starts with letter: " + args[1]), LANG_CONFIG));
                    return true;
                }
                RANKS_MANAGER.deleteAllRank(listOfRankNamesToRemove);
                player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "List of ranks that have been removed: \n" + listOfRemovedRanks), LANG_CONFIG));
                player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "&c" + listOfRankNamesToRemove.size() + " &7ranks have been deleted"), LANG_CONFIG));
                break;
            }
            case "priority": {
                if (args.length < 2) {
                    player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Usage: /deleteallrank priority [priority number]"), LANG_CONFIG));
                    player.sendMessage(MysticalMessage.INFORMATION.formatMessage(
                            Collections.singletonMap("message", "&7Example: /deleteallrank priority 50 -> This will delete all ranks that has a priority equal and beyond 50. (The higher the number of priority, the less its value.)"), LANG_CONFIG));
                    return true;
                }
                int rankPriorityToRemove = 0;
                try {
                    rankPriorityToRemove = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "Invalid priority number."), LANG_CONFIG));
                }

                HashMap<String, Integer> listOfRankNamesInFile = new HashMap<>();
                for (Rank rankInRankList : rankList.values()) {
                    listOfRankNamesInFile.put(rankInRankList.getName(), rankInRankList.getPriority());
                }

                listOfRemovedRanks.append("List of removed ranks:\n");

                for (Map.Entry<String, Integer> rankAccount : listOfRankNamesInFile.entrySet()) {
                    String rankName = rankAccount.getKey();
                    int rankPriority = rankAccount.getValue();
                    if (rankPriority < rankPriorityToRemove) {
                        continue;
                    }
                    listOfRankNamesToRemove.add(rankName);
                    listOfRemovedRanks.append(rankName).append("\n");
                }

                if (listOfRemovedRanks.length() == 23) {
                    player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "No rank has a priority equal or above the number " + args[1]), LANG_CONFIG));
                    return true;
                }
                RANKS_MANAGER.deleteAllRank(listOfRankNamesToRemove);
                player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "List of ranks that have been removed: \n" + listOfRemovedRanks), LANG_CONFIG));
                player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "&c" + listOfRankNamesToRemove.size() + " &7ranks have been deleted"), LANG_CONFIG));
                break;
            }
            default:
                usageText(player);
                break;
        }
        if (listOfRankNamesToRemove.size() > 0) { PLAYER_INFO_MANAGER.updatePlayerRankList(listOfRankNamesToRemove); }
        return true;
    }

    /**
     * @param player : Player provided by the onCommand Event
     */
    public void usageText(Player player) {
        List<String> usageText = new ArrayList<>();
        usageText.add("&7Usage: /deleteallrank <name | char | priority | all> [name: name | char: first character | priority: number]");
        usageText.add("&7Example: /deleteallrank name Member -> This will delete all ranks that contains the word Member in it. Case-Sensitive.");
        usageText.add("&7Example: /deleteallrank char A -> This will delete all ranks that starts with the character 'A' in it. Case-Sensitive.");
        usageText.add("&7Example: /deleteallrank priority 50 -> This will delete all ranks that has a priority equal and beyond 50.(The higher the number of priority, the less its value.)");
        usageText.add("&7Example: /deleteallrank all -> This will delete all ranks that are in ranks.yml");

        for (String text : usageText) {
            player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", text), LANG_CONFIG));
        }
    }
}
