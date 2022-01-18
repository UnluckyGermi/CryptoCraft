package tabcompleters;

import main.CryptoManager;
import models.CryptoPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(label.equalsIgnoreCase("cpay") && args.length == 2){
            Player player = (Player) sender;
            CryptoPlayer cryptoPlayer = CryptoManager.getCryptoPlayer(player.getUniqueId());

            return cryptoPlayer.getCoinList();

        }
        return null;
    }
}
