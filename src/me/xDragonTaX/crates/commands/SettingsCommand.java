package me.xDragonTaX.crates.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.xDragonTaX.crates.main.Main;

public class SettingsCommand implements CommandExecutor, Listener{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(Main.onlyplayerscanusethis);
			return true;
		}
		
		openGui((Player)sender);
		
		return true;
	}

	
	public static void openGui(Player p) {
		ArrayList<String>lore = new ArrayList<>();
		File ordner = new File("plugins/CrateSystem");
		if(!ordner.exists()){
			ordner.mkdir();
		}
		
		File file = new File("plugins/CrateSystem/playerdata.yml");
		if(!file.exists()){
			try{
				file.createNewFile();
			}catch(IOException ex){
			}
		}
		
		
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		Inventory inv = Bukkit.createInventory(null, 27, "§2Einstellungen§§");
		ItemStack item = new ItemStack(Material.ITEM_FRAME);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName("§aCrate Bestätigung");
		lore.add("");
		if(cfg.get(p.getUniqueId() + " confirm") == null || cfg.get(p.getUniqueId() + " confirm") != null && !cfg.get(p.getUniqueId() + " confirm").equals("false")) {
			lore.add("§7Bestätigung: §2An");
		}else {
			lore.add("§7Bestätigung: §cAus");
		}
		meta.setLore(lore);
		lore.clear();
		
		item.setItemMeta(meta);
		inv.setItem(11, item);
		
		item.setType(Material.CHEST);
		//meta.setDisplayName("§cComing Soon");
		//meta.setLore(null);
		
		meta.setDisplayName("§eCrate Animation");
		lore.add("");
		if(cfg.get(p.getUniqueId() + " animation") == null || cfg.get(p.getUniqueId() + " animation") != null && !cfg.get(p.getUniqueId() + " animation").equals("false")) {
			lore.add("§7Animation: §2An");
		}else {
			lore.add("§7Animation: §cAus");
		}
		meta.setLore(lore);
		lore.clear();
		item.setItemMeta(meta);
		inv.setItem(15, item);
		
		ItemStack spacer = new ItemStack(Material.STAINED_GLASS_PANE);
		ItemMeta spacermeta = spacer.getItemMeta();
		spacer.setDurability((short) 15);
		spacermeta.setDisplayName("");
		spacer.setItemMeta(spacermeta);
		for(int i=0; i < 27; i++) {
			if(i != 11 && i != 15) {
				inv.setItem(i, spacer);
			}
		}
		
		p.openInventory(inv);
		
	}
	
	@EventHandler
	private static void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(e.getInventory().getName() != null && e.getInventory().getName().equals("§2Einstellungen§§")) {
			e.setCancelled(true);
			File ordner = new File("plugins/CrateSystem");
			if(!ordner.exists()){
				ordner.mkdir();
			}
			
			File file = new File("plugins/CrateSystem/playerdata.yml");
			if(!file.exists()){
				try{
					file.createNewFile();
				}catch(IOException ex){
				}
			}
			
			
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if(e.getSlot() == 11) {
				if(cfg.get(p.getUniqueId() + " confirm") == null || cfg.get(p.getUniqueId() + " confirm") != null && !cfg.get(p.getUniqueId() + " confirm").equals("false")) {
					cfg.set(p.getUniqueId() + " confirm", "false");
					try {
						cfg.save(file);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					p.sendMessage(Main.prefix + "Crate Bestätigung §aaktiviert");
				}else {
					cfg.set(p.getUniqueId() + " confirm", "true");
					try {
						cfg.save(file);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					p.sendMessage(Main.prefix + "Crate Bestätigung §aaktiviert");
				}
				openGui(p);
			}else if(e.getSlot() == 15) {

				if(cfg.get(p.getUniqueId() + " animation") == null || cfg.get(p.getUniqueId() + " animation") != null && !cfg.get(p.getUniqueId() + " animation").equals("false")) {
					cfg.set(p.getUniqueId() + " animation", "true");
					try {
						cfg.save(file);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					p.sendMessage(Main.prefix + "Crate Animation §aaktiviert");
				}else {
					cfg.set(p.getUniqueId() + " animation", "false");
					try {
						cfg.save(file);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					p.sendMessage(Main.prefix + "Crate Animation §cdeaktiviert");
				}
				openGui(p);
			}
		}
	}
	
}
