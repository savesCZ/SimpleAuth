package cz.saves.AuthMeV2;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class changepass implements CommandExecutor {
    JoinRegister plugin;
    public changepass(JoinRegister plugin) { this.plugin = plugin;}

    public boolean onCommand(CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Musíš být hráč!!");
            return true;
        }

        Player player = (Player) sender;
        if (cmd.getName().startsWith("changepassword")){
            if (args.length==0 || args.length==1){
                player.sendMessage("§c§lCHYBA! §7Použij §c/changepassword <stareheslo> <noveheslo>");
                return true;
            }
            String configPass = plugin.names.getString(player.getName()); //getovaný heslo z configu
            String oldPass = args[0];
            String newPass = args[1];
            if (!oldPass.equals(configPass)){
                player.sendMessage("§c§lCHYBA! §7Nesprávné staré heslo!");
                return true;
            }
            if (configPass.equals(oldPass) && oldPass.equals(newPass)){
                player.sendMessage("§c§lCHYBA! §7Vždyť je to stejné heslo!");
                return true;
            }
            if (args[0].equals(configPass)){
                plugin.names.set(player.getName(), null);
                plugin.names.set(player.getName(),newPass);
                player.sendMessage("§aHeslo změněno!");

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
