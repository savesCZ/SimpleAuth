package cz.saves.AuthMeV2;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
public class Login implements CommandExecutor {

    JoinRegister plugin;
    public Login(JoinRegister plugin) { this.plugin = plugin;}



    public boolean onCommand(CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Musíš být hráč!!");
            return true;
        }

        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("login")){
            if (!plugin.effect.contains(player.getName())){
                player.sendMessage("§c§lCHYBA!§7 Již jsi přihlášen!");
                return true;
            }
            if (args.length==0 && plugin.names.contains(player.getName())){
                player.sendMessage("§c§lChyba! §cNesprávné heslo!");
                player.sendMessage("§c Použij /login <heslo>");
                return true;
            }

            if (plugin.names.contains(player.getName()) && args[0].equals(plugin.names.getString(player.getName()))){
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                plugin.effect.remove(player.getName());
                player.sendMessage("§aVýborně! jsi přihlášený!!");
            }

            else  {
                player.sendMessage("§c§lCHYBA! §cNesprávné heslo!");
                player.sendMessage("§c Použij /login <heslo>");
                return true;
            }


        }
        return true;
    }
}
