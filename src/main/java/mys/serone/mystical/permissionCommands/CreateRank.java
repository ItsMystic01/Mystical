package mys.serone.mystical.permissionCommands;

import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.rankSystem.Rank;
import mys.serone.mystical.rankSystem.RanksManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CreateRank implements CommandExecutor {

    public ChatFunctions chatFunctions = new ChatFunctions();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (args.length < 3) { chatFunctions.commandSyntaxError( (Player) sender, "/createRank [Rank Name] [Prefix] [Permission...]"); return true; }

        List<String> finalizedArguments = new ArrayList<>();
        Collections.addAll(finalizedArguments, Arrays.copyOfRange(args, 2, args.length));

        RanksManager ranksManager = new RanksManager(new File("C:/Users/ItsMystic01/Downloads/MyJava/Spigot/src/main/resources/ranks.yml"));

        Rank rankChecker = ranksManager.getRank(args[0]);
        if (rankChecker != null) { chatFunctions.rankChat((Player) sender, "Rank already exists"); return true; }
        Rank newRank = new Rank();
        newRank.setName(args[0]);
        newRank.setPrefix(args[1]);
        newRank.setPermissions(finalizedArguments);
        newRank.setIsDefault(false);
        ranksManager.createRank(newRank);

        sender.sendMessage("Success");
        return true;
    }
}
