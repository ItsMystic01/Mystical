package mys.serone.mystical.functions;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.List;

public class ChatFunctions {

    private final Mystical PLUGIN;
    public static final String ECONOMY_PREFIX = "&6[&aEconomy&6] &l| ";
    public static final String SYNTAX_ERROR_PREFIX = "&b[&3Syntax Error&b] &l| ";
    public static final String GENERAL_PREFIX = "&6[&eServer&6] &l| ";
    public static final String PERMISSION_PREFIX = "&b[&3Permission&b] &l| ";
    public static final String RANK_PREFIX = "&b[&3Rank&b] &l| ";

    public ChatFunctions(Mystical plugin) { this.PLUGIN = plugin; }

    public void informationChat(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', GENERAL_PREFIX + "&f" + message));
    }

    public void commandPermissionError(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', PERMISSION_PREFIX + "&3You do not have the permission to perform this command."));
    }

    public void commandSyntaxError(Player player, String commandSyntax) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', SYNTAX_ERROR_PREFIX + "&3" + commandSyntax));
    }

    public void adminEconomyChat(Player player, Player target, String status, double newBalance, double amount) {
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', ECONOMY_PREFIX + "&a$" + amount + "&f has been " + status.toLowerCase() + " to your account."));
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', ECONOMY_PREFIX + "&fBalance: &a$" + newBalance));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ECONOMY_PREFIX + "&f" + status + " Balance to &c" + target.getName() + "&r&f: &a$" + amount));
    }

    public void balanceEconomyChat(Player player, double balance) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ECONOMY_PREFIX + "&fBalance: &a$" + balance));
    }

    public String payerEconomyChat(Player recipient, double amount) {
        return ChatColor.translateAlternateColorCodes('&', ECONOMY_PREFIX + "&fYou have successfully given &a$" + amount + " &fto &c" + recipient.getName());
    }

    public String recipientEconomyChat(Player sender, double amount) {
        return ChatColor.translateAlternateColorCodes('&', ECONOMY_PREFIX + "&fYou have received: &a$" + amount + "&f from &c" + sender.getName());
    }

    public void playerRankChat(Player player, List<String> rank) {
        RanksManager ranksManager = new RanksManager(PLUGIN);
        StringBuilder userRank = new StringBuilder();
        for ( String perRank : rank ) {
            userRank.append(ranksManager.getRank(perRank).getPrefix());
            userRank.append(" ");
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', RANK_PREFIX + "&r" + userRank));
    }

    public void rankChat(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', RANK_PREFIX + "&3" + message));
    }

    public void messageChat(Player player, String playerPrefix, Player targetPlayer, String targetPrefix, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dTo &r" + playerPrefix + " "
                + targetPlayer.getDisplayName() + "&7: ") + message);
        targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dFrom &r" + targetPrefix +
                " " + player.getDisplayName() + "&7: ") + message);
    }

}
