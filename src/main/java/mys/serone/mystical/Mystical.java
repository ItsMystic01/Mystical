package mys.serone.mystical;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import mys.serone.mystical.functions.CommandEntry;
import mys.serone.mystical.listeners.ChatListener;
import mys.serone.mystical.listeners.OnFirstJoin;
import java.sql.*;

public final class Mystical extends JavaPlugin {
    private Connection connection;

    @Override
    public void onEnable() {

        try { connection = getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e); }
        saveDefaultConfig();
        checkDatabase();
        getServer().getPluginManager().registerEvents(new OnFirstJoin(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        CommandEntry.initCommands(this);
    }
    @Override
    public void onDisable() {
        disconnect();
        Bukkit.getLogger().info("Shutting down");
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                getLogger().info("Disconnected from MySQL database.");
            } catch (SQLException e) {
                getLogger().warning("Failed to disconnect from MySQL database: " + e.getMessage());
            }
        }
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            getLogger().info("(getConnection()) Connected to MySQL database.");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/m1st?autoReconnect=true&useUnicode=yes", "root", "axiemystic01");
        } catch (ClassNotFoundException | SQLException e) {
            getLogger().warning("Error: (getConnection()) Failed to connect to MySQL database: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void checkDatabase() {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS m1st");
            statement.executeUpdate("USE m1st");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS player_info (player_uuid VARCHAR(36) PRIMARY KEY, player_rank TEXT, player_coins DOUBLE)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

