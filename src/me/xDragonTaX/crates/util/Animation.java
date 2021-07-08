package me.xDragonTaX.crates.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.xDragonTaX.crates.main.Main;

public class Animation {
	
	public static HashMap<Player, Integer>spinning = new HashMap<>();
	public static HashMap<Player, Integer>win = new HashMap<>();
	private static Random gen = new Random();
	private static int spin = gen.nextInt(10000);
	
	public static void startAnimate(Player p, Inventory inv, ArrayList<ItemStack> items, ArrayList<ItemStack> broadcastable, String crate) {
	//	p.openInventory(inv);
		spinning.put(p, 0);
				spin = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
					public void run() {
							int itemInt = gen.nextInt(items.size());
							spinning.put(p, spinning.get(p) + 1);
							if(spinning.get(p) < 40) {
								inv.setItem(13, items.get(itemInt));
								if(!p.getInventory().equals(inv)) {
									p.openInventory(inv);
								}
								p.updateInventory();
								if(spinning.get(p) == 39) {
									win.put(p, itemInt);
								}
								p.playSound(p.getLocation(), Sound.PISTON_RETRACT, 1f, 1f);
							}else if(spinning.get(p) == 40 ) {
								p.playSound(p.getLocation(), Sound.LEVEL_UP, 1f, 1f);
							}else if(spinning.get(p) == 41) {
								itemInt = win.get(p);
								p.getInventory().addItem(items.get(itemInt));
								p.updateInventory();
								p.closeInventory();
								Bukkit.getScheduler().cancelTask(spin);
								spinning.remove(p);
								if(!broadcastable.contains(items.get(itemInt))) {
									p.sendMessage(Main.prefix + "Du hast §e" + (items.get(itemInt).getItemMeta() != null && items.get(itemInt).getItemMeta().getDisplayName() != null ? items.get(itemInt).getItemMeta().getDisplayName() : items.get(itemInt).getType().toString()) + "§7 aus einer " + crate +" erhalten");
								}else {
									Bukkit.broadcastMessage(Main.prefix + "Der Spieler §e" + p.getName() + "§7 hat §e" + (items.get(itemInt).getItemMeta() != null && items.get(itemInt).getItemMeta().getDisplayName() != null ? items.get(itemInt).getItemMeta().getDisplayName() : items.get(itemInt).getType().toString()) + "§7 aus einer §e" + crate + "§7 gezogen.");
								}
								win.remove(p);
							}
						
					}
				},0, 5);
	}
	
	
	public static Inventory animationInv() {
		Inventory inv = Bukkit.createInventory(null, 9*3, "§eCrate §8×§§ §aWird geöffnet");
		
		ItemStack spacer = new ItemStack(Material.STAINED_GLASS_PANE);
		ItemMeta meta = spacer.getItemMeta();
		
		meta.setDisplayName("");
		spacer.setDurability((short) 10);
		spacer.setItemMeta(meta);
		
		int[] slots = {0,1,7,8,9,17,18,19,25,26};
		for(int i=0; i < slots.length; i++) {
			inv.setItem(slots[i], spacer );
		}
		return inv;
		
	}
	
	
}
