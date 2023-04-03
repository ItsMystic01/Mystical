package mys.serone.mystical.commands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.playerInfoSystem.PlayerInfoManager;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.List;

public class Message implements CommandExecutor {
    private final Mystical PLUGIN;
    private final PlayerInfoManager PLAYER_INFO_MANAGER;
    private final ChatFunctions CHAT_FUNCTIONS;
    private final RanksManager RANKS_MANAGER;
    public Message(Mystical plugin, RanksManager ranksManager, ChatFunctions chatFunctions, PlayerInfoManager playerInfoManager) {
        this.PLUGIN = plugin;
        this.RANKS_MANAGER = ranksManager;
        this.CHAT_FUNCTIONS = chatFunctions;
        this.PLAYER_INFO_MANAGER = playerInfoManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        if (!sender.hasPermission("mystical.message")) { CHAT_FUNCTIONS.commandPermissionError((Player) sender); return true; }

        Player player = (Player) sender;

        if (args.length < 2) { CHAT_FUNCTIONS.commandSyntaxError(player, "/message [user] [message]"); return true; }

        Player targetPlayer = PLUGIN.getServer().getPlayer(args[0]);

        if (targetPlayer == player) { CHAT_FUNCTIONS.commandSyntaxError(player, "You can not message yourself."); return true;}
        if (targetPlayer == null) { CHAT_FUNCTIONS.commandSyntaxError(player, "/message [user] [message]"); return true; }

        String playerUUID = player.getUniqueId().toString();
        String targetUUID = targetPlayer.getUniqueId().toString();

        List<String> senderRankList = PLAYER_INFO_MANAGER.getPlayerRankList(playerUUID);
        List<String> recipientRankList = PLAYER_INFO_MANAGER.getPlayerRankList(targetUUID);

        String senderPrefix = RANKS_MANAGER.getRank(senderRankList.get(0)).getPrefix();
        String recipientPrefix = RANKS_MANAGER.getRank(recipientRankList.get(0)).getPrefix();
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        CHAT_FUNCTIONS.messageChat(player, senderPrefix, targetPlayer, recipientPrefix, message);

        return true;
    }
}
