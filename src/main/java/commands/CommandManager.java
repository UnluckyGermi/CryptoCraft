package commands;

import main.CryptoManager;
import models.CryptoPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import utils.Utils;

public class CommandManager implements CommandExecutor {







    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


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

        if(label.equalsIgnoreCase("cbal")){

            if(cryptoPlayer.getCoinList().isEmpty()) {
                player.sendMessage("§cYou don't have any crypto coin :'(");
                return true;
            }

            player.sendMessage(" ");
            player.sendMessage("§e§nCRYPTO COINS");
            player.sendMessage(" ");
            for(String s : cryptoPlayer.getCoinList()){
                player.sendMessage("- §b" + s + ": §a" + Utils.round5(cryptoPlayer.getCoinQuantity(s)) + " §b(§a"
                    + Utils.round(cryptoPlayer.getCoinTotalValue(s)) + Utils.currencySymbol + "§b)");
            }

            player.sendMessage(" ");
            player.sendMessage("§b§lTotal Coin Value: §a§l" + Utils.round(cryptoPlayer.getAllCoinsTotalValue()) + Utils.currencySymbol);
            player.sendMessage(" ");

        }

        if(label.equalsIgnoreCase("bal")){
            player.sendMessage("§bBalance: " + "§a" + Utils.round(cryptoPlayer.getBalance()) + Utils.currencySymbol);

        }

        return true;
    }
}
