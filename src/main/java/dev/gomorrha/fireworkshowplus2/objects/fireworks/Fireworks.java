package dev.gomorrha.fireworkshowplus2.objects.fireworks;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public abstract class Fireworks implements ConfigurationSerializable {

    public Location loc;

    public Fireworks(Location loc) {
        this.loc = loc;
    }

    public Fireworks(Fireworks f) { loc = f.getPos().clone(); }

    public abstract void play(boolean highest);

    public void setPos(Location loc) {
        this.loc = loc;
    }
    public Location getPos() {
        return loc;
    }

}