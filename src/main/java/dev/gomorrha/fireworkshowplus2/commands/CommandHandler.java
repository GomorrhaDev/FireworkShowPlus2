package dev.gomorrha.fireworkshowplus2.commands;

import dev.gomorrha.fireworkshowplus2.FireworkShowPlus2;
import dev.gomorrha.fireworkshowplus2.objects.Frame;
import dev.gomorrha.fireworkshowplus2.objects.Show;
import dev.gomorrha.fireworkshowplus2.objects.fireworks.NormalFireworks;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommandHandler implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (cmd.getName().equalsIgnoreCase("fireworkshow")) {
            if (args.length == 1) {
                completions.add("create");
                completions.add("delete");
                completions.add("addframe");
                completions.add("delframe");
                completions.add("dupframe");
                completions.add("newfw");
                completions.add("play");
                completions.add("playframe");
                completions.add("stop");
                completions.add("list");
                completions.add("highest");
                completions.add("place");
                completions.add("mark");
                return completions;
            } else if (args.length == 2) {
                String subcommand = args[0].toLowerCase();

                if (subcommand.equals("delete") || subcommand.equals("play") || subcommand.equals("stop") || subcommand.equals("highest") || subcommand.equals("place")) {
                    for (String showName : FireworkShowPlus2.getShows().keySet()) {
                        completions.add(showName);
                    }
                } else if (subcommand.equals("addframe") || subcommand.equals("delframe") || subcommand.equals("dupframe") || subcommand.equals("newfw")) {
                    for (String showName : FireworkShowPlus2.getShows().keySet()) {
                        completions.add(showName);
                    }
                } else if (subcommand.equals("mark")) {
                    for (String showName : FireworkShowPlus2.getShows().keySet()) {
                        completions.add(showName);
                    }
                } else if (subcommand.equals("playframe")) {
                    for (String showName : FireworkShowPlus2.getShows().keySet()) {
                        completions.add(showName);
                    }
                }

                return completions;

            } else if (args.length == 3 && args[0].equalsIgnoreCase("place")) {
                String showName = args[1].toLowerCase();
                if (FireworkShowPlus2.getShows().containsKey(showName)) {
                    int frameCount = FireworkShowPlus2.getShows().get(showName).frames.size();
                    for (int i = 1; i <= frameCount; i++) {
                        completions.add(String.valueOf(i));
                    }
                }
            }
        }

        return completions;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!cmd.getName().equalsIgnoreCase("fireworkshow")) { return true; }

        //Help command for Fireworks Shows
        if (args.length < 1 || (args.length == 1 && args[0].equals("help")))
        {
            sender.sendMessage(ChatColor.GREEN + "/fws create <showname>" + ChatColor.GRAY + " Create a new fireworkshow");
            sender.sendMessage(ChatColor.GREEN + "/fws play <showname>" + ChatColor.GRAY + " Start a fireworkshow");
            sender.sendMessage(ChatColor.GREEN + "/fws place <showname> <frameid>" + ChatColor.GRAY + " Start/Stop the placing mode for a frame");
            sender.sendMessage(ChatColor.GREEN + "/fws delete <showname>" + ChatColor.GRAY + " Delete a fireworkshow");
            sender.sendMessage(ChatColor.GREEN + "/fws addframe <showname> <delay>" + ChatColor.GRAY + " Add a frame to a show");
            sender.sendMessage(ChatColor.GREEN + "/fws delframe <showname> <frameid>" + ChatColor.GRAY + " Delete a frame from a show");
            sender.sendMessage(ChatColor.GREEN + "/fws dupframe <showname> <frameid>" + ChatColor.GRAY + " Duplicate a frame from a show");
            sender.sendMessage(ChatColor.GREEN + "/fws newfw <showname> (<frameid>)" + ChatColor.GRAY + " Set a firework on your location for a show");
            sender.sendMessage(ChatColor.GREEN + "/fws highest <showname> <true/false>" + ChatColor.GRAY + " Set if the fireworks will spawn on the highest block available or where it was initially placed");
            return true;
        }

        //Play Fireworks show command
        if (args[0].equalsIgnoreCase("play"))
        {
            if ( args.length < 2 )
            {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " play <showname>");
                return true;
            }

            if (!sender.hasPermission("fireworkshow.play"))
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            if ( !FireworkShowPlus2.getShows().containsKey(args[1].toLowerCase()) )
            {
                sender.sendMessage(ChatColor.RED + "That fireworkshow does not exist!");
                return true;
            }

            Show show = FireworkShowPlus2.getShows().get(args[1].toLowerCase());

            if ( show.isRunning() )
            {
                sender.sendMessage(ChatColor.GREEN + "fireworkshow " + ChatColor.DARK_GREEN + args[1].toLowerCase() + ChatColor.GREEN + " is already running!");
                return true;
            }

            show.play();
            sender.sendMessage(ChatColor.GREEN + "You started the " + ChatColor.DARK_GREEN + args[1].toLowerCase() + ChatColor.GREEN + " Firework show!");
            return true;
        }

        //Stop Fireworks show command
        if (args[0].equalsIgnoreCase("stop"))
        {
            if ( args.length != 2 )
            {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " stop <showname>");
                return true;
            }

            if (!sender.hasPermission("fireworkshow.stop"))
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            if ( args[1].equalsIgnoreCase("all") )
            {
                for ( Show show : FireworkShowPlus2.getShows().values() )
                {
                    show.stop();
                }

                sender.sendMessage(ChatColor.GREEN + "You stopped all fireworkshows!");
                return true;
            }

            if ( !FireworkShowPlus2.getShows().containsKey(args[1].toLowerCase()) )
            {
                sender.sendMessage(ChatColor.RED + "That fireworkshow does not exist!");
                return true;
            }

            FireworkShowPlus2.getShows().get(args[1].toLowerCase()).stop();
            sender.sendMessage(ChatColor.GREEN + "You stopped fireworkshow " + ChatColor.DARK_GREEN + args[1].toLowerCase() + ChatColor.GREEN + "!");
            return true;
        }

        //Create Fireworks command
        if ( args[0].equalsIgnoreCase("create"))
        {
            if ( args.length != 2 )
            {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " create <showname>");
                return true;
            }

            if ( !sender.hasPermission("fireworkshow.create") )
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            String name = args[1].toLowerCase();

            if ( FireworkShowPlus2.getShows().containsKey(name) )
            {
                sender.sendMessage(ChatColor.RED + "A show with that name already exists!");
                return true;
            }

            try {
                FireworkShowPlus2.createShow(name);
                sender.sendMessage(ChatColor.GREEN + "You created a fireworkshow with name " + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
            } catch (Exception e) {
                System.out.println(e.toString());
                sender.sendMessage(ChatColor.RED + "An error occurred while attempting to create a firework show!");
            }
        }

        //Delete Fireworks Command
        else if (args[0].equalsIgnoreCase("delete"))
        {
            if (args.length != 2 )
            {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " delete <showname>");
                return true;
            }

            if (!sender.hasPermission("fireworkshow.delete"))
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();

            if (!FireworkShowPlus2.getShows().containsKey(name))
            {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }

            FireworkShowPlus2.getShows().remove(name);
            FireworkShowPlus2.getShowFiles().set(name, null);
            sender.sendMessage(ChatColor.GREEN + "You deleted the Fireworks show " + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
        }

        //Add an Empty frame for a show with a delay
        else if (args[0].equalsIgnoreCase("addframe"))
        {
            if (args.length != 3)
            {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " addframe <showname> <delay>");
                return true;
            }

            if (!sender.hasPermission("fireworkshow.addframe"))
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();

            if (!FireworkShowPlus2.getShows().containsKey(name)) {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }

            if (!args[2].matches("[0-9]+"))
            {
                sender.sendMessage(ChatColor.RED + "Invalid delay!");
                return true;
            }
            long delay = Long.valueOf(args[2]);

            if (delay > 600)
            {
                sender.sendMessage(ChatColor.RED + "The delay can't be longer than 30 seconds!");
                return true;
            }

            FireworkShowPlus2.getShows().get(name).frames.add(new Frame(delay));
            FireworkShowPlus2.getShowFiles().set(name, FireworkShowPlus2.getShows().get(name));
            sender.sendMessage(ChatColor.GREEN + "You added a new frame (#" + ChatColor.YELLOW + FireworkShowPlus2.getShows().get(name).frames.size() + ChatColor.GREEN + ") " +
                    "to the firworkshow with name " + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
        }

        //Delete a frame from the show
        else if (args[0].equalsIgnoreCase("delframe"))
        {
            if (args.length != 3)
            {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " delframe <showname> <frameid>");
                return true;
            }
            //Check for Delete Frame permission
            if (!sender.hasPermission("fireworkshow.delframe"))
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();

            //Show does not exist
            if (!FireworkShowPlus2.getShows().containsKey(name))
            {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }
            //Frame does not exist
            if ( !args[2].matches("[0-9]+") ||FireworkShowPlus2.getShows().get(name).frames.size() < Integer.valueOf(args[2]) )
            {
                sender.sendMessage(ChatColor.RED + "That frame does not exists!");
                return true;
            }
            int frame = Integer.valueOf(args[2]);

            FireworkShowPlus2.getShows().get(name).frames.remove(frame-1);
            FireworkShowPlus2.getShowFiles().set(name, FireworkShowPlus2.getShows().get(name));
            sender.sendMessage(ChatColor.GREEN + "You removed a frame from the fireworkshow with name" + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
        }
        //Duplicate last frame in the show
        else if ( args[0].equalsIgnoreCase("dupframe") )
        {
            if (args.length != 3)
            {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " dupframe <showname> <frameid>");
                return true;
            }

            if (!sender.hasPermission("fireworkshow.dupframe"))
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();

            if (!FireworkShowPlus2.getShows().containsKey(name))
            {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }

            if (!args[2].matches("[0-9]+") ||FireworkShowPlus2.getShows().get(name).frames.size() < Integer.valueOf(args[2]))
            {
                sender.sendMessage(ChatColor.RED + "That frame does not exists!");
                return true;
            }
            int frameid = Integer.valueOf(args[2]);

            Frame frame = new Frame(FireworkShowPlus2.getShows().get(name).frames.get(frameid-1));
            FireworkShowPlus2.getShows().get(name).frames.add(frame);
            FireworkShowPlus2.getShowFiles().set(name, FireworkShowPlus2.getShows().get(name));
            sender.sendMessage(ChatColor.GREEN + "You duplicated a frame (#" + ChatColor.YELLOW + FireworkShowPlus2.getShows().get(name).frames.size() + ChatColor.GREEN + ") " +
                    "from the fireworkshow with name " + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
        }

        //Add new held firework into last, or specified frame.
        else if (args[0].equalsIgnoreCase("newfw"))
        {

            if (args.length < 2)
            {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " newfw <showname>");
                return true;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "This command can only be executed by players!");
                return true;
            }

            if (!sender.hasPermission("fireworkshow.newfw"))
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();

            if (!FireworkShowPlus2.getShows().containsKey(name))
            {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }

            if (FireworkShowPlus2.getShows().get(name).frames.isEmpty()) {
                sender.sendMessage(ChatColor.RED + "There are no frames in this show!");
                return true;
            }

            int frame;
            if (args.length == 3)
            {
                if (!args[2].matches("[0-9]+") || FireworkShowPlus2.getShows().get(name).frames.size() < Integer.valueOf(args[2]))
                {
                    sender.sendMessage(ChatColor.RED + "That frame does not exists!");
                    return true;
                }
                frame = Integer.valueOf(args[2]);
            }
            else
            {
                frame = FireworkShowPlus2.getShows().get(name).frames.size();
            }
            Player p = (Player) sender;
            if (p.getInventory().getItemInMainHand().getType() != Material.FIREWORK_ROCKET)
            {
                sender.sendMessage(ChatColor.RED + "Please hold a firework or firework charge in your hand!");
                return true;
            }
            FireworkMeta meta = (FireworkMeta) p.getInventory().getItemInMainHand().getItemMeta();
            NormalFireworks nf = new NormalFireworks(meta, p.getLocation());
            FireworkShowPlus2.getShows().get(name).frames.get(frame-1).add(nf);
            sender.sendMessage(ChatColor.GREEN + "You added firework to the Firework Show with name " + ChatColor.DARK_GREEN + name
                    + ChatColor.GREEN + " on frame (#" + ChatColor.YELLOW + frame + ChatColor.GREEN + ")!");

            FireworkShowPlus2.getShowFiles().set(name, FireworkShowPlus2.getShows().get(name));
        }

        else if(args[0].equalsIgnoreCase("list"))
        {
            Set<String> set = FireworkShowPlus2.getShowFiles().getKeys(false);
            String[] list = set.toArray(new String[set.size()]);

            int maxItems = 10;
            int page = 0;
            if (args.length > 1) { page = Integer.parseInt(args[1]) - 1; }

            if (page > Math.ceil(list.length/maxItems)|| page < 0)
            {
                sender.sendMessage(ChatColor.GREEN + "No fireworks found! Try /fws create <fireworks_id>."); //Replace with more user friendly message.
                return true;
            }
            sender.sendMessage("Firework Shows || Page " + (page+1));
            sender.sendMessage("-----------------------"); //Format this shit too
            for(int i=0;i<maxItems;i++)
            {
                if( (page*maxItems) + i >= list.length ) { break; }
                sender.sendMessage(list[(page*maxItems) + i]);
            }
        }

        else if ( args[0].equalsIgnoreCase("highest") ) {
            if ( args.length != 3 ) {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " highest <showname> <true/false>");
                return true;
            }

            if ( !sender.hasPermission("fireworkshow.highest") ) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            String name = args[1].toLowerCase();


            if ( !FireworkShowPlus2.getShows().containsKey(name) ) {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exists!");
                return true;
            }

            if ( args[2].equalsIgnoreCase("true") ) {
                FireworkShowPlus2.getShows().get(name).setHighest(true);
            } else if ( args[2].equalsIgnoreCase("false")) {
                FireworkShowPlus2.getShows().get(name).setHighest(false);
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid input! Use 'true' or 'false'.");
                return true;
            }
            FireworkShowPlus2.getShowFiles().set(name, FireworkShowPlus2.getShows().get(name));
            sender.sendMessage(ChatColor.GREEN + "You set highest parameter for '" + ChatColor.DARK_GREEN + name
                    + ChatColor.GREEN + "' to " + FireworkShowPlus2.getShows().get(name).getHighest() + ".");
        }

        else if (args[0].equalsIgnoreCase("place")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "This command can only be executed by players!");
                return true;
            }

            Player player = (Player) sender;

            if (!sender.hasPermission("fireworkshow.newfw"))
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            if(FireworkShowPlus2.getPlaceMode()){
                FireworkShowPlus2.togglePlaceMode(player);
                return true;
            }

            if (args.length != 3 ) {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " place <showname> <frameid>");
                return true;
            }

            String name = args[1].toLowerCase();

            if (!FireworkShowPlus2.getShows().containsKey(name)) {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exist!");
                return true;
            }

            int frameId;
            if (!args[2].matches("[0-9]+") || FireworkShowPlus2.getShows().get(name).frames.size() < Integer.valueOf(args[2])) {
                sender.sendMessage(ChatColor.RED + "That frame does not exist!");
                return true;
            }
            frameId = Integer.valueOf(args[2]);

            if(!FireworkShowPlus2.getPlaceMode()) {
                FireworkShowPlus2.togglePlaceMode(player); // Enable place mode
                player.sendMessage(ChatColor.GREEN + "Now click on the ground where you want to place the firework.");
                player.setMetadata("fireworkshow_place_showname", new FixedMetadataValue(FireworkShowPlus2.fws, name));
                player.setMetadata("fireworkshow_place_frameid", new FixedMetadataValue(FireworkShowPlus2.fws, frameId));
            }
        }

        else if (args[0].equalsIgnoreCase("mark")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "This command can only be executed by players!");
                return true;
            }

            if (!sender.hasPermission("fireworkshow.mark")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            if (args.length != 3) {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " mark <showname> <frameid>");
                return true;
            }

            String name = args[1].toLowerCase();

            if (!FireworkShowPlus2.getShows().containsKey(name)) {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exist!");
                return true;
            }

            int frameId;
            if (!args[2].matches("[0-9]+") || FireworkShowPlus2.getShows().get(name).frames.size() < Integer.valueOf(args[2])) {
                sender.sendMessage(ChatColor.RED + "That frame does not exist!");
                return true;
            }
            frameId = Integer.valueOf(args[2]);

            Player player = (Player) sender;
            World world = player.getWorld();
            Location center = player.getLocation();

            Frame frame = FireworkShowPlus2.getShows().get(name).frames.get(frameId - 1);
            frame.markPositions(world, center, name);

            sender.sendMessage(ChatColor.GREEN + "Marked positions for frame #" + ChatColor.YELLOW + frameId + ChatColor.GREEN + " in the Firework Show with name " + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
        }

        else if (args[0].equalsIgnoreCase("playframe"))
        {
            if (args.length != 3)
            {
                sender.sendMessage(ChatColor.RED + "Invalid arguments, you should try " + ChatColor.DARK_RED + "/" + cmd.getName() + " playframe <showname> <frameid>");
                return true;
            }

            if (!sender.hasPermission("fireworkshow.play"))
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            String name = args[1].toLowerCase();

            if (!FireworkShowPlus2.getShows().containsKey(name))
            {
                sender.sendMessage(ChatColor.RED + "A show with that name doesn't exist!");
                return true;
            }

            int frameId;
            if (!args[2].matches("[0-9]+") || FireworkShowPlus2.getShows().get(name).frames.size() < Integer.valueOf(args[2]))
            {
                sender.sendMessage(ChatColor.RED + "That frame does not exist!");
                return true;
            }
            frameId = Integer.valueOf(args[2]);

            Show show = FireworkShowPlus2.getShows().get(name);
            Frame frame = show.frames.get(frameId - 1);

            frame.play(FireworkShowPlus2.getShows().get(name).getHighest());
            sender.sendMessage(ChatColor.GREEN + "You played frame #" + ChatColor.YELLOW + frameId + ChatColor.GREEN + " from the Firework Show with name " + ChatColor.DARK_GREEN + name + ChatColor.GREEN + "!");
        }

        FireworkShowPlus2.saveShows();
        return true;
    }

}