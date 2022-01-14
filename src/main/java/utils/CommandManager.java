package utils;

import main.CryptoManager;
import main.Main;
import models.CryptoPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(label.equalsIgnoreCase("bal")){
            if(!(sender instanceof Player)) {
                sender.sendMessage("This command is only for players.");
                return false;
            }

            Player player = (Player) sender;
            CryptoPlayer cryptoPlayer = CryptoManager.getCryptoPlayer(player.getUniqueId());

            if(cryptoPlayer == null) {
                sender.sendMessage("§cUnexpected error. Try re-logging.");
                return false;
            }

            player.sendMessage("§bBalance: " + "§a" + cryptoPlayer.getBalance() +
                    Main.plugin.getConfig().get("currency-symbol"));

        }

        return true;
    }
}
