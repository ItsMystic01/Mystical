package mys.serone.mystical.kit;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.ChatFunctions;
import mys.serone.mystical.kitSystem.PersonalKit;
import mys.serone.mystical.kitSystem.PersonalKitManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.io.File;

public class DeleteKit implements CommandExecutor {

    private final Mystical PLUGIN;
    private final ChatFunctions CHAT_FUNCTIONS;
    private final PersonalKitManager PERSONAL_KIT_MANAGER;
    public DeleteKit(Mystical plugin, ChatFunctions chatFunctions, PersonalKitManager personalKitManager) {
        this.PLUGIN = plugin;
        this.CHAT_FUNCTIONS = chatFunctions;
        this.PERSONAL_KIT_MANAGER = personalKitManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) { return true; }
        if (!sender.hasPermission("mystical.managekits")) { CHAT_FUNCTIONS.commandPermissionError((Player) sender); return true; }

        if (args.length < 1) {
            CHAT_FUNCTIONS.commandSyntaxError((Player) sender, "/deleteKit [name]");
            return true;
        }

        Player player = (Player) sender;
        String kitName = args[0];

        File kitFile = new File(PLUGIN.getDataFolder().getAbsolutePath(), "kits/" + kitName + ".yml");
        if (kitFile.exists()) {
            boolean deleteFile = kitFile.delete();
            if (deleteFile) {
                PersonalKit kitToRemove = PERSONAL_KIT_MANAGER.getKit(kitName);
                PERSONAL_KIT_MANAGER.deleteKit(kitToRemove);
                CHAT_FUNCTIONS.configurationError(player, kitName + " kit has been deleted successfully.");
            } else {
                CHAT_FUNCTIONS.configurationError(player, kitName + " kit has been deleted unsuccessfully.");
            }
        } else {
            CHAT_FUNCTIONS.configurationError(player, kitName + " rank kit does not exist.");
        }

        return true;
    }

}
