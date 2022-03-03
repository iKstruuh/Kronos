package omega.core;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Utils {
	
	public static int getTimeLeft(Long playerCooldown, String cooldown) {
		long serverTime = System.currentTimeMillis();
		long featureCooldown = (Long.valueOf(cooldown).longValue())*1000L;
		if(playerCooldown != 0L && (playerCooldown + featureCooldown > serverTime)) {
			long cooldownMillis = serverTime - playerCooldown;
			int seconds = (int)(cooldownMillis/1000L);
			return seconds;
		}
		return 0;
	}
	
	public static void translate(CommandSender sender, String msg) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}
}
