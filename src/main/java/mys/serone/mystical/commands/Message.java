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

    public Message(Mystical plugin) { this.PLUGIN = plugin; }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        ChatFunctions chatFunctions = new ChatFunctions(PLUGIN);
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(PLUGIN);
        RanksManager ranksManager = new RanksManager(PLUGIN);

        if (!(sender instanceof Player)) {
            return true;
        }

        if (!sender.hasPermission("mystical.message")) { chatFunctions.commandPermissionError((Player) sender); return true; }

        Player player = (Player) sender;

        if (args.length < 2) { chatFunctions.commandSyntaxError(player, "/message [user] [message]"); return true; }

        Player targetPlayer = PLUGIN.getServer().getPlayer(args[0]);

        if (targetPlayer == player) { chatFunctions.commandSyntaxError(player, "You can not message yourself."); return true;}
        if (targetPlayer == null) { chatFunctions.commandSyntaxError(player, "/message [user] [message]"); return true; }

        String playerUUID = player.getUniqueId().toString();
        String targetUUID = targetPlayer.getUniqueId().toString();

        List<String> senderRankList = playerInfoManager.getPlayerRankList(playerUUID);
        List<String> recipientRankList = playerInfoManager.getPlayerRankList(targetUUID);

        String senderPrefix = ranksManager.getRank(senderRankList.get(0)).getPrefix();
        String recipientPrefix = ranksManager.getRank(recipientRankList.get(0)).getPrefix();
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        chatFunctions.messageChat(player, senderPrefix, targetPlayer, recipientPrefix, message);

        return true;
    }
}
