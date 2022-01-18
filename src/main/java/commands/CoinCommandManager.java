package commands;

import main.CryptoManager;
import models.CryptoPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import utils.Utils;

import java.util.List;

public class CoinCommandManager implements CommandExecutor {

    private void showList(CommandSender sender, int page){

        List<String> coinList = CryptoManager.getCoinList();

        if(coinList.size() == 0){
            sender.sendMessage("§cThere aren't coins available at the moment :(");
            return;
        }

        String[][] div = Utils.splitArray(coinList, 6);

        if(page > div.length || page < 1){
            sender.sendMessage("§cPage not available.");
            return;
        }


        sender.sendMessage(" ");
        sender.sendMessage("§e§nLIST OF AVAILABLE COINS§r§e (§6Page " + page + "§e/§6" + div.length + "§e)");
        sender.sendMessage(" ");

        for(String s : div[page - 1]) {
            if(s == null) break;
            sender.sendMessage("- §b" + s + " (§a" + Utils.round(CryptoManager.getCoinValue(s)) + Utils.currencySymbol + "§b)");
        }

        sender.sendMessage(" ");
    }

    private void showHelp(CommandSender sender){
        sender.sendMessage(" ");
        sender.sendMessage("                         §e§l-- §6§lCOIN HELP §e§l--");
        sender.sendMessage(" ");
        sender.sendMessage("- §6/coin §ebuy §r§l> §6Buy cryptocoins.");
        sender.sendMessage("- §6/coin §esell §r§l> §6Sell cryptocoins.");
        sender.sendMessage("- §6/coin §elist [Page] §r§l> §6Show the list of available coins.");
        sender.sendMessage(" ");
        sender.sendMessage("§6Use /crypto §e<command> §6to see the usage of a certain command.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(label.equalsIgnoreCase("coin")){


            if(args.length == 0){
                showHelp(sender);
                return true;
            }

            switch (args[0]){
                case "list":
                    int page = 1;
                    if(args.length > 1){
                        try{
                            page = Integer.parseInt(args[1]);
                        }
                        catch (NumberFormatException e){
                            sender.sendMessage("§cThe page must be a numeric value.");
                            return true;
                        }
                    }

                    showList(sender, page);
                    break;

                case "buy":
                case "sell":

                    if(!(sender instanceof Player)){
                        sender.sendMessage("This command can only be executed by a player.");
                        return true;
                    }

                    if(args.length != 3){
                        if(args[0].equalsIgnoreCase("buy")){
                            sender.sendMessage("§cUsage: /coin buy <Coin> <Quantity (" + Utils.currencySymbol + ")>");
                        }
                        else{
                            sender.sendMessage("§cUsage: /coin sell <Coin> <Quantity (Coin) | * (all) >");
                        }
                        return true;
                    }

                    Player player = (Player) sender;
                    CryptoPlayer cryptoPlayer = CryptoManager.getCryptoPlayer(player.getUniqueId());

                    if(cryptoPlayer == null){
                        sender.sendMessage("§cUnexpected error, try re-logging.");
                        return true;
                    }

                    String coin = args[1];


                    if(!CryptoManager.coinExists(coin)){
                        player.sendMessage("§cThat coin is not available. Use §6/coin list§c to see all available coins.");
                        return true;
                    }

                    if(!cryptoPlayer.getCoinList().contains(coin) && args[0].equalsIgnoreCase("sell")){
                        player.sendMessage("§cYou don't have that coin.");
                        return true;
                    }

                    double coinValue = CryptoManager.getCoinValue(coin);

                    double quantity;
                    try{
                        quantity = Double.parseDouble(args[2]);
                    }
                    catch (NumberFormatException ex){

                        if(args[2].equals("*") && args[0].equals("sell")) quantity = cryptoPlayer.getCoinQuantity(coin);
                        else {
                            player.sendMessage("§cQuantity must be a numeric value (or '*' for selling all).");
                            return true;
                        }
                    }

                    if(args[0].equals("sell")){
                        if(quantity > cryptoPlayer.getCoinQuantity(coin)){
                            player.sendMessage("§cYou don't have enough " + coin + " to sell. Use §6/cbal §cto see your coins.");
                            return true;
                        }


                        cryptoPlayer.removeCoin(coin, quantity);
                        cryptoPlayer.setBalance(cryptoPlayer.getBalance() + coinValue * quantity);

                        player.sendMessage("§bYou've sold §a" + Utils.round5(quantity) + " " + coin + " §bat §a" + Utils.round(coinValue * quantity) + Utils.currencySymbol);
                    }
                    else{
                        if(quantity > cryptoPlayer.getBalance()) {
                            player.sendMessage("§cYou don't have enough money. Use §6/bal §cto see your current balance.");
                            return true;
                        }

                        double coinQuantity = quantity / coinValue;

                        if(coinQuantity < 0.001){

                            double min = 0.001 * coinValue;

                            player.sendMessage("§cThat's a low value. Min: §6" + min + Utils.currencySymbol);
                            return true;
                        }




                        cryptoPlayer.addCoin(coin, coinQuantity);
                        cryptoPlayer.setBalance(cryptoPlayer.getBalance() - quantity);

                        player.sendMessage("§bYou've bought §a" + Utils.round5(coinQuantity) + " " + coin + " §bat §a" + Utils.round(quantity) + Utils.currencySymbol);

                    }
                    break;
                default:
                    showHelp(sender);


            }

        }
        return true;
    }
}
