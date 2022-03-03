package omega.core.features;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import omega.core.Main;
import omega.core.Utils;

public class ChatControl implements Listener{
	
	private Main instance;
	public ChatControl(Main instance) {
		this.instance = instance;
	}

	public static Map<UUID, Long> chatCooldown = new HashMap<>();
	
	//CHAT DELAY
	
	@EventHandler(priority = EventPriority.LOW)
	public void chatDelay(AsyncPlayerChatEvent e) {
		FileConfiguration kronosConfig = instance.getKronosConfig();
		Player p = (Player)e.getPlayer();
		if(p.hasPermission(kronosConfig.getString("ChatControl.cooldown-seconds"))) return;
		if(!ChatControl.chatCooldown.containsKey(p.getUniqueId())
				|| Utils.getTimeLeft(ChatControl.chatCooldown.get(p.getUniqueId()), 
						kronosConfig.getString("ChatControl.cooldown-seconds")) <= 0) {
			ChatControl.chatCooldown.put(p.getUniqueId(), System.currentTimeMillis());
			return;
		}
		e.setCancelled(true);
		for(String msg : kronosConfig.getStringList("ChatControl.cooldown-message")) {
			Utils.translate(p, msg);
		}
	}
	
	//SPAM & IP BLOCKER
	
	@EventHandler
	public void spamBlocker(AsyncPlayerChatEvent e) {
		
	}
}
