package me.xDragonTaX.crates.listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.xDragonTaX.crates.commands.CrateCommand;
import me.xDragonTaX.crates.main.Main;
import me.xDragonTaX.crates.util.Animation;

public class CrateListener implements Listener{

	@EventHandler
	public static void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Action action = e.getAction();
		if(e.getItem() != null && e.getItem().getItemMeta() != null && e.getItem().getItemMeta().getLore() != null && e.getItem().getItemMeta().getLore().contains("§e➥ Rechtsklick §7um die Crate zu öffnen")) {
			if(action == Action.RIGHT_CLICK_BLOCK && p.isSneaking()  && p.hasPermission(Main.perm + Main.adminperm) && p.getGameMode() == GameMode.CREATIVE) {
				if(e.getItem().getType() == Material.SKULL_ITEM) {
					e.setCancelled(true);
					File ordner = new File("plugins/CrateSystem");
					if(!ordner.exists()){
						ordner.mkdir();
					}
					
					File file = new File("plugins/CrateSystem/locations.yml");
					if(!file.exists()){
						try{
							file.createNewFile();
						}catch(IOException ex){
						}
					}

					YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
					if(isCrateValid(e.getItem(), p) != null) {
						Location loc = e.getClickedBlock().getLocation().add(0.5, +0.5, 0.5);
						cfg.set("Locations." + loc.toString(), isCrateValid(e.getItem(), p));
						p.sendMessage(Main.prefix + "Crate Position gesetzt");
						try {
							cfg.save(file);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
						String[]namelist = e.getItem().getItemMeta().getDisplayName().split(" ");
						String itemname = "";
						for(int i=0; i < namelist.length -1; i++) {
							itemname = namelist[i] + " ";
						}
						ArmorStand stand = loc.getWorld().spawn(loc, ArmorStand.class);
						stand.setCustomName(itemname + "Crate §8× §aVorschau");
						stand.setHelmet(e.getItem());
						stand.setVisible(false);
						stand.setGravity(false);
						stand.setSmall(true);
						stand.setCustomNameVisible(true);
					}
					return;
				}else {
					p.sendMessage(Main.prefix + "§cDu kannst nur Köpfe setzen");
					return;
				}
			
			}else if(action == Action.LEFT_CLICK_AIR || action ==  Action.LEFT_CLICK_BLOCK) {
					e.setCancelled(true);
					if(isCrateValid(e.getItem(), p) != null) {
						CrateCommand.showContent(isCrateValid(e.getItem(), p), p);
					}
			}else if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
				e.setCancelled(true);
				if(CrateCommand.toggle == true) {
					confirmOpen(e.getItem(), p);
				}else {
					p.sendMessage(Main.prefix + "§cCrates sind derzeitig deaktiviert");
				}
				
			}
		}else {
			if(action == Action.LEFT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.SKULL) {
				File ordner = new File("plugins/CrateSystem");
				if(!ordner.exists()){
					ordner.mkdir();
				}
				
				File file = new File("plugins/CrateSystem/locations.yml");
				if(!file.exists()){
					try{
						file.createNewFile();
					}catch(IOException ex){
					}
				}

				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				if(cfg.contains("Locations." + e.getClickedBlock().getLocation().toString())) {
					e.setCancelled(true);
				}
			}
		}
	}	
	
	@EventHandler
	public void interactEnt(PlayerInteractAtEntityEvent e) {
		
		if(e.getRightClicked().getType() == EntityType.ARMOR_STAND && e.getRightClicked().isCustomNameVisible()) {
			File ordner = new File("plugins/CrateSystem");
			if(!ordner.exists()){
				ordner.mkdir();
			}
			
			File file = new File("plugins/CrateSystem/locations.yml");
			if(!file.exists()){
				try{
					file.createNewFile();
				}catch(IOException ex){
				}
			}
	
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if(cfg.contains("Locations." + e.getRightClicked().getLocation().toString())) {
				e.setCancelled(true);
					CrateCommand.showContent(cfg.getString("Locations." + e.getRightClicked().getLocation().toString()), e.getPlayer());
			}
		}
	}
	
	@EventHandler
	public void leftClickEnt(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			Player p = (Player)e.getDamager();
			if(e.getEntity().getType() == EntityType.ARMOR_STAND && e.getEntity().isCustomNameVisible()) {
				if(p.getGameMode() == GameMode.CREATIVE && p.hasPermission(Main.perm + Main.adminperm) && p.isSneaking()) {

					File ordner = new File("plugins/CrateSystem");
					if(!ordner.exists()){
						ordner.mkdir();
					}
					
					File file = new File("plugins/CrateSystem/locations.yml");
					if(!file.exists()){
						try{
							file.createNewFile();
						}catch(IOException ex){
						}
					}

					YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

					Location loc = e.getEntity().getLocation();
					if(cfg.contains("Locations." + loc.toString())) {
						cfg.set("Locations." + loc.toString(), null);
						p.sendMessage(Main.prefix + "Crate Position gelöscht"); 
						try {
							cfg.save(file);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
						for(Entity ent :loc.getWorld().getNearbyEntities(loc, 2, 2, 2)) {
							if(ent.getType() == EntityType.ARMOR_STAND && ent.getCustomName() != null && ent.getCustomName().contains("Crate §8× §aVorschau")) {
								ent.remove();
							}
						}
					}
				}
			}
		}
	}
	
	public static void spinnnnnnn() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			public void run() {
				
				for(World world : Bukkit.getWorlds()) {
					for(Entity ent : world.getEntities()) {
						if(ent.getType() == EntityType.ARMOR_STAND && ent.getCustomName() != null && ent.getCustomName().contains("Crate §8× §aVorschau")) {
							ArmorStand stand = (ArmorStand)ent;
							stand.setHeadPose(stand.getHeadPose().add(0, 0.2, 0));
							stand.getLocation().getWorld().playEffect(stand.getLocation(), Effect.FLYING_GLYPH, 1);
						}
					}
				}
			}
		},0, 10);
	}

	
	public static void confirmOpen(ItemStack item, Player p) {
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
		
		if(cfg.get(p.getUniqueId() + " confirm") == null || cfg.get(p.getUniqueId() + " confirm") != null && !cfg.get(p.getUniqueId() + " confirm").equals("true")) {
			String[]namelist = item.getItemMeta().getDisplayName().split(" ");
			String itemname = "";
			for(int i=0; i < namelist.length -1; i++) {
				itemname = namelist[i] + " ";
			}
			Inventory inv = Bukkit.createInventory(null, 27, itemname + "Crate§§ §8× §2Bestätigung");
			ItemStack invItem = new ItemStack(Material.INK_SACK);
			ItemMeta meta = invItem.getItemMeta();
			
			invItem.setDurability((short) 10);
			meta.setDisplayName("§aBestätigen");
			invItem.setItemMeta(meta);
			inv.setItem(11, invItem);
			
			invItem.setDurability((short) 1);
			meta.setDisplayName("§cAbbrechen");
			invItem.setItemMeta(meta);
			inv.setItem(15, invItem);
			
			inv.setItem(13, item);
			
			inv.getItem(13).setAmount(1);
			
			ItemStack spacer = new ItemStack(Material.STAINED_GLASS_PANE);
			ItemMeta spacermeta = spacer.getItemMeta();
			spacer.setDurability((short) 15);
			spacermeta.setDisplayName("");
			spacer.setItemMeta(spacermeta);
			for(int i=0; i < 27; i++) {
				if(i != 11 && i != 13 && i != 15) {
					inv.setItem(i, spacer);
				}
			}
			
			p.openInventory(inv);
			
		}else {

			if(cfg.get(p.getUniqueId() + " animation") == null || cfg.get(p.getUniqueId() + " animation") != null && cfg.get(p.getUniqueId() + " animation").equals("true")) {
				openCrate(item, p, false, true);
			}else {
				openCrate(item, p, false, false);
			}
		}
		
	}
	
	
	public static void openCrate(ItemStack item, Player p, Boolean openall, Boolean animate) {
		if(isCrateValid(item, p) != null) {
			String name = isCrateValid(item, p);
			Random gen = new Random();
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
			Map<String, Object> items = cfg.getConfigurationSection(name.toLowerCase() + ".content").getValues(true);
			int numb = 0;
			for(Map.Entry<String, Object> entry : items.entrySet()) {
				String key = entry.getKey();
				if(!key.contains(".type") && !key.contains(".meta") && !key.contains(".amount") && !key.contains(".damage")  && !key.contains(".broadcast")) {
					numb += 1;
				}
			}
			numb = gen.nextInt(numb);
			String[]namelist = item.getItemMeta().getDisplayName().split(" ");
			String itemname = "";
			for(int i=0; i < namelist.length -1; i++) {
				itemname = namelist[i] + " ";
			}
			int i = 0;
			
			if(animate == true) {
					
					ArrayList<ItemStack>stack = new ArrayList<>();
					ArrayList<ItemStack>broadcast = new ArrayList<>();
					for(Map.Entry<String, Object> entry : items.entrySet()) {
						String key = entry.getKey();
						if(!key.contains(".type") && !key.contains(".meta") && !key.contains(".amount") && !key.contains(".damage") && !key.contains(".broadcast")) {
							ItemStack aniitem = ItemStack.deserialize(cfg.getConfigurationSection(name.toLowerCase() + ".content." + key).getValues(true));

							if(cfg.get(name.toLowerCase() + ".content." + key + ".broadcast") != null && cfg.get(name.toLowerCase() + ".content." + key + ".broadcast").equals("true")) {
								broadcast.add(aniitem);
							}
							stack.add(aniitem);
						}
					}
					
					Inventory inv = Animation.animationInv();
					
					if(p.getItemInHand().getAmount() > 1) {
						p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
					}else {
						p.setItemInHand(null);
					}
					Animation.startAnimate(p,inv,stack,broadcast,item.getItemMeta().getDisplayName());
					p.openInventory(inv);
					return;
			}
			
			
			
			
			for(Map.Entry<String, Object> entry : items.entrySet()) {
				String key = entry.getKey();
				if(!key.contains(".type") && !key.contains(".meta") && !key.contains(".amount") && !key.contains(".damage") && !key.contains(".broadcast")) {
					if(numb == i) {
						ItemStack win = ItemStack.deserialize(cfg.getConfigurationSection(name.toLowerCase() + ".content." + key).getValues(true));
						if(openall == false) {
							if(win.getItemMeta() != null && win.getItemMeta().getDisplayName() != null) {
							p.sendMessage(Main.prefix + "Du hast §e" + win.getItemMeta().getDisplayName() + "§7 aus einer §e" + itemname + "Crate§7 gezogen.");
							}else {
								p.sendMessage(Main.prefix + "Du hast §e" + win.getType() + "§7 aus einer §e" + itemname + "Crate§7 gezogen.");
							}
						}
						if(cfg.get(name.toLowerCase() + ".content." + key + ".broadcast") != null && cfg.get(name.toLowerCase() + ".content." + key + ".broadcast").equals("true")) {
							if(win.getItemMeta() != null && win.getItemMeta().getDisplayName() != null) {
								Bukkit.broadcastMessage(Main.prefix + "Der Spieler §e" + p.getName() + "§7 hat §e" + win.getItemMeta().getDisplayName() + "§7 aus einer §e" + itemname + "Crate§7 gezogen.");
							}else {
								Bukkit.broadcastMessage(Main.prefix + "Der Spieler §e" + p.getName() + "§7 hat §e" + win.getType() + "§7 aus einer §e" + itemname + "Crate§7 gezogen.");
							}
						}
						
						p.getInventory().addItem(win);
						if(p.getItemInHand().getAmount() > 1) {
							p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
						}else {
							p.setItemInHand(null);
						}
						return;
					}
					i += 1;
				}
			}
		}
	}
	
	public static String isCrateValid(ItemStack item, Player p) {
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
		String[]namelist = item.getItemMeta().getDisplayName().split(" ");
		String itemname = "";
		for(int i=0; i < namelist.length -1; i++) {
			itemname = namelist[i] + " ";
		}
		for(int i=0; i < cfg.getStringList("Crates").size(); i++) {
			
			if(itemname.equalsIgnoreCase(cfg.getString(cfg.getStringList("Crates").get(i) + ".Name"))) {
				String[]list = cfg.getString(cfg.getStringList("Crates").get(i)).split("'");
				return list[1];
			}
			
		}
		p.sendMessage(Main.prefix + "§cDiese Crate scheint beschädigt zu sein");
		return null;
		
	}
	
	private static HashMap<String, String>broadcast = new HashMap<>();
	
	@EventHandler
	public static void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(e.getInventory().getName().contains("Crate§§ | §cRemove")) {
			if(e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getLore() != null) {
				e.setCancelled(true);
				String[]list = e.getInventory().getName().split(" ");
				String Crate = list[0].substring(2);
				int li = 0;
				for(int i=0; i < e.getCurrentItem().getItemMeta().getLore().size(); i++) {
					li +=1;
				}
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
				String id = e.getCurrentItem().getItemMeta().getLore().get(li -1);
				id = id.replaceAll("§7ID: §e", "");
				cfg.set(Crate.toLowerCase() + ".content." + id, null);
				try {
					cfg.save(file);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				p.performCommand("crate remove " + Crate);
				p.sendMessage(Main.prefix + "Item §e" + id + " §7wurde aus der §e" + Crate +" Crate§7 entfernt");
			}
		}else if(e.getInventory().getName().contains("Crate§§ | §6Broadcast")) {
			if(e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getLore() != null) {
				e.setCancelled(true);
			}
			int li = 0;
			for(int i=0; i < e.getCurrentItem().getItemMeta().getLore().size(); i++) {
				li +=1;
			}
			String[]list = e.getInventory().getName().split(" ");
			String Crate = list[0].substring(2);
			String id = e.getCurrentItem().getItemMeta().getLore().get(li -1);
			id = id.replaceAll("§7ID: §e", "");

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
			if(cfg.get(Crate.toLowerCase() + ".content." + id + ".broadcast") != null) {
				cfg.set(Crate.toLowerCase() + ".content." + id + ".broadcast", null);
				p.sendMessage(Main.prefix + "Item §e" + id + " §7in Crate §e" + Crate +"§7 wird nun nicht mehr gebroadcastet");
			}else {
				cfg.set(Crate.toLowerCase() + ".content." + id + ".broadcast", "true");
				p.sendMessage(Main.prefix + "Item §e" + id + " §7in Crate §e" + Crate +"§7 wird nun gebroadcastet");
			}
			try {
				cfg.save(file);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}else if(e.getInventory().getName().contains("Crate§§ §8× §2Bestätigung")) {
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
			e.setCancelled(true);
			if(e.getSlot() == 11) {
				p.closeInventory();
				ItemStack crateitem = e.getInventory().getItem(13);
				if(p.getItemInHand() != null && p.getItemInHand().getType() != Material.AIR) {
					crateitem.setAmount(p.getItemInHand().getAmount());
					if(crateitem.isSimilar(p.getItemInHand())) {
						if(cfg.get(p.getUniqueId() + " animation") == null || cfg.get(p.getUniqueId() + " animation") != null && cfg.get(p.getUniqueId() + " animation").equals("false")) {
							openCrate(crateitem, p, false, true);
						}else {
							openCrate(crateitem, p, false, false);
						}
					}else {
						p.sendMessage(Main.prefix + "§cCrate konnte nicht geladen werden");
					}
				}else {
					p.sendMessage(Main.prefix + "§cCrate konnte nicht geladen werden");
				}
			}else if(e.getSlot() == 15) {
				p.closeInventory();
			}
			
		}else if(e.getInventory().getName().contains("§eCrate §8×§§ §aWird geöffnet")) {
				e.setCancelled(true);
		}
	}
	
	@EventHandler
	public static void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if(broadcast.containsKey(p.getName())) {
			e.setCancelled(true);
			String message = e.getMessage();
			message = ChatColor.translateAlternateColorCodes('&', message);
		}
	}
	
	@EventHandler
	public static void onClose(InventoryCloseEvent e) {
		if(e.getInventory().getName() != null && e.getInventory().getName().contains("Crate |§§ §bEdit")) {
			String[]list = e.getInventory().getName().split(" ");
			String Crate = list[0].substring(2);
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
			cfg.set(Crate.toLowerCase() + ".content", null);
			try {
				cfg.save(file);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			Inventory inv = e.getInventory();
			int x = 0;
			for(int i = 0; i < 54; i++) {
				ItemStack item = inv.getItem(i);
				if(item != null && item.getType() != Material.AIR) {
					cfg.set(Crate.toLowerCase() + ".content." + x, item.serialize());
					x += 1;
				}
			}
			try {
				cfg.save(file);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			e.getPlayer().sendMessage(Main.prefix + "§aCrate §e" + Crate + "§a gespeichert.");
			
		}
	}
	
}
