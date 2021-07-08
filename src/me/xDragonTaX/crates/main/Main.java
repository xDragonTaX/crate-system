package me.xDragonTaX.crates.main;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.xDragonTaX.crates.commands.CrateCommand;
import me.xDragonTaX.crates.commands.OpenallCommand;
import me.xDragonTaX.crates.commands.SettingsCommand;
import me.xDragonTaX.crates.listener.CrateListener;

public class Main extends JavaPlugin{
	
	public static Main instance;
	public static String permission = "";
	public static String prefix = "";
	public static String perm = "";
	public static String adminperm = "";
	public static String onlyplayerscanusethis = "";
	
	
	@Override
	public void onDisable() {
		System.out.println("[CrateSystem]   Plugin deaktiviert.");
		Bukkit.getServer().clearRecipes();
	}
	
	@Override
	public void onEnable() {
		instance = this;
		loadConfig();
		CrateListener.spinnnnnnn();
		System.out.println("[CrateSystem]   Plugin aktiviert.");
		

		this.getCommand("crate").setExecutor(new CrateCommand());
		this.getCommand("openall").setExecutor(new OpenallCommand());
		this.getCommand("settings").setExecutor(new SettingsCommand());
		

		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new CrateListener(), this);
		pm.registerEvents(new SettingsCommand(), this);
	}
	
	
	public static void loadConfig() {
			File ordner = new File("plugins/CrateSystem");
			if(!ordner.exists()){
				ordner.mkdir();
			}
			
			File file = new File("plugins/CrateSystem/config.yml");
			if(!file.exists()){
				try{
					file.createNewFile();
				}catch(IOException ex){
				}
			}
				
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				
				
				
			if(cfg.getString("prefix") != null) {
				prefix = cfg.getString("prefix");
				prefix = ChatColor.translateAlternateColorCodes('&', prefix);
			}else {
				prefix = "&8» &a&lCrates &8┃&7 ";
				cfg.set("prefix", prefix);
			}
			if(cfg.getString("Permission Prefix") != null) {
				perm = cfg.getString("Permission Prefix");
			}else {
				cfg.set("Permission Prefix", "crates.");
				perm = "crates.";
			}
			if(cfg.getString("Admin Permission") != null) {
				adminperm = cfg.getString("Admin Permission");
			}else {
				cfg.set("Admin Permission", "admin");
				adminperm = "admin";
			}
			
			
			
			if(cfg.getString("No Permission Message") != null) {
				permission = cfg.getString("No Permission Message");
				permission = ChatColor.translateAlternateColorCodes('&', permission);
			}else {
				permission = "&a&lCrates &8┃ &cDazu hast du keine Rechte!";
				cfg.set("No Permission Message", permission);
			}
			if(cfg.getString("Only Players Can use this") != null) {
				onlyplayerscanusethis = cfg.getString("Only Players Can use this");
			}else {
				onlyplayerscanusethis = "Dieser Befehl kann nur von Spielern ausgeführt werden";
				cfg.set("Only Players Can use this", onlyplayerscanusethis);
			}
			
			try {
				cfg.save(file);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
	}
	
	
	
	public static Main getInstance() {
		return instance;
	}
	
}
