package tabcompleters;

import main.CryptoManager;
import main.UserManager;
import models.CryptoPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CryptoCommandCompleter implements TabCompleter {

    private final String[] subCommands = new String[]{"addcoin", "removecoin", "setbalance"};

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(label.equalsIgnoreCase("crypto")) {

            if (args.length == 1) return Arrays.stream(subCommands).toList();

            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("addcoin")) {
                    return Utils.filterCoin(args[2]);
                } else if (args[0].equalsIgnoreCase("removecoin")) {
                    UUID uuid = UserManager.playerUuidByName.get(args[1]);
                    if (uuid == null) return null;


                    CryptoPlayer cryptoPlayer = CryptoManager.getCryptoPlayer(uuid);
                    return cryptoPlayer.getCoinList();
                }
            }

        }



        return null;
    }
}
