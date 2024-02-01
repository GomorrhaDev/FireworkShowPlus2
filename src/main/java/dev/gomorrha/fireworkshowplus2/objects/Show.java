package dev.gomorrha.fireworkshowplus2.objects;

import dev.gomorrha.fireworkshowplus2.FireworkShowPlus2;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Show implements ConfigurationSerializable {

    public ArrayList<Frame> frames = new ArrayList<Frame>();
    private ArrayList<Integer> taskids = new ArrayList<Integer>();
    private boolean running = false;
    public boolean highest = false;

    public Show() {

    }

    public Show(Show show) {
        frames = (ArrayList<Frame>) show.frames.clone();
        highest = show.highest;
    }

    public void play() {
        if ( running ) return;
        running = true;

        long current = 0;
        for ( final Frame f : frames ) {
            current += f.getDelay();
            taskids.add(Bukkit.getScheduler().scheduleSyncDelayedTask(FireworkShowPlus2.fws, new Runnable() {
                @Override
                public void run() {
                    f.play(highest);
                }
            }, current));
        }

        taskids.add(Bukkit.getScheduler().scheduleSyncDelayedTask(FireworkShowPlus2.fws, new Runnable() {
            @Override
            public void run() {
                running = false;
                taskids.clear();
            }
        }, current));
    }

    public void stop() {
        if ( !running ) return;

        for ( int id : taskids ) {
            Bukkit.getScheduler().cancelTask(id);
        }
    }

    public boolean isRunning() {
        return running;
    }


    public boolean getHighest() {
        return highest;
    }

    public void setHighest(boolean highest) {
        this.highest = highest;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("highest", highest);
        map.put("frames", frames);

        return map;
    }

    public static Show deserialize(Map<String, Object> args) {
        boolean highest = (Boolean) args.get("highest");
        Show show = new Show();
        show.setHighest(highest);
        for ( Frame f : (ArrayList<Frame>) args.get("frames") ) {
            show.frames.add(f);
        }
        return show;
    }
}