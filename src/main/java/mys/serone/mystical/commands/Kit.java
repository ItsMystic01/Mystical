package mys.serone.mystical.commands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.kit.KitManager;
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
    private static ChatFunctions chatFunctions;
    private static KitManager kitManager;

    public Kit(Mystical plugin) {
        this.PLUGIN = plugin;
        chatFunctions = new ChatFunctions(PLUGIN);
        kitManager = new KitManager(PLUGIN);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        RanksManager ranksManager = new RanksManager(PLUGIN);
        Player player = (Player) sender;

        if (!sender.hasPermission("mystical.kit")) { chatFunctions.commandPermissionError((Player) sender); return true; }
        if (args.length < 1) { chatFunctions.commandSyntaxError(player, "Usage: /kit [rank]"); return true;}

        String rankKitToGet = args[0];
        Rank rank = ranksManager.getRank(rankKitToGet);
        File kitFile = new File(PLUGIN.getDataFolder().getAbsolutePath(), "kits/" + rankKitToGet + ".yml");

        if (rank != null) { kitManager.kitOne(player, args); }
        else if (kitFile.exists()) { KitManager.claimKit(PLUGIN, player, rankKitToGet); }
        else { chatFunctions.commandSyntaxError(player, "Kit does not exist."); return true; }
        return true;
    }
}
