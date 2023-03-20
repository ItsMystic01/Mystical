package mys.serone.mystical.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class ConfigUtil {
    private final File FILE;
    private final FileConfiguration CONFIG;

    public ConfigUtil(Plugin plugin, String path) {
        this(plugin.getDataFolder().getAbsolutePath() + "/" + path);
    }

    public ConfigUtil(String path) {
        this.FILE = new File(path);
        this.CONFIG = YamlConfiguration.loadConfiguration(this.FILE);
    }

    public boolean save() {
        try {
            this.CONFIG.save(this.FILE);
            return true;
    } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public FileConfiguration getConfig() {
        return this.CONFIG;
    }

}
