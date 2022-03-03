package omega.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import omega.core.features.ChatControl;
import omega.core.features.CombatLog;

public class Main extends JavaPlugin{
	
	private FileConfiguration kronosConfig = null;
	private File kronosFile = null;

	public void onEnable() {
		this.registerCommands();
		this.registerEvents();
		this.registerKronosConfig();
		this.patchConfig();
		Utils.translate(Bukkit.getConsoleSender(), "&7(&e&lOMEGA&b&lCRAFT&7) &3&lKronos &aactivado.");
	}
	
	public void onDisable() {
		
	}
	
	private void registerCommands() {
		this.getCommand("kronos").setExecutor(new Commands());
	}
	
	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new ChatControl(this), this);
		pm.registerEvents(new CombatLog(this), this);
	}
	
	private void patchConfig() {
		FileConfiguration kronosConfig = getKronosConfig();
		if(kronosConfig.getString("Config-Version").equals("1.0")) {
			
		}
	}
	
	//KRONOS CONFIG FILE
	
	public void registerKronosConfig(){
		kronosFile = new File(this.getDataFolder(), "kronos.yml");
		if(!kronosFile.exists()){
			this.getKronosConfig().options().copyDefaults(true);
			saveKronosConfig();
		}
	}
	public void saveKronosConfig() {
		try {
			kronosConfig.save(kronosFile);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
   
	public FileConfiguration getKronosConfig() {
		if(kronosConfig == null) {
			reloadKronosConfig();
		}
		return kronosConfig;
	}
   
	public void reloadKronosConfig() {
		if(kronosConfig == null) {
			kronosFile = new File(getDataFolder(), "kronos.yml");
		}
		kronosConfig = YamlConfiguration.loadConfiguration(kronosFile);

		Reader defConfigStream;
		try {
			defConfigStream = new InputStreamReader(this.getResource("kronos.yml"), "UTF8");
			if(defConfigStream != null) {
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				kronosConfig.setDefaults(defConfig);
			}
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}      
	}
}
