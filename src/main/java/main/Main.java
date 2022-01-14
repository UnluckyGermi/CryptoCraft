package main;

import org.bukkit.plugin.java.JavaPlugin;
import utils.CommandManager;
import utils.MainListener;


public class Main extends JavaPlugin {

    public static Main plugin;



    @Override
    public void onEnable() {
        plugin = this;
        this.getServer().getPluginManager().registerEvents(new MainListener(), this);



        CommandManager cm = new CommandManager();
        getCommand("bal").setExecutor(cm);
        getCommand("crypto").setExecutor(cm);
        getCommand("cbal").setExecutor(cm);
        this.saveDefaultConfig();

        UserManager.createUsersFile();
        UserManager.loadUserData();

        CryptoManager.updateData();

    }

    @Override
    public void onDisable(){
        UserManager.saveUserData();
    }


}
