package cz.saves.AuthMeV2;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.io.IOException;

public class unregister implements CommandExecutor {

    JoinRegister plugin;
    public unregister(JoinRegister plugin) { this.plugin = plugin;}

    public boolean onCommand(CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Musíš být hráč!!");
            return true;
        }


        Player player = (Player) sender;
        if (cmd.getName().startsWith("unregister") && player.isOp() && plugin.names.contains(player.getName())
                && !plugin.effect.contains(player.getName())){
            player.sendMessage(player.getName());

            if (args.length==0){
                player.sendMessage("§c§lCHYBA! §7Musíš zadat nick!");
                return true;
            }

            String nick = args[0];

            if (nick.equalsIgnoreCase(player.getName())){
                player.sendMessage("§c§lCHYBA! §7Nemůžeš odregistrovat sám sebe!");
                return true;
            }
            if (!plugin.names.isSet(nick)){
                player.sendMessage("§c§lCHYBA! §7Tento hráč není zaregistrovaný!");
                return true;
            }

            if (plugin.names.isSet(nick)){
                player.sendMessage("§aHráč odregistrován!");
                plugin.names.set(nick, null);
                try {
                    plugin.names.save(plugin.file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


        return true;
    }
}
