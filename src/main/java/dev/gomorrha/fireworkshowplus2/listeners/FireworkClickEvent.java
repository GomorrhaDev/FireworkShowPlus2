package dev.gomorrha.fireworkshowplus2.listeners;

import dev.gomorrha.fireworkshowplus2.FireworkShowPlus2;
import dev.gomorrha.fireworkshowplus2.objects.fireworks.NormalFireworks;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkClickEvent implements Listener {
    @EventHandler
    public void onFireworkClick(PlayerInteractEvent e) {
        if (e.getItem() != null && e.getItem().getType() == Material.FIREWORK_ROCKET && FireworkShowPlus2.getPlaceMode()) {
            e.setCancelled(true);

            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Player player = e.getPlayer();
                Location location = e.getClickedBlock().getLocation();
                String showName = player.getMetadata("fireworkshow_place_showname").get(0).asString();
                int frameId = player.getMetadata("fireworkshow_place_frameid").get(0).asInt();

                NormalFireworks nf = new NormalFireworks((FireworkMeta) e.getItem().getItemMeta(), location);
                FireworkShowPlus2.getShows().get(showName).frames.get(frameId - 1).add(nf);
                player.sendMessage(ChatColor.GREEN + "You added a firework to the Firework Show with name " + ChatColor.DARK_GREEN + showName
                        + ChatColor.GREEN + " on frame (#" + ChatColor.YELLOW + frameId + ChatColor.GREEN + ")!");

                FireworkShowPlus2.getShowFiles().set(showName, FireworkShowPlus2.getShows().get(showName));
            }
        }
    }
}
