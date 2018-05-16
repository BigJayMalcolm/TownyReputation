package me.bigjaymalcolm.townyreputation;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import me.bigjaymalcolm.townyreputation.listeners.PlayerEventListener;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.object.TownyUniverse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Main extends JavaPlugin
{
    public static Towny TownyPlugin;
    public static TownyUniverse TownyUniverse;

    public static Map<String, FileConfiguration> PlayerReputations;

    @Override
    public void onEnable()
    {
        PlayerReputations = new HashMap<String, FileConfiguration>();

        PluginManager pm = getServer().getPluginManager();
        TownyPlugin = (Towny)pm.getPlugin("Towny");
        TownyUniverse = TownyPlugin.getTownyUniverse();

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("getreputation"))
        {
            try
            {
                Player player = (Player)sender;
                String playerName = player.getName();
                FileConfiguration playerData = PlayerReputations.get(playerName);

                player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Towny Reputations");
                player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Nation Reputations");

                for (Nation nation : TownyUniverse.getDataSource().getNations())
                {
                    if (playerData.contains("reputation." + nation.getName().toLowerCase()))
                    {
                        Object reputation = playerData.get("reputation." + nation.getName().toLowerCase());
                        player.sendMessage(ChatColor.BOLD + nation.getName() + ": " + ChatColor.RESET + reputation.toString());
                    }
                    else
                    {
                        player.sendMessage(ChatColor.BOLD + nation.getName() + ": " + ChatColor.RESET + "0");
                    }
                }

                player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Town Reputations");

                for (Town town : TownyUniverse.getDataSource().getTowns())
                {
                    if (playerData.contains("reputation." + town.getName().toLowerCase()))
                    {
                        Object reputation = playerData.get("reputation." + town.getName().toLowerCase());
                        player.sendMessage(ChatColor.BOLD + town.getName() + ": " + ChatColor.RESET + reputation.toString());
                    }
                    else
                    {
                        player.sendMessage(ChatColor.BOLD + town.getName() + ": " + ChatColor.RESET + "0");
                    }
                }
                return true;
            }
            catch (Exception e) { }
        }
        else if (cmd.getName().equalsIgnoreCase("setreputation"))
        {
            try
            {
                Player player = Bukkit.getPlayer(args[0]);
                String town = args[1].toLowerCase();

                if (TownyUniverse.getDataSource().hasTown(town) || TownyUniverse.getDataSource().hasNation(town))
                {
                    Integer reputation = Integer.parseInt(args[2]);
                    String playerName = player.getName();
                    FileConfiguration playerData = PlayerReputations.get(playerName);

                    playerData.set("reputation." + town, reputation);

                    getLogger().info(playerName + "'s reputation towards " + town + " has been set to " + reputation.toString() + " by " + sender.getName()); // Log the action
                    sender.sendMessage("You have altered " + playerName + "'s reputation towards " + town + " to " + reputation.toString()); // Let the command sender know what they did
                    player.sendMessage("Your reputation towards " + town + " has been set to " + reputation.toString()); // Inform the player that their reputation has changed
                    return true;
                }
                else
                {
                    player.sendMessage("That town/nation does not exist");
                    return true;
                }
            }
            catch (Exception e) { }

            return false;
        }

        return false;
    }
}