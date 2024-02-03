package dev.gomorrha.fireworkshowplus2.objects.fireworks;

import dev.gomorrha.fireworkshowplus2.FireworkShowPlus2;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.HashMap;
import java.util.Map;

public class NormalFireworks extends Fireworks {

    private FireworkMeta meta;
    private boolean isMarked = false;

    public NormalFireworks(FireworkMeta meta, Location loc) {
        super(loc);
        this.meta = meta;
    }

    public NormalFireworks(NormalFireworks f) {
        super(f);
        meta = f.getMeta().clone();
    }

    public FireworkMeta getMeta() {
        return meta;
    }

    @Override
    public void play(boolean highest) {
        Location newLoc = loc;
        if(highest) { newLoc = newLoc.getWorld().getHighestBlockAt(newLoc).getLocation().add(0.5,1,0.5); } // If the highest parameter is true for the show, it will spawn the entity at the highest block location.
        Firework firework = (org.bukkit.entity.Firework) loc.getWorld().spawnEntity(newLoc, EntityType.FIREWORK);
        firework.setFireworkMeta(meta);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("meta", meta);
        map.put("location", loc);

        return map;
    }

    public static NormalFireworks deserialize(Map<String, Object> args) {
        FireworkMeta meta = (FireworkMeta) args.get("meta");
        NormalFireworks nf = new NormalFireworks(meta, (Location) args.get("location"));
        return nf;
    }

    public void toggleMark(Boolean highest) {
        isMarked = !isMarked;
        if (isMarked) {
            if(highest) {
                Location highestLoc = loc.getWorld().getHighestBlockAt(loc).getLocation().add(0.5, 1, 0.5);
                spawnContinuousParticles(loc.getWorld(), highestLoc);
            } else
                spawnContinuousParticles(loc.getWorld(), loc);
        }
    }
    private void spawnContinuousParticles(World world, Location loc) {
        Particle particle = Particle.REDSTONE;
        Color color = Color.RED; // Change this to the desired color

        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1);

        new org.bukkit.scheduler.BukkitRunnable() {
            @Override
            public void run() {
                if (!isMarked) {
                    this.cancel();
                } else {
                    world.spawnParticle(particle, loc, 1, 0, 0, 0, 1, dustOptions);
                }
            }
        }.runTaskTimer(FireworkShowPlus2.fws, 0, 20);
    }

}