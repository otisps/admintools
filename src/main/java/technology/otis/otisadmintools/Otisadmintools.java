package technology.otis.otisadmintools;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import technology.otis.otisadmintools.commands.WarpCommand;
import technology.otis.otisadmintools.db.Database;

import java.sql.Connection;

public final class Otisadmintools extends JavaPlugin {

    private static Otisadmintools instance;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        instance = this;
        FileConfiguration config = this.getConfig();
        Database database = new Database();
        Connection connection = database.getConnection(this);
        database.initalizeDatabase();
        getServer().getPluginCommand("warp").setExecutor(new WarpCommand());
    }
    @Override
    public void onDisable() {
    }

    public static Otisadmintools getInstance() {
        return instance;
    }
    }
