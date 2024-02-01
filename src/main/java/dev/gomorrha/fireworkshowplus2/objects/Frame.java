package dev.gomorrha.fireworkshowplus2.objects;

import dev.gomorrha.fireworkshowplus2.objects.fireworks.Fireworks;
import dev.gomorrha.fireworkshowplus2.objects.fireworks.NormalFireworks;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Frame implements ConfigurationSerializable {

    public ArrayList<NormalFireworks> fireworks = new ArrayList<NormalFireworks>();
    public long delay;

    public Frame(long delay) {
        this.delay = delay;
    }

    public Frame(Frame frame) {
        for ( NormalFireworks f : frame.fireworks) {
            fireworks.add(new NormalFireworks(f));
        }
        delay = frame.delay;
    }

    public long getDelay() {
        return delay;
    }

    public void play(boolean highest) {
        for ( Fireworks fw : fireworks ) {
            fw.play(highest);
        }
    }

    public void add(NormalFireworks fw) {
        fireworks.add(fw);
    }

    public void remove(int i) {
        fireworks.remove(i);
    }

    public void remove(Fireworks fw) {
        fireworks.remove(fw);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("delay", delay);
        map.put("fireworks", fireworks);

        return map;
    }

    public static Frame deserialize(Map<String, Object> args) {
        long delay = ((Number) args.get("delay")).longValue();
        Frame frame = new Frame(delay);
        for ( NormalFireworks fw : (ArrayList<NormalFireworks>) args.get("fireworks") ) {
            frame.add(fw);
        }
        return frame;
    }
}