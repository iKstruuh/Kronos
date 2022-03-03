package omega.core.features;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import omega.core.Main;
import omega.core.Utils;
import omega.core.managers.TimeManager;

public class CombatLog implements Listener{

	private Main instance;
	public CombatLog(Main instance) {
		this.instance = instance;
	}

	public static Map<UUID, Long> combatCooldown = new HashMap<>();
	
	@EventHandler
	public void combatLog(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player victim = (Player)e.getEntity();
			if(victim.getHealth() - e.getFinalDamage() <= 0.0D) {
				if(CombatLog.combatCooldown.containsKey(victim.getUniqueId())) {
					CombatLog.combatCooldown.remove(victim.getUniqueId());
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void combatLog(EntityDamageByEntityEvent e) {
		FileConfiguration kronosConfig = instance.getKronosConfig();
		if(e.getEntity() instanceof Player
				&& e.getDamager() instanceof Player) {
			Player victim = (Player)e.getEntity();
			Player damager = (Player)e.getDamager();
			if(victim.getHealth() - e.getFinalDamage() <= 0.0D) {
				CombatLog.combatCooldown.remove(damager.getUniqueId());
				CombatLog.combatCooldown.remove(victim.getUniqueId());
				return;
			}
			List<Player> players = new ArrayList<>();
			players.add(victim);
			players.add(damager);
			for(Player player : players) {
				if(!CombatLog.combatCooldown.containsKey(player.getUniqueId())) {
					for(String msg : kronosConfig.getStringList("CombatLog.timer-message")) {
						Utils.translate(player, msg);
					}
					CombatLog.combatCooldown.put(player.getUniqueId(), System.currentTimeMillis());
					TimeManager timer = new TimeManager(instance);
					timer.startCombatTimer(player, kronosConfig.getStringList("CombatLog.timer-message2"), 
							kronosConfig.getString("CombatLog.timer-seconds"));
				}else {
					CombatLog.combatCooldown.put(player.getUniqueId(), System.currentTimeMillis());
				}
			}
		}
	}
	
	@EventHandler
	public void combatLog(PlayerQuitEvent e) {
		Player p = (Player)e.getPlayer();
		if(!CombatLog.combatCooldown.containsKey(p.getUniqueId())) return;
		p.setHealth(0);
		CombatLog.combatCooldown.remove(p.getUniqueId());
		return;
	}
}
