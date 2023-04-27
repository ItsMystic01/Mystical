package mys.serone.mystical.kit;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.MysticalMessage;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.kitSystem.PersonalKit;
import mys.serone.mystical.kitSystem.PersonalKitManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;

public class DeleteKit implements CommandExecutor {

    private final Mystical PLUGIN;
    private final PersonalKitManager PERSONAL_KIT_MANAGER;
    private final FileConfiguration LANG_CONFIG;

    public DeleteKit(Mystical plugin, PersonalKitManager personalKitManager, FileConfiguration langConfig) {
        this.PLUGIN = plugin;
        this.PERSONAL_KIT_MANAGER = personalKitManager;
        this.LANG_CONFIG = langConfig;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;

        if (!player.hasPermission(MysticalPermission.DELETE_KIT.getPermission())) {
            player.sendMessage(MysticalMessage.COMMAND_PERMISSION_ERROR.formatMessage(LANG_CONFIG));
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(MysticalMessage.INFORMATION.formatMessage(Collections.singletonMap("message", "/deleteKit <name>"), LANG_CONFIG));
            return true;
        }

        String kitName = args[0];
        File kitFile = new File(PLUGIN.getDataFolder().getAbsolutePath(), "kits" + File.separator + kitName + ".yml");

        if (kitFile.exists()) {
            boolean deleteFile = kitFile.delete();

            if (deleteFile) {
                PersonalKit kitToRemove = PERSONAL_KIT_MANAGER.getKit(kitName);

                PERSONAL_KIT_MANAGER.deleteKit(kitToRemove);
                player.sendMessage(MysticalMessage.DELETE_KIT_SUCCESSFUL.formatMessage(Collections.singletonMap("kit", kitName), LANG_CONFIG));
            } else {
                player.sendMessage(MysticalMessage.DELETE_KIT_UNSUCCESSFUL.formatMessage(Collections.singletonMap("kit", kitName), LANG_CONFIG));
            }
        } else {
            player.sendMessage(MysticalMessage.DELETE_KIT_RANK_DOES_NOT_EXIST_ERROR.formatMessage(Collections.singletonMap("kit", kitName), LANG_CONFIG));
        }

        return true;
    }

}
