package me.bigjaymalcolm.townyreputation;

import me.bigjaymalcolm.townyreputation.listeners.PlayerJoinListener;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import me.bigjaymalcolm.townyreputation.listeners.PlayerQuitListener;
import me.bigjaymalcolm.townyreputation.reputation.PlayerReputation;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.palmergames.bukkit.towny.object.Town;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin
{
    public static Towny TownyPlugin;
    public static TownyUniverse TownyUniverse;
    public static List<Nation> TownyNations;
    public static List<Town> TownyTowns;

    public static Map<String, FileConfiguration> PlayerReputations;

    @Override
    public void onEnable()
    {
        PlayerReputations = new HashMap<String, FileConfiguration>();

        PluginManager pm = getServer().getPluginManager();
        TownyPlugin = (Towny)pm.getPlugin("Towny");
        TownyUniverse = TownyPlugin.getTownyUniverse();
        TownyNations = TownyUniverse.getDataSource().getNations();
        TownyTowns = TownyUniverse.getDataSource().getTowns();

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

        getLogger().info("TownyReputation has been enabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("getreputation"))
        {
            if (args[0] == null) return false; // If the player has not entered any args, cancel
            Player player = (Player)sender;
            String town = args[0].toLowerCase();
            String playerName = player.getName();
            FileConfiguration playerData = PlayerReputations.get(playerName);

            if (playerData.contains("reputation." + town))
            {
                Object reputation = playerData.get("reputation." + town);
                player.sendMessage(town + " reputation: " + reputation.toString());
            }
            else
            {
                player.sendMessage("No such town/nation as " + town);
            }

            return true;
        }
        else if (cmd.getName().equalsIgnoreCase("setreputation"))
        {
            if (args[0] == null) return false; // If the player has not entered any args, cancel
            Player player = Bukkit.getPlayer(args[0]);
            String town = args[1].toLowerCase();
            Integer reputation = Integer.parseInt(args[2]);
            String playerName = player.getName();
            FileConfiguration playerData = PlayerReputations.get(playerName);

            playerData.set("reputation." + town, reputation);

            getLogger().info(playerName + "'s reputation towards " + town + " has been set to " + reputation.toString() + " by " + sender.getName()); // Log the action
            sender.sendMessage("You have altered " + playerName + "'s reputation towards " + town + " to " + reputation.toString()); // Let the command sender know what they did
            player.sendMessage("Your reputation towards " + town + " has been set to " + reputation.toString()); // Inform the player that their reputation has changed
        }

        return false;
    }
}