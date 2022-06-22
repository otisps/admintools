package technology.otis.otisadmintools.db;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import technology.otis.otisadmintools.Otisadmintools;

import java.sql.*;

public class Database {

    /**
     * gets a connection to the database
     * @param plugin the main class
     * @return a connection.
     */
    public Connection getConnection(Plugin plugin){
        FileConfiguration config = plugin.getConfig();
        String url = config.getString("database.host");
        String user = config.getString("database.username");
        String password = config.getString("database.password");
        try {
            Connection newConnection = DriverManager.getConnection(url, user, password);
            plugin.getLogger().info("Successfully connect Otis' Admin Tools to the database.");
            return newConnection;

        } catch (SQLException e) {
            plugin.getLogger().info("Otis Admin Tools wasn't able to connect to a database.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new table
     */
    public void initalizeDatabase(){
        try {
            Statement statement = getConnection(Otisadmintools.getInstance()).createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS warp_location(name varchar(16) primary key , x_location double, y_location double, z_location double)";
            statement.execute(sql);
            statement.close();
            getConnection(Otisadmintools.getInstance()).close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets location from a warpname
     * @param warpName name of the loation you want
     * @return location
     */
    public Location findLocationByWarpName(String warpName){
        try {
            PreparedStatement statement = getConnection(Otisadmintools.getInstance()).prepareStatement( "SELECT * FROM warp_location WHERE name = ?");
            statement.setString(1, warpName);
            ResultSet results = statement.executeQuery();
            if(results.next()){
                Double xLocation = results.getDouble("x_location");
                Double yLocation = results.getDouble("y_location");
                Double zLocation = results.getDouble("z_location");
                Location warpLocation = new Location(Otisadmintools.getInstance().getServer().getWorld("world"), xLocation, yLocation, zLocation);
                return warpLocation;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    /**
     * First time setting of a location.
     * @param warpName warpname to set
     * @param location location to set
     */
    public void setLocation(String warpName, Location location)  {
        try {
            PreparedStatement statement = getConnection(
                    Otisadmintools.getInstance())
                    .prepareStatement("INSERT INTO warp_location (name, x_location, y_location, z_location)" +
                    " VALUES(?, ?, ? , ?)");

            statement.setString(1, warpName);
            statement.setDouble(2, location.getX());    
            statement.setDouble(3, location.getY());
            statement.setDouble(4, location.getZ());

            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update existing warp
     * @param warpName  name of warp to update
     * @param location location to update it with
     */
    public void updateLocation(String warpName, Location location)  {
        try {
            PreparedStatement statement = getConnection(Otisadmintools.getInstance()).prepareStatement("UPDATE warp_location SET x_location = ?, y_location = ?, z_location = ? WHERE name = ?");
            statement.setDouble(1, location.getX());
            statement.setDouble(2, location.getY());
            statement.setDouble(3, location.getZ());
            statement.setString(4, warpName);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete Warp
     * @param warpName to delete
     */
    public void deleteLocation(String warpName){
        try {
            PreparedStatement statement = getConnection(Otisadmintools.getInstance()).prepareStatement("DELETE FROM warp_location WHERE name= ?");
            statement.setString(1, warpName);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
