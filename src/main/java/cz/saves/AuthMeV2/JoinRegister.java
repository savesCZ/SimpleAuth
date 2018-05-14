package cz.saves.AuthMeV2;

import org.bukkit.plugin.java.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.potion.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.YamlConfiguration;

public class JoinRegister extends JavaPlugin implements CommandExecutor, Listener
{
    String registerMsg;

    public JoinRegister() {
        this.registerMsg = "§7Prosím, zaregistruj se příkazem §c§l/register §c<tveheslo> <znovuheslo>";
    }
    public static JoinRegister instance;



    List<String> effect = new ArrayList<String>();
    public YamlConfiguration names;
    public File file;

    public void onEnable() {
        instance = this;
        this.getCommand("register").setExecutor(this);
        this.getCommand("login").setExecutor(new Login(this));
        this.getCommand("unregister").setExecutor(new unregister(this));
        this.getCommand("changepassword").setExecutor(new changepass(this));
        this.getLogger().info("Zapínám se");
        this.getServer().getPluginManager().registerEvents(this,this);
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        file = new File(getDataFolder() + "/players.yml");
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        names = YamlConfiguration.loadConfiguration(file);
    }

    public static JoinRegister getInstance(){
        return instance;
    }

    public void onDisable() {

        this.getLogger().info("Vypínám se");
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 99999999, 100));
        //player.sendMessage(this.registerMsg);
        effect.add(player.getName());
        if (names.contains(player.getName())){
            player.sendMessage("§cPřihlaš se příkazem /login <heslo>");
        }
        else {
            player.sendMessage(this.registerMsg);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
         Player player = event.getPlayer();
        if (effect.contains(player.getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void blockJoinCommand(final PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().toLowerCase();
        if (message.startsWith("/register") || message.startsWith("/login") || message.startsWith("/unregister")) {
            return;
        }
        if (effect.contains(player.getName())) {
            player.sendMessage("§cCHYBA! tento příkaz nemůže být vykonán!");
            event.setCancelled(true);
        }
        if (message.startsWith("/register") && !effect.contains(player.getName())){
            player.sendMessage("§cNemůžeš se již přihlásit nebo zaregistrovat!");
            event.setCancelled(true);
        }
        if (!names.contains(player.getName()) && message.startsWith("/login")){
            player.sendMessage("§cJeště nejsi ani zaregistrovaný!");
            event.setCancelled(true);
        }
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Musíš být hráč!!");
            return true;
        }
        final Player player = (Player)sender;
        if (cmd.getName().startsWith("register")) {
            player.sendMessage(player.getName());

            if (names.contains(player.getName()) && effect.contains(player.getName())){
                player.sendMessage("§cJiž jsi zaregistrovaný!");
                player.sendMessage("§cPřihlaš se příkazem /login <heslo>");
                return true;
            }
            if (!effect.contains(player.getName())){
                player.sendMessage("§cJiž jsi zaregistrovaný!");
                return true;
            }

            if (!names.contains(player.getName()) && args.length==0 || args.length==1){
                player.sendMessage("§c§lCHYBA! " + registerMsg);
                return true;
            }

           if (args[0].equals(args[1])){
               effect.remove(player.getName());
               player.removePotionEffect(PotionEffectType.BLINDNESS);
               player.sendMessage("§aVýborně! jsi zaregistrovaný!!");
               names.set(player.getName(), args[0]);
               try {
                   names.save(file);
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
           else {
               player.sendMessage("§c§lCHYBA! " + registerMsg);
           }


        }
        return true;
    }


}