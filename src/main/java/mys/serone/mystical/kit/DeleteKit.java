package mys.serone.mystical.kit;

import mys.serone.mystical.Mystical;
import mys.serone.mystical.functions.MysticalPermission;
import mys.serone.mystical.kitSystem.PersonalKit;
import mys.serone.mystical.kitSystem.PersonalKitManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.Objects;

public class DeleteKit implements CommandExecutor {

    private final Mystical PLUGIN;
    private final PersonalKitManager PERSONAL_KIT_MANAGER;
    private final FileConfiguration LANG_FILE;
    public DeleteKit(Mystical plugin, PersonalKitManager personalKitManager, FileConfiguration langFile) {
        this.PLUGIN = plugin;
        this.PERSONAL_KIT_MANAGER = personalKitManager;
        this.LANG_FILE = langFile;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) { return true; }

        Player player = (Player) sender;
        String langMessage = LANG_FILE.getString("information");
        String langPermissionMessage = LANG_FILE.getString("command_permission_error");
        String langDeleteKitSuccessfulMessage = LANG_FILE.getString("delete_kit_successful");
        String langDeleteKitUnsuccessfulMessage = LANG_FILE.getString("delete_kit_unsuccessful");
        String langKitDoesNotExistMessage = LANG_FILE.getString("delete_kit_rank_does_not_exist_error");

        if (!player.hasPermission(MysticalPermission.permissionENUM.DELETE_KIT.getPermission())) { player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                Objects.requireNonNull(langPermissionMessage))); return true; }
        if (args.length < 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    String.format(Objects.requireNonNull(langMessage), "/deleteKit <name>")));
            return true;
        }

        String kitName = args[0];
        File kitFile = new File(PLUGIN.getDataFolder().getAbsolutePath(), "kits/" + kitName + ".yml");

        if (kitFile.exists()) {
            boolean deleteFile = kitFile.delete();

            if (deleteFile) {
                PersonalKit kitToRemove = PERSONAL_KIT_MANAGER.getKit(kitName);

                PERSONAL_KIT_MANAGER.deleteKit(kitToRemove);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        String.format(Objects.requireNonNull(langDeleteKitSuccessfulMessage), kitName)));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        String.format(Objects.requireNonNull(langDeleteKitUnsuccessfulMessage), kitName)));
            }
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    String.format(Objects.requireNonNull(langKitDoesNotExistMessage), kitName)));
        }

        return true;
    }

}
