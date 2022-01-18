package tabcompleters;

import main.CryptoManager;
import models.CryptoPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import utils.Utils;

import java.util.Arrays;
import java.util.List;

public class CoinCommandCompleter implements TabCompleter {

    private final String[] subCommands = new String[]{"buy", "sell", "list"};

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(label.equalsIgnoreCase("coin")){
            if(args.length == 1) return Arrays.stream(subCommands).toList();
            if(args.length == 2 && args[0].equalsIgnoreCase("buy")) return Utils.filterCoin(args[1]);
            if(args.length == 2 && args[0].equalsIgnoreCase("sell")){
                Player player = (Player) sender;
                CryptoPlayer cryptoPlayer = CryptoManager.getCryptoPlayer(player.getUniqueId());
                if(cryptoPlayer == null) return null;

                return cryptoPlayer.getCoinList();
            }
        }
        return null;
    }
}
