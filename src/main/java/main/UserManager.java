package main;

import models.CryptoPlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class UserManager {

    private static File usersFile;
    private static FileConfiguration usersData;

    public static HashMap<String, UUID> playerUuidByName = new HashMap<>();

    public static void loadUserData(){
        try {
            usersData.load(usersFile);

            for(String id : usersData.getKeys(false)){
                UUID uuid = UUID.fromString(id);
                double balance = usersData.getDouble(id + ".balance");
                HashMap<String, Double> cryptos = new HashMap<>();
                String name = usersData.getString(id + ".name");
                playerUuidByName.put(name, uuid);

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
            Main.plugin.getLogger().info("Error: Can't load user data: " + e);
            return;
        }
    }

    public static void saveUserData(){
        usersData = new YamlConfiguration();
        for(CryptoPlayer cp : CryptoManager.getCryptoPlayers()){
            String name = Main.plugin.getServer().getOfflinePlayer(cp.getUuid()).getName();
            usersData.set(cp.getUuid().toString() + ".name", name);
            usersData.set(cp.getUuid().toString() + ".balance", cp.getBalance());

            for(String s : cp.getCoinList()){
                usersData.set(cp.getUuid().toString() + ".coins." + s, cp.getCoinQuantity(s));
            }
        }

        try {
            usersData.save(usersFile);
        } catch (IOException e) {
            Main.plugin.getLogger().info("Unexpected error: Can't save user data.");
        }
    }

    public static void createUsersFile(){
        usersFile = new File(Main.plugin.getDataFolder(), "users.yml");
        if (!usersFile.exists()) {
            usersFile.getParentFile().mkdirs();
            Main.plugin.saveResource("users.yml", false);
        }

        usersData = new YamlConfiguration();
    }
}
