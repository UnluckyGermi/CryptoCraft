package main;


import commands.CoinCommandManager;
import commands.CryptoCommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import commands.CommandManager;
import tabcompleters.CoinCommandCompleter;
import tabcompleters.CryptoCommandCompleter;
import utils.MainListener;
import utils.TaskManager;


public class Main extends JavaPlugin {

    public static Main plugin;

    private void loadCommands(){

        PluginCommand crypto = getCommand("crypto");

        if(crypto != null) {
            crypto.setExecutor(new CryptoCommandManager());
            crypto.setTabCompleter(new CryptoCommandCompleter());
        }

        CommandManager commandManager = new CommandManager();
        PluginCommand bal = getCommand("bal");
        PluginCommand cbal = getCommand("cbal");

        if(bal != null) bal.setExecutor(commandManager);
        if(cbal != null) cbal.setExecutor(commandManager);

        PluginCommand coin = getCommand("coin");
        if(coin != null){
            coin.setExecutor(new CoinCommandManager());
            coin.setTabCompleter(new CoinCommandCompleter());
        }

    }

    @Override
    public void onEnable() {
        plugin = this;
        this.getServer().getPluginManager().registerEvents(new MainListener(), this);
        this.saveDefaultConfig();

        loadCommands();

        UserManager.createUsersFile();
        UserManager.loadUserData();

        TaskManager.apiConsumeTask.runTaskTimer(this, 0L, 72000L);

    }

    @Override
    public void onDisable(){
        UserManager.saveUserData();
    }


}
