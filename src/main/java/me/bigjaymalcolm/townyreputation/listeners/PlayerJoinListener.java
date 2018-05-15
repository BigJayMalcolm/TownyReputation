package me.bigjaymalcolm.townyreputation.listeners;

import me.bigjaymalcolm.townyreputation.Main;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import me.bigjaymalcolm.townyreputation.reputation.PlayerReputation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.MetadataValue;

import java.io.File;
import java.io.IOException;

public class PlayerJoinListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
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

        // Reload the file in case it was a new olayer and was just created
        YamlConfiguration.loadConfiguration(file);

        PlayerReputation playerReputation = new PlayerReputation();

        for (Town town : Main.TownyTowns) { playerReputation.Reputations.put(town.getName(), (Integer)playerData.get("reputation." + town.getName().toLowerCase())); }
        for (Nation nation : Main.TownyNations) { playerReputation.Reputations.put(nation.getName(), (Integer)playerData.get("reputation." + nation.getName().toLowerCase())); }

        player.setMetadata("PlayerReputation", (MetadataValue)playerReputation);
    }
}