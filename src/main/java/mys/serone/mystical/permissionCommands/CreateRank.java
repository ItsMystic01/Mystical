package mys.serone.mystical.permissionCommands;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CreateRank implements CommandExecutor {

    private final Mystical PLUGIN;

    public CreateRank(Mystical plugin) { this.PLUGIN = plugin; }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        ChatFunctions chatFunctions = new ChatFunctions(PLUGIN);
        RanksManager ranksManager = new RanksManager(PLUGIN);

        if (!sender.hasPermission("mystical.manageranks")) { chatFunctions.commandPermissionError((Player) sender); return true; }
        if (args.length < 4) { chatFunctions.commandSyntaxError((Player) sender, "/createRank [Rank Name] [Prefix] [Priority] [Permission...]"); return true; }

        List<String> finalizedArguments = new ArrayList<>();
        Collections.addAll(finalizedArguments, Arrays.copyOfRange(args, 3, args.length));
        Rank rankChecker = ranksManager.getRank(args[0]);

        if (rankChecker != null) { chatFunctions.rankChat((Player) sender, "Rank already exists"); return true; }

        ranksManager.createRank(args[0], args[1], Integer.parseInt(args[2]), finalizedArguments, null, null);

        chatFunctions.rankChat((Player) sender, args[0] + " Rank has been created successfully.");
        return true;
    }
}
