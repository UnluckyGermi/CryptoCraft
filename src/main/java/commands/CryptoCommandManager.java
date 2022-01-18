package commands;

import main.CryptoManager;
import main.Main;
import main.UserManager;
import models.CryptoPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import utils.Utils;

import java.util.UUID;

public class CryptoCommandManager implements CommandExecutor {

    private void showCryptoHelp(CommandSender sender){
        sender.sendMessage(" ");
        sender.sendMessage("                         §e§l-- §6§lADMIN HELP §e§l--");
        sender.sendMessage(" ");
        sender.sendMessage("- §6/crypto §eaddcoin §r§l> §6Add CryptoCoins to player.");
        sender.sendMessage("- §6/crypto §eremovecoin §r§l> §6Remove CryptoCoins from player.");
        sender.sendMessage("- §6/crypto §esetbalance §r§l> §6Set the balance for a player.");
        sender.sendMessage(" ");
        sender.sendMessage("§6Use /crypto §e<command> §6to see the usage of a certain command.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(label.equalsIgnoreCase("crypto")){
            if(sender.isOp() || sender.hasPermission("cryptocraft.admin")){
                if(args.length == 0){
                    showCryptoHelp(sender);
                    return true;
                }

                String command = args[0];

                if(command.equalsIgnoreCase("addcoin") || command.equalsIgnoreCase("removecoin")){
                    if(args.length != 4){
                        sender.sendMessage("§cUsage: /crypto " + command + " <Player> <Coin> <Quantity>");
                        return true;
                    }
                }
                else if(command.equalsIgnoreCase("setbalance")){
                    if(args.length != 3){
                        sender.sendMessage("§cUsage: /crypto setbalance <Player> <Balance>");
                        return true;
                    }
                }
                else{
                    showCryptoHelp(sender);
                    return true;
                }

                UUID targetUuid = UserManager.playerUuidByName.get(args[1]);

                CryptoPlayer target = CryptoManager.getCryptoPlayer(targetUuid);

                if(targetUuid == null || target == null){
                    sender.sendMessage("§cThe player doesn't exist.");
                    return true;
                }

                String playerName = Main.plugin.getServer().getOfflinePlayer(targetUuid).getName();

                if(command.equalsIgnoreCase("addcoin") || command.equalsIgnoreCase("removecoin")) {

                    String coin = args[2];
                    if(!CryptoManager.coinExists(coin)){
                        sender.sendMessage("§cThe coin doesn't exist.");
                        return true;
                    }

                    if(!target.getCoinList().contains(coin) && command.equalsIgnoreCase("removecoin")){
                        sender.sendMessage("§cPlayer doesn't have that coin.");
                        return true;

                    }

                    double quantity;
                    try {
                        quantity = Double.parseDouble(args[3]);
                    }
                    catch (NumberFormatException e){
                        if(args[3].equalsIgnoreCase("*") && command.equalsIgnoreCase("removecoin")){
                            quantity = target.getCoinQuantity(coin);
                        }
                        else{
                            sender.sendMessage("§cThe quantity must be numeric.");
                            return true;
                        }
                    }



                    if(command.equalsIgnoreCase("addcoin")){
                        if(quantity == 0){
                            sender.sendMessage("§cQuantity must be >0.");
                            return true;
                        }
                        target.addCoin(coin, quantity);
                        sender.sendMessage("§bAdded §a" + quantity + " " + coin + " §bto §a" + playerName + "§b.");
                    }
                    else{
                        double removed = target.removeCoin(coin, quantity);
                        sender.sendMessage("§bRemoved §a" + Utils.round5(removed) + " " + coin + " §bfrom §a" + playerName + "§b.");
                    }

                }
                else if(command.equalsIgnoreCase("setbalance")){

                    double quantity;
                    try {
                        quantity = Double.parseDouble(args[2]);
                    }
                    catch (NumberFormatException e){
                        sender.sendMessage("§cThe quantity must be numeric.");
                        return true;
                    }

                    target.setBalance(quantity);
                    sender.sendMessage("§bSet the balance of §a" + playerName + " §bto §a" + quantity + Utils.currencySymbol + "§b.");

                }

            }
            else{
                sender.sendMessage("§cYou don't have permission for this command.");
            }
        }
        return true;
    }
}
