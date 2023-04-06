package mys.serone.mystical.commands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.functions.PermissionENUM;
import mys.serone.mystical.kitSystem.KitManager;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.io.File;

public class Kit implements CommandExecutor {
    private final Mystical PLUGIN;
    private final ChatFunctions CHAT_FUNCTIONS;
    private final KitManager KIT_MANAGER;
    private final RanksManager RANKS_MANAGER;

    public Kit(Mystical plugin, ChatFunctions chatFunctions, KitManager kitManager, RanksManager ranksManager) {
        this.PLUGIN = plugin;
        this.CHAT_FUNCTIONS = chatFunctions;
        this.KIT_MANAGER = kitManager;
        this.RANKS_MANAGER = ranksManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        if (!sender.hasPermission(PermissionENUM.permissionENUM.KIT.getPermission())) { CHAT_FUNCTIONS.commandPermissionError((Player) sender); return true; }
        if (args.length < 1) { CHAT_FUNCTIONS.commandSyntaxError(player, "Usage: /kit [rank]"); return true;}

        String rankKitToGet = args[0];
        Rank rank = RANKS_MANAGER.getRank(rankKitToGet);
        File kitFile = new File(PLUGIN.getDataFolder().getAbsolutePath(), "kits/" + rankKitToGet + ".yml");

        if (rank != null) { KIT_MANAGER.kitOne(player, args); }
        else if (kitFile.exists()) { KIT_MANAGER.claimKit(player, rankKitToGet); }
        else { CHAT_FUNCTIONS.commandSyntaxError(player, "Kit does not exist."); return true; }
        return true;
    }
}
