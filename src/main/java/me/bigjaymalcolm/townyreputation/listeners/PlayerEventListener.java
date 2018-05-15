package me.bigjaymalcolm.townyreputation.listeners;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import me.bigjaymalcolm.townyreputation.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;

public class PlayerEventListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException, InvalidConfigurationException
    {
        Player player = event.getPlayer();
        String playerName = player.getName();
        File userdata = new File(Main.TownyPlugin.getDataFolder(), File.separator + "TownyReputation");
        File file = new File(userdata, File.separator + playerName + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(file);

        // If the player does not yet have a reputation file
        if (!file.exists())
        {
            try
            {
                for (Town town : Main.TownyTowns)
                {
                    playerData.set("reputation." + town.getName().toLowerCase(), 0);
                }

                for (Nation nation : Main.TownyNations)
                {
                    playerData.set("reputation." + nation.getName().toLowerCase(), 0);
                }

                playerData.save(file);
            }
            catch (IOException exception) { exception.printStackTrace(); }
        }

        Main.PlayerReputations.put(playerName, playerData);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        // Get the leaving player's name
        String playerName = event.getPlayer().getName();

        // Save player's data
        File userdata = new File(Main.TownyPlugin.getDataFolder(), File.separator + "TownyReputation");
        File file = new File(userdata, File.separator + playerName + ".yml");
        FileConfiguration playerData = Main.PlayerReputations.get(playerName);

        try {
            playerData.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the current player from active users
        Main.PlayerReputations.remove(event.getPlayer().getName());
    }
}