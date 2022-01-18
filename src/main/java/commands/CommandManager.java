package commands;

import main.CryptoManager;
import main.Main;
import main.UserManager;
import models.CryptoPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import utils.Utils;

import java.util.UUID;

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

        if(label.equalsIgnoreCase("pay")){
            if(args.length != 2){
                player.sendMessage("§cUsage: /pay <Player> <Quantity>");
                return true;
            }

            UUID target = UserManager.playerUuidByName.get(args[0]);
            if(target == null) {
                player.sendMessage("§cThat player doesn't exist.");
                return true;
            }

            if(target.equals(player.getUniqueId())){
                player.sendMessage("§cYou can't send money to yourself.");
                return true;
            }

            CryptoPlayer cryptoPlayerTarget = CryptoManager.getCryptoPlayer(target);
            if(cryptoPlayerTarget == null) {
                player.sendMessage("§cUnexpected Error. Try re-logging.");
                return true;
            }

            double quantity;
            try {
                quantity = Double.parseDouble(args[1]);
            }
            catch (NumberFormatException ex){
                player.sendMessage("§cQuantity must be a numeric value.");
                return true;
            }

            if(quantity < 0.01){
                player.sendMessage("§cQuantity must be more than 0.01" + Utils.currencySymbol);
                return true;
            }

            if(quantity > cryptoPlayer.getBalance()){
                player.sendMessage("§cYou don't have enough balance");
                return true;
            }

            cryptoPlayer.setBalance(cryptoPlayer.getBalance() - quantity);
            cryptoPlayerTarget.setBalance(cryptoPlayerTarget.getBalance() + quantity);

            player.sendMessage("§bYou sent §a" + quantity + Utils.currencySymbol + " §bto §a" + args[0]);

            OfflinePlayer targetPlayer = Main.plugin.getServer().getPlayer(target);

            if(targetPlayer.isOnline()){
                Player onlineTarget = (Player) targetPlayer;
                onlineTarget.sendMessage("§bYou received §a" + quantity + Utils.currencySymbol + " §bfrom §a" + player.getName());
            }

        }

        if(label.equalsIgnoreCase("cpay")){
            if(args.length != 3){
                player.sendMessage("§cUsage: /cpay <Player> <Coin> <Quantity>");
                return true;
            }

            UUID target = UserManager.playerUuidByName.get(args[0]);
            if(target == null) {
                player.sendMessage("§cThat player doesn't exist.");
                return true;
            }

            if(target.equals(player.getUniqueId())){
                player.sendMessage("§cYou can't send money to yourself.");
                return true;
            }

            CryptoPlayer cryptoPlayerTarget = CryptoManager.getCryptoPlayer(target);
            if(cryptoPlayerTarget == null) {
                player.sendMessage("§cUnexpected Error. Try re-logging.");
                return true;
            }

            String coin = args[1];

            if(!cryptoPlayer.getCoinList().contains(coin)){
                player.sendMessage("§cYou don't have that coin.");
                return true;
            }

            double quantity;
            try {
                quantity = Double.parseDouble(args[2]);
            }
            catch (NumberFormatException ex){
                player.sendMessage("§cQuantity must be a numeric value.");
                return true;
            }

            if(quantity < 0.01){
                player.sendMessage("§cQuantity must be more than 0.01" + Utils.currencySymbol);
                return true;
            }

            double coinQuantity = quantity / CryptoManager.getCoinValue(coin);

            if(cryptoPlayer.getCoinQuantity(coin) < coinQuantity){
                player.sendMessage("§cYou don't have enough " + coin);
                return true;
            }

            cryptoPlayer.removeCoin(coin, coinQuantity);
            cryptoPlayerTarget.addCoin(coin, coinQuantity);

            player.sendMessage("§bYou sent §a" + Utils.round5(coinQuantity) + " " + coin + " §b(§a"+ quantity + Utils.currencySymbol + "§b) to §a" + args[0]);

            OfflinePlayer targetPlayer = Main.plugin.getServer().getPlayer(target);

            if(targetPlayer.isOnline()){
                Player onlineTarget = (Player) targetPlayer;
                onlineTarget.sendMessage("§bYou received §a" + Utils.round5(coinQuantity) + " " + coin + " §b(§a"+ quantity + Utils.currencySymbol + "§b) from §a" + player.getName());
            }



        }

        return true;
    }
}
