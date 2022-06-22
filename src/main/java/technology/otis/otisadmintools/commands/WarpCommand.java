package technology.otis.otisadmintools.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import technology.otis.otisadmintools.Otisadmintools;
import technology.otis.otisadmintools.db.Database;

public class WarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Otisadmintools plugin = Otisadmintools.getInstance();

        if (!(sender instanceof Player)) {
            plugin.getLogger().info("Command is for players only.");
            return true;
        }

        // So its a player ...
        Player player = (Player) sender;

        if (args.length < 1){ // Case no location entered
            player.sendMessage("Enter where you'd like to warp to /warp name");
            return true;

        } if (args.length == 1){ // Case location entered
            Database database = new Database();
            Location warpLocation = database.findLocationByWarpName(args[0]);
                // Case that location is unknown
            if (warpLocation == null) {
                player.sendMessage("We haven't got a warp with that named saved");
                return true;
            }

            // Case that location is correct
            player.teleport(warpLocation);
            player.sendMessage("You have been teleported to the warp.");
            return true;
        }

        if ((args[0].equalsIgnoreCase("set")) && (args.length == 2)) {
            // Case entered correctly & doesn't contain
            Location location = player.getLocation();
            Database database = new Database();
            Location warpLocation = database.findLocationByWarpName(args[0]);
            if (warpLocation != null) {
                database.updateLocation(args[1], location);
                player.sendMessage("You have updated the warp location.");
                return true;
            }
            database.setLocation(args[1], location);
            player.sendMessage("You have set the warp location.");
            return true;
        }

        if(args[0].equalsIgnoreCase("delete") && args.length == 2){
            Database database = new Database();
            database.deleteLocation(args[1]);
            player.sendMessage("You have deleted the warp location.");
            return true;
        }

        player.sendMessage("Invalid arguments, use /warp name");
        return true;
    }
}
