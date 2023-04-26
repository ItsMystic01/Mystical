package mys.serone.mystical.kit;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
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
    private final PersonalKitManager PERSONAL_KIT_MANAGER;

    public DeleteKit(Mystical plugin, PersonalKitManager personalKitManager) {
        this.PLUGIN = plugin;
        this.PERSONAL_KIT_MANAGER = personalKitManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;

        if (!player.hasPermission(MysticalPermission.permissionENUM.DELETE_KIT.getPermission())) {
            player.sendMessage(MysticalMessage.messageENUM.COMMAND_PERMISSION_ERROR.formatMessage());
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(MysticalMessage.messageENUM.INFORMATION.formatMessage("/deleteKit <name>"));
            return true;
        }

        String kitName = args[0];
        File kitFile = new File(PLUGIN.getDataFolder().getAbsolutePath(), "kits/" + kitName + ".yml");

        if (kitFile.exists()) {
            boolean deleteFile = kitFile.delete();

            if (deleteFile) {
                PersonalKit kitToRemove = PERSONAL_KIT_MANAGER.getKit(kitName);

                PERSONAL_KIT_MANAGER.deleteKit(kitToRemove);
                player.sendMessage(MysticalMessage.messageENUM.DELETE_KIT_SUCCESSFUL.formatMessage(kitName));
            } else {
                player.sendMessage(MysticalMessage.messageENUM.DELETE_KIT_UNSUCCESSFUL.formatMessage(kitName));
            }
        } else {
            player.sendMessage(MysticalMessage.messageENUM.DELETE_KIT_RANK_DOES_NOT_EXIST_ERROR.formatMessage(kitName));
        }

        return true;
    }

}
