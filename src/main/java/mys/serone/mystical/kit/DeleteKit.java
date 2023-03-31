package mys.serone.mystical.kit;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.io.File;

public class DeleteKit implements CommandExecutor {

    private final Mystical plugin;
    private final ChatFunctions chatFunctions;
    public DeleteKit(Mystical plugin) {
        this.plugin = plugin;
        this.chatFunctions = new ChatFunctions(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) { return true; }
        if (!sender.hasPermission("mystical.managekits")) { chatFunctions.commandPermissionError((Player) sender); return true; }

        if (args.length < 1) {
            chatFunctions.commandSyntaxError((Player) sender, "/deleteKit [name]");
            return true;
        }

        Player player = (Player) sender;
        String kitName = args[0];

        File kitFile = new File(plugin.getDataFolder().getAbsolutePath(), "kits/" + kitName + ".yml");
        if (kitFile.exists()) {
            boolean deleteFile = kitFile.delete();
            if (deleteFile) {
                PersonalKitManager personalKitManager = new PersonalKitManager(plugin);
                PersonalKit kitToRemove = personalKitManager.getKit(kitName);
                personalKitManager.deleteKit(kitToRemove);
                chatFunctions.configurationError(player, kitName + " kit has been deleted successfully.");
            } else {
                chatFunctions.configurationError(player, kitName + " kit has been deleted unsuccessfully.");
            }
        } else {
            chatFunctions.configurationError(player, kitName + " rank kit does not exist.");
        }

        return true;
    }

}
