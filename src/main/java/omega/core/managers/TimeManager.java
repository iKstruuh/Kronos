package omega.core.managers;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import omega.core.Main;
import omega.core.Utils;
import omega.core.features.CombatLog;

public class TimeManager {
	
	private Main instance;
	public TimeManager(Main instance) {
		this.instance = instance;
	}

	int taskID;
	String time;
	
	private Player player;
	private List<String> message;
	
	public void startCombatTimer(Player player, List<String> message, String time) {
		this.player = player;
		this.message = message;
		this.time = time;
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		taskID = scheduler.scheduleSyncRepeatingTask(instance, new Runnable() {
			public void run() {
				if(!combatTimer()) {
					Bukkit.getScheduler().cancelTask(taskID);
					return;
				}
			}
		}, 0L, 20L);
	}
	
	protected boolean combatTimer() {
		if(CombatLog.combatCooldown.containsKey(player.getUniqueId()) && player.isOnline() && player != null) {
			if(Utils.getTimeLeft(CombatLog.combatCooldown.get(player.getUniqueId()), 
					time) <= 0) {
				CombatLog.combatCooldown.remove(player.getUniqueId());
				for(String msg : this.message) {
					Utils.translate(player, msg);
				}
				return false;
			}else {
				return true;
			}
		}else {
			return false;
		}
	}
}
