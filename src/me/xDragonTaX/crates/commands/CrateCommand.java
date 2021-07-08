package me.xDragonTaX.crates.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.xDragonTaX.crates.main.Main;
import me.xDragonTaX.crates.util.Animation;

public class CrateCommand implements CommandExecutor{

	public static Boolean toggle = true;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		File ordner = new File("plugins/CrateSystem");
		if(!ordner.exists()){
			ordner.mkdir();
		}
		
		File file = new File("plugins/CrateSystem/crates.yml");
		if(!file.exists()){
			try{
				file.createNewFile();
			}catch(IOException ex){
			}
		}
			
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(!(sender instanceof Player)) {
			
			if(args.length >= 3) {
				if(args[0].equalsIgnoreCase("give")) {
					String Crate = args[1];
						
					if(Bukkit.getPlayer(args[2]) != null) {
						Player p = Bukkit.getPlayer(args[2]);
						ArrayList<String>lore = new ArrayList<>();
						
						ItemStack item =ItemStack.deserialize(cfg.getConfigurationSection(Crate.toLowerCase() + ".Item").getValues(true));
						ItemMeta meta = item.getItemMeta();
						
						meta.setDisplayName(cfg.getString(Crate.toLowerCase() + ".Name") + "Crate ");
						lore.add("");
						lore.add("§e➥ Rechtsklick §7um die Crate zu öffnen");
						lore.add("§e➥ Linksklick §7um die Crate-Vorschau zu öffnen");
						meta.setLore(lore);
						lore.clear();
						item.setItemMeta(meta);
						if(args.length >= 4) {
							try {
								 int amount = Integer.parseInt(args[3]);
									item.setAmount(amount);
							}catch(NumberFormatException ex) {
								p.sendMessage(Main.prefix + "§cBitte gib eine valide Zahl an");
								return true;
							}
						}
						
						p.getInventory().addItem(item);
						return true;
					}
				}else if(args[0].equalsIgnoreCase("giveall")) {
					String Crate = args[1];

					if(Bukkit.getPlayer(args[2]) != null) {
						Player p = Bukkit.getPlayer(args[2]);
						ArrayList<String>lore = new ArrayList<>();
						
						
						
						ItemStack item =ItemStack.deserialize(cfg.getConfigurationSection(Crate.toLowerCase() + ".Item").getValues(true));
						ItemMeta meta = item.getItemMeta();
						
						meta.setDisplayName(cfg.getString(Crate.toLowerCase() + ".Name") +"Crate ");

						lore.add("");
						lore.add("§e➥ Rechtsklick §7um die Crate zu öffnen");
						lore.add("§e➥ Linksklick §7um die Crate-Vorschau zu öffnen");
						meta.setLore(lore);
						lore.clear();
						item.setItemMeta(meta);
						int amount = 1;
						if(args.length >= 3) {
							try {
								  amount = Integer.parseInt(args[3]);
									item.setAmount(amount);
							}catch(NumberFormatException ex) {
								p.sendMessage(Main.prefix + "§cBitte gib eine valide Zahl an");
								return true;
							}
						}
						Bukkit.broadcastMessage(Main.prefix + "Alle Spieler haben §b" + amount +"x §e" + item.getItemMeta().getDisplayName() + "§7erhalten");
						for(Player all : Bukkit.getOnlinePlayers()) {
							all.getInventory().addItem(item);
						}
					}
				}else {
					sender.sendMessage("/crate give <Crate> <Spieler> [Anzahl] : Gibt dem angegebenen Spieler die Crates");
					sender.sendMessage("/crate giveall <Crate> [Anzahl] : Gibt allen Spielern die angegebene Crate");
					return true;
				}
			}

			sender.sendMessage("/crate give <Crate> <Spieler> [Anzahl] : Gibt dem angegebenen Spieler die Crates");
			sender.sendMessage("/crate giveall <Crate> [Anzahl] : Gibt allen Spielern die angegebene Crate");
			return true;
		}
		
		Player p = (Player)sender;
		
		if(!p.hasPermission(Main.perm + Main.adminperm)) {
			p.sendMessage(Main.permission);
			return true;
		}
		

		
		if(args.length >= 1) {
			if(args[0].equalsIgnoreCase("reload")) {
				Main.loadConfig();
				p.sendMessage(Main.prefix + "§aConfig neugeladen");
				return true;
			}else if(args[0].equalsIgnoreCase("list")) {
				List<String> list = cfg.getStringList("Crates");
				String message = "";
				for(int i=0;i < list.size(); i++) {
					if(message != "") {
						message = message + "§f, §e";
					}
					message = message + list.get(i);
				}
				
				
				p.sendMessage(Main.prefix + "Alle Crates: §e" + message);
				return true;
			}else if(args[0].equalsIgnoreCase("toggle")) {
				if(toggle == true) {
					toggle = false;
					Bukkit.broadcastMessage(Main.prefix + "Crates sind nun global §cdeaktiviert");
				}else {
					toggle = true;
					Bukkit.broadcastMessage(Main.prefix + "Crates sind nun wieder global §aaktiviert");
				}
				return true;
			}
		}
		ArrayList<String>lore = new ArrayList<>();
		if(args.length >= 2) {
			if(args[0].equalsIgnoreCase("create")) {
				if(cfg.getConfigurationSection(args[1]) == null) {
					List<String>crates = (List<String>) cfg.getStringList("Crates");
					crates.add(args[1].toLowerCase());
					cfg.set("Crates", crates);
					cfg.createSection(args[1].toLowerCase());
					try {
						cfg.save(file);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					p.sendMessage(Main.prefix + "§7Crate §e" + args[1] + " §7erstellt");
				}else {
					p.sendMessage(Main.prefix + "§cDiese Crate existiert bereits");
				}
			}else if(args[0].equalsIgnoreCase("delete")) {

				String Crate = args[1];
				
				if(checkIfCrateExists(Crate.toLowerCase(), p)) {
					List<String>crates = (List<String>) cfg.getStringList("Crates");
					crates.remove(args[1].toLowerCase());
					cfg.set("Crates", crates);
					cfg.set(Crate.toLowerCase(), null);
					try {
						cfg.save(file);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					p.sendMessage(Main.prefix + "§7Crate §e" + args[1] + " §7gelöscht");
				}else {
					p.sendMessage(Main.prefix + "§cDiese Crate existiert nicht");
				}
			}else if(args[0].equalsIgnoreCase("testani")) {

				String Crate = args[1];
				if(checkIfCrateExists(Crate.toLowerCase(), p)) {

					Map<String, Object> items = cfg.getConfigurationSection(Crate.toLowerCase() + ".content").getValues(true);
					
					ArrayList<ItemStack>stack = new ArrayList<>();
					for(Map.Entry<String, Object> entry : items.entrySet()) {
						String key = entry.getKey();
						if(!key.contains(".type") && !key.contains(".meta") && !key.contains(".amount") && !key.contains(".damage") && !key.contains(".broadcast")) {
							ItemStack item = ItemStack.deserialize(cfg.getConfigurationSection(Crate.toLowerCase() + ".content." + key).getValues(true));
							stack.add(item);
						}
					}
					
					Inventory inv = Bukkit.createInventory(null, 54, "§e" +Crate + " Crate§§ | §aTestanimation");
					p.openInventory(inv);
					Animation.startAnimate(p,inv, stack,null,Crate.toLowerCase());
				}else {
					p.sendMessage(Main.prefix + "§cDiese Crate existiert nicht");
				}
			}else if(args[0].equalsIgnoreCase("setname") && args.length >= 3) {

				String Crate = args[1];
				
				if(checkIfCrateExists(Crate.toLowerCase(), p)) {
					
					String message = "";
					for(int i=2; i < args.length; i++) {
						message = message + args[i] + " ";
					}
					 message = ChatColor.translateAlternateColorCodes('&', message);
					
					
					cfg.set(Crate.toLowerCase() + ".Name", "§§§r" + message);
					try {
						cfg.save(file);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					p.sendMessage(Main.prefix + "Cratename von §e" + args[1] + "§7 zu §e" + message + "§7gesetzt.");
				}else {
					p.sendMessage(Main.prefix + "§cDiese Crate existiert nicht");
				}
				
			}else if(args[0].equalsIgnoreCase("add")) {
				String Crate = args[1];
				
				if(checkIfCrateExists(Crate.toLowerCase(), p)) {
					if(p.getItemInHand() != null && !p.getItemInHand().getType().equals(Material.AIR)) {
						ItemStack pitem = p.getItemInHand();
						ItemStack item = pitem;
						
					/*	if(item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null) {
							ItemMeta meta = item.getItemMeta();
							meta.setDisplayName("§f" + Crate.toLowerCase() + " " + item.getType());
							item.setItemMeta(meta);
						}*/
						int i = 0;
						if(cfg.getConfigurationSection(Crate.toLowerCase() + ".content") != null) {
							Map<String, Object> items = cfg.getConfigurationSection(Crate.toLowerCase() + ".content").getValues(true);
							for(Map.Entry<String, Object> entry : items.entrySet()) {
								String key = entry.getKey();
								if(!key.contains(".type") && !key.contains(".meta") && !key.contains(".amount") && !key.contains(".damage") && !key.contains(".broadcast")) {
									i += 1;
								}
							}
						}
						i +=1;
						cfg.set(Crate.toLowerCase() + ".content." + i, item.serialize());
						try {
							cfg.save(file);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
						p.sendMessage(Main.prefix + "§aItem gespeichert");
					}else {
						p.sendMessage("§4Fehler!§c Du musst ein Item in der Hand halten");
					}
				}
			}else if(args[0].equalsIgnoreCase("show")) {
				showContent(args[1], p);
			}else if(args[0].equalsIgnoreCase("remove")) {
				int i = 0;
				String Crate = args[1];

				if(checkIfCrateExists(Crate.toLowerCase(), p)) {
					Map<String, Object> items = cfg.getConfigurationSection(Crate.toLowerCase() + ".content").getValues(true);
					
					Inventory inv = Bukkit.createInventory(null, 54, "§e" +Crate + " Crate§§ | §cRemove");
					
					for(Map.Entry<String, Object> entry : items.entrySet()) {
						String key = entry.getKey();
						if(!key.contains(".type") && !key.contains(".meta") && !key.contains(".amount") && !key.contains(".damage") && !key.contains(".broadcast")) {
							ItemStack item = ItemStack.deserialize(cfg.getConfigurationSection(Crate.toLowerCase() + ".content." + key).getValues(true));
							ItemMeta meta = item.getItemMeta();
							lore.clear();
							if(item.getItemMeta().getLore() != null) {
								lore = (ArrayList<String>) item.getItemMeta().getLore();
							}
							lore.add("");
							lore.add("§7ID: §e" + key);
							meta.setLore(lore);
							item.setItemMeta(meta);
							inv.setItem(i, item);
							i += 1;
						}
					}

					lore.clear();
					
					p.openInventory(inv);
				}
			}else if(args[0].equalsIgnoreCase("edit")) {
				int i = 0;
				String Crate = args[1];

				Inventory inv = Bukkit.createInventory(null, 54, "§e" +Crate + " Crate |§§ §bEdit");
				if(checkIfCrateExists(Crate.toLowerCase(), p)) {
						if(cfg.getConfigurationSection(Crate.toLowerCase() + ".content") != null) {
						Map<String, Object> items = cfg.getConfigurationSection(Crate.toLowerCase() + ".content").getValues(true);
						
						
						for(Map.Entry<String, Object> entry : items.entrySet()) {
							String key = entry.getKey();
							if(!key.contains(".type") && !key.contains(".meta") && !key.contains(".amount") && !key.contains(".damage") && !key.contains(".broadcast")) {
								ItemStack item = ItemStack.deserialize(cfg.getConfigurationSection(Crate.toLowerCase() + ".content." + key).getValues(true));
								ItemMeta meta = item.getItemMeta();
							/*	lore.clear();
								if(item.getItemMeta().getLore() != null) {
									lore = (ArrayList<String>) item.getItemMeta().getLore();
								}
							//	lore.add("");
							//	lore.add("§7ID: §e" + key);
								meta.setLore(lore);*/
								item.setItemMeta(meta);
								inv.setItem(i, item);
								i += 1;
							}
						}
	
						lore.clear();
						
					}
				}
				p.openInventory(inv);
			}else if(args[0].equalsIgnoreCase("setbc")) {
				int i = 0;
				String Crate = args[1];

				if(checkIfCrateExists(Crate.toLowerCase(), p)) {
					Map<String, Object> items = cfg.getConfigurationSection(Crate.toLowerCase() + ".content").getValues(true);
					
					Inventory inv = Bukkit.createInventory(null, 54, "§e" +Crate + " Crate§§ | §6Broadcast");
					
					for(Map.Entry<String, Object> entry : items.entrySet()) {
						String key = entry.getKey();
						if(!key.contains(".type") && !key.contains(".meta") && !key.contains(".amount") && !key.contains(".damage") && !key.contains(".broadcast")) {
							ItemStack item = ItemStack.deserialize(cfg.getConfigurationSection(Crate.toLowerCase() + ".content." + key).getValues(true));
							ItemMeta meta = item.getItemMeta();
							lore.clear();
							if(item.getItemMeta().getLore() != null) {
								lore = (ArrayList<String>) item.getItemMeta().getLore();
							}
							lore.add("");
							lore.add("§7ID: §e" + key);
							meta.setLore(lore);
							item.setItemMeta(meta);
							inv.setItem(i, item);
							i += 1;
						}
					}

					lore.clear();
					
					p.openInventory(inv);
				}
			}else if(args[0].equalsIgnoreCase("setitem")) {

				String Crate = args[1];

				if(checkIfCrateExists(Crate.toLowerCase(), p)) {
					if(p.getItemInHand() != null && !p.getItemInHand().getType().equals(Material.AIR)) {
						ItemStack pitem = p.getItemInHand();
						ItemStack item = pitem;
						if(item.getAmount() > 1) {
							item.setAmount(1);
						}
						
						cfg.set(Crate.toLowerCase() + ".Item", item.serialize());
						try {
							cfg.save(file);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
						p.sendMessage(Main.prefix + "§aDisplay Item gespeichert");
					}else {
						p.sendMessage("§4Fehler!§c Du musst ein Item in der Hand halten");
					}
				}
			}else if(args[0].equalsIgnoreCase("give")) {

				String Crate = args[1];

				if(isCrateGiveable(Crate.toLowerCase(), p)) {
					
					
					
					ItemStack item =ItemStack.deserialize(cfg.getConfigurationSection(Crate.toLowerCase() + ".Item").getValues(true));
					ItemMeta meta = item.getItemMeta();
					
					meta.setDisplayName(cfg.getString(Crate.toLowerCase() + ".Name") + "Crate ");
					lore.add("");
					lore.add("§e➥ Rechtsklick §7um die Crate zu öffnen");
					lore.add("§e➥ Linksklick §7um die Crate-Vorschau zu öffnen");
					meta.setLore(lore);
					lore.clear();
					item.setItemMeta(meta);
					if(args.length >= 3) {
						try {
							 int amount = Integer.parseInt(args[2]);
								item.setAmount(amount);
						}catch(NumberFormatException ex) {
							p.sendMessage(Main.prefix + "§cBitte gib eine valide Zahl an");
							return true;
						}
						
					}
					
					if(args.length >= 4) {
						if(Bukkit.getPlayer(args[3]) == null) {
							p.sendMessage(Main.prefix + "§cSpieler " + args[3] + " konnte nicht gefunden werden");
							return true;
						}
						
						Player target = Bukkit.getPlayer(args[3]);
						target.getInventory().addItem(item);
						p.sendMessage(Main.prefix + "Crate §e" + Crate + " §7an §e" + target.getName() + "§7 gegeben.");
						return true;
					}
					
					p.getInventory().addItem(item);
				}
				
			}else if(args[0].equalsIgnoreCase("giveall")) {
				String Crate = args[1];

				if(isCrateGiveable(Crate.toLowerCase(), p)) {
					
					
					
					ItemStack item =ItemStack.deserialize(cfg.getConfigurationSection(Crate.toLowerCase() + ".Item").getValues(true));
					ItemMeta meta = item.getItemMeta();
					
					meta.setDisplayName(cfg.getString(Crate.toLowerCase() + ".Name") +"Crate ");

					lore.add("");
					lore.add("§e➥ Rechtsklick §7um die Crate zu öffnen");
					lore.add("§e➥ Linksklick §7um die Crate-Vorschau zu öffnen");
					meta.setLore(lore);
					lore.clear();
					item.setItemMeta(meta);
					int amount = 1;
					if(args.length >= 3) {
						try {
							  amount = Integer.parseInt(args[2]);
								item.setAmount(amount);
						}catch(NumberFormatException ex) {
							p.sendMessage(Main.prefix + "§cBitte gib eine valide Zahl an");
							return true;
						}
					}
					Bukkit.broadcastMessage(Main.prefix + "Alle Spieler haben §b" + amount +"x §e" + item.getItemMeta().getDisplayName() + "§7erhalten");
					for(Player all : Bukkit.getOnlinePlayers()) {
						all.getInventory().addItem(item);
					}
				}
			}else {
				sendHelp(p);
			}
		}else {
			sendHelp(p);
		}
		
		return true;
	}
	
	private void sendHelp(Player p) {
		p.sendMessage("§e/crate reload §8: §7Lädt die config neu");
		p.sendMessage("§e/crate list §8: §7Zeigt alle Crates");
		p.sendMessage("§e/crate toggle §8: §7De/Aktiviert Crates");
		p.sendMessage("§e/crate create <Crate> §8: §7Erstellt eine neue Crate");
		p.sendMessage("§e/crate delete <Crate> §8: §7Löscht eine Crate");
		p.sendMessage("§e/crate setname <Crate> <Name> §8: §7Setzt den Anzeigenamen der Crate");
		p.sendMessage("§e/crate setitem <Crate> §8: §7Setzt das Display Item der Crate");
		p.sendMessage("§e/crate edit <Crate> §8: §7Editiert den Inhalt der Crate");
		p.sendMessage("§e/crate add <Crate> §8: §7Fügt ein Item zur Crate hinzu");
		p.sendMessage("§e/crate remove <Crate> §8: §7Lässt Items entfernen");
		p.sendMessage("§e/crate show <Crate> §8: §7Zeigt alle Items in der Crate an");
		p.sendMessage("§e/crate setbc <Crate> §8: §7Lässt Items bei einem Gewinn broadcasten");
		p.sendMessage("§e/crate give <Crate> [Anzahl] §8: §7Gibt dem Spieler Crates");
		p.sendMessage("§e/crate giveall <Crate> [Anzahl] §8: §7Gibt allen Spielern Crates");
	}
	
	
	public static boolean isCrateGiveable(String name, Player p) {
		checkIfCrateExists(name, p);
		File ordner = new File("plugins/CrateSystem");
		if(!ordner.exists()){
			ordner.mkdir();
		}
		
		File file = new File("plugins/CrateSystem/crates.yml");
		if(!file.exists()){
			try{
				file.createNewFile();
			}catch(IOException ex){
			}
		}
			
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			
		if(cfg.getString(name + ".Name") == null) {
			p.sendMessage(Main.prefix + "§cDie Crate muss einen Namen haben");
			return false;
		}
		if(cfg.getString(name + ".Item") == null) {
			p.sendMessage(Main.prefix + "§cDie Crate muss ein Display Item haben");
			return false;
		}
			
		return true;
	}
	
	public static boolean checkIfCrateExists(String name, Player p) {
		File ordner = new File("plugins/CrateSystem");
		if(!ordner.exists()){
			ordner.mkdir();
		}
		
		File file = new File("plugins/CrateSystem/crates.yml");
		if(!file.exists()){
			try{
				file.createNewFile();
			}catch(IOException ex){
			}
		}
			
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if(cfg.getConfigurationSection(name) != null) {
				return true;
			}else {
				p.sendMessage(Main.prefix + "§cDiese Crate existiert nicht");
				return false;
			}
	}
	
	public static void showContent(String Crate, Player p) {
		
		if(checkIfCrateExists(Crate.toLowerCase(), p)) {
			File ordner = new File("plugins/CrateSystem");
			if(!ordner.exists()){
				ordner.mkdir();
			}
			
			File file = new File("plugins/CrateSystem/crates.yml");
			if(!file.exists()){
				try{
					file.createNewFile();
				}catch(IOException ex){
				}
			}
				
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			int i = 0;
			
			Map<String, Object> items = cfg.getConfigurationSection(Crate.toLowerCase() + ".content").getValues(true);
			
			Inventory inv = Bukkit.createInventory(null, 54, "§e" + cfg.getString(Crate.toLowerCase() + ".Name") + "Crate§§");
			
			
			for(Map.Entry<String, Object> entry : items.entrySet()) {
				String key = entry.getKey();
				if(!key.contains(".type") && !key.contains(".meta") && !key.contains(".amount") && !key.contains(".damage") && !key.contains(".broadcast")) {
					ItemStack item = ItemStack.deserialize(cfg.getConfigurationSection(Crate.toLowerCase() + ".content." + key).getValues(true));
					
					inv.setItem(i, item);
					i += 1;
				}
			}
			p.openInventory(inv);
		}
	}
	
}
