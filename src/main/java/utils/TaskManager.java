package utils;

import main.CryptoManager;
import main.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskManager {

    public static BukkitRunnable apiConsumeTask = new BukkitRunnable() {
        @Override
        public void run() {
            CryptoManager.updateData();
            Main.plugin.getLogger().info("CryptoCurrencies data updated!");
        }
    };
}
