package main;

import models.CryptoPlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import utils.ApiConsumer;
import utils.CommandManager;
import utils.MainListener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin {

    public static Main plugin;

    private File usersFile;
    private FileConfiguration usersData;

    @Override
    public void onEnable() {
        plugin = this;
        this.getServer().getPluginManager().registerEvents(new MainListener(), this);

        CommandManager cm = new CommandManager();
        getCommand("bal").setExecutor(cm);
        this.saveDefaultConfig();

        createUsersFile();

        loadUserData();


    }

    @Override
    public void onDisable(){
        saveUserData();
    }

    private void loadUserData(){
        try {
            usersData.load(usersFile);

            for(String id : usersData.getKeys(false)){
                UUID uuid = UUID.fromString(id);
                double balance = usersData.getDouble(id + ".balance");
                HashMap<String, Double> cryptos = new HashMap<>();

                ConfigurationSection coinsSection = usersData.getConfigurationSection(id + ".coins");
                if(coinsSection != null){
                    for(String coin : coinsSection.getKeys(false)) {
                        double value = usersData.getDouble(id + ".coins." + coin);
                        cryptos.put(coin, value);
                    }
                }

                CryptoManager.addCryptoPlayer(uuid, balance, cryptos);
            }
        } catch (Exception e) {
            getLogger().info("Error: Can't load user data: " + e);
            return;
        }
    }

    private void saveUserData(){
        for(CryptoPlayer cp : CryptoManager.getCryptoPlayers()){
            String name = getServer().getOfflinePlayer(cp.getUuid()).getName();
            usersData.set(cp.getUuid().toString() + ".name", name);
            usersData.set(cp.getUuid().toString() + ".balance", cp.getBalance());

            for(String s : cp.getCoinList()){
                usersData.set(cp.getUuid().toString() + ".coins." + s, cp.getCoinQuantity(s));
            }
        }

        try {
            usersData.save(usersFile);
        } catch (IOException e) {
            getLogger().info("Unexpected error: Can't save user data.");
        }
    }

    private void createUsersFile(){
        usersFile = new File(getDataFolder(), "users.yml");
        if (!usersFile.exists()) {
            usersFile.getParentFile().mkdirs();
            saveResource("users.yml", false);
        }

        usersData = new YamlConfiguration();
    }
}
