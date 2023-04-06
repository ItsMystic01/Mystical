package mys.serone.mystical.roleCommands;

import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.functions.PermissionENUM;
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

    private final ChatFunctions CHAT_FUNCTIONS;
    private final RanksManager RANKS_MANAGER;

    public CreateRank(ChatFunctions chatFunctions, RanksManager ranksManager) {
        this.CHAT_FUNCTIONS = chatFunctions;
        this.RANKS_MANAGER = ranksManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(PermissionENUM.permissionENUM.CREATE_RANK.getPermission())) { CHAT_FUNCTIONS.commandPermissionError((Player) sender); return true; }
        if (args.length < 4) { CHAT_FUNCTIONS.commandSyntaxError((Player) sender, "/createRank [Rank Name] [Prefix] [Priority] [Permission...]"); return true; }

        List<String> finalizedArguments = new ArrayList<>();
        Collections.addAll(finalizedArguments, Arrays.copyOfRange(args, 3, args.length));
        Rank rankChecker = RANKS_MANAGER.getRank(args[0]);

        if (rankChecker != null) { CHAT_FUNCTIONS.rankChat((Player) sender, "Rank already exists"); return true; }

        RANKS_MANAGER.createRank(args[0], args[1], Integer.parseInt(args[2]), finalizedArguments, null, null);

        CHAT_FUNCTIONS.rankChat((Player) sender, args[0] + " Rank has been created successfully.");
        return true;
    }
}
