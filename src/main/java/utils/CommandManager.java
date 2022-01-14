package utils;

import main.CryptoManager;
import main.Main;
import main.UserManager;
import models.CryptoPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandManager implements CommandExecutor {

    private static String currencySymbol = Main.plugin.getConfig().getString("currency-symbol");

    private double round(double x){
        return (Math.round(x * 100.0) / 100.0);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(label.equalsIgnoreCase("crypto")){
            if(sender.isOp() || sender.hasPermission("cryptocraft.admin")){
                if(args.length == 0){
                    // Show Help
                    return true;
                }
                if(args[0].equalsIgnoreCase("addcoin") || args[0].equalsIgnoreCase("removecoin")){
                    if(args.length < 4){
                        // Show help
                        return true;
                    }
                    UUID targetUuid = UserManager.playerUuidByName.get(args[1]);
                    if(targetUuid == null){
                        sender.sendMessage("§cThe player doesn't exist.");
                        return true;
                    }

                    String coin = args[2];
                    if(!CryptoManager.coinExists(coin)){
                        sender.sendMessage("§cThe coin doesn't exist.");
                        return true;
                    }
                    double quantity;
                    try {
                        quantity = Double.parseDouble(args[3]);
                    }
                    catch (NumberFormatException e){
                        sender.sendMessage("§cThe quantity must be numeric.");
                        return true;
                    }

                    String name = Main.plugin.getServer().getOfflinePlayer(targetUuid).getName();

                    if(args[0].equalsIgnoreCase("addcoin")){
                        CryptoManager.getCryptoPlayer(targetUuid).addCoin(coin, quantity);
                        sender.sendMessage("§bAdded §a" + quantity + " " + coin + " §bto §a" + name);
                    }
                    else{
                        double removed = CryptoManager.getCryptoPlayer(targetUuid).removeCoin(coin, quantity);
                        sender.sendMessage("§bRemoved §a" + removed + " " + coin + " §bfrom §a" + name);
                    }



                }
            }
        }

        if(label.equalsIgnoreCase("cbal")){
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

            if(cryptoPlayer.getCoinList().isEmpty()) {
                player.sendMessage("§cYou don't have any crypto coin :'(");
                return true;
            }

            player.sendMessage(" ");
            player.sendMessage("§e§nCRYPTO COINS");
            player.sendMessage(" ");
            for(String s : cryptoPlayer.getCoinList()){
                player.sendMessage("- §b" + s + ": §a" + round(cryptoPlayer.getCoinQuantity(s)) + " §b(§a"
                    + round(cryptoPlayer.getCoinTotalValue(s)) + currencySymbol + "§b)");
            }

            player.sendMessage(" ");
            player.sendMessage("§b§lTotal Coin Value: §a§l" + round(cryptoPlayer.getAllCoinsTotalValue()) + currencySymbol);
            player.sendMessage(" ");

        }

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
                    currencySymbol);

        }

        return true;
    }
}
