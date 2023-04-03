package mys.serone.mystical.kit;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.kitSystem.PersonalKitManager;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class SetKitPrefix implements CommandExecutor {
    private final Mystical PLUGIN;
    private final ChatFunctions CHAT_FUNCTIONS;
    private final PersonalKitManager PERSONAL_KIT_MANAGER;
    private final RanksManager RANKS_MANAGER;

    public SetKitPrefix(Mystical plugin, ChatFunctions chatFunctions, PersonalKitManager personalKitManager, RanksManager ranksManager) {
        this.PLUGIN = plugin;
        this.CHAT_FUNCTIONS = chatFunctions;
        this.PERSONAL_KIT_MANAGER = personalKitManager;
        this.RANKS_MANAGER = ranksManager;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        if (!sender.hasPermission("mystical.kit")) { CHAT_FUNCTIONS.commandPermissionError((Player) sender); return true; }
        if (args.length < 2) { CHAT_FUNCTIONS.commandSyntaxError(player, "Usage: /setKitPrefix [rank] [prefix]"); return true;}

        String rankKitToGet = args[0];
        Rank rank = RANKS_MANAGER.getRank(rankKitToGet);
        File kitFile = new File(PLUGIN.getDataFolder().getAbsolutePath(), "kits/" + rankKitToGet + ".yml");

        if (rank != null) { RANKS_MANAGER.setKitName(player, rankKitToGet, args[1]); }
        else if (kitFile.exists()) { PERSONAL_KIT_MANAGER.setKitPrefix(player, rankKitToGet, args[1]); }
        else { CHAT_FUNCTIONS.commandSyntaxError(player, "Kit does not exist."); return true; }
        return true;
    }
}
