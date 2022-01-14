package utils;

import main.CryptoManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MainListener implements Listener {

    @EventHandler
    public void onFirstJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        if(CryptoManager.getCryptoPlayer(player.getUniqueId()) != null) return;
        CryptoManager.addCryptoPlayer(player.getUniqueId());
    }
}
