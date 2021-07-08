package me.xDragonTaX.crates.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.xDragonTaX.crates.main.Main;

public class TestAnimationCommand implements CommandExecutor, Listener{
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(Main.onlyplayerscanusethis);
			return true;
		}
		
		
		return true;
	}
	
	
/*	@EventHandler
	private static void onClick(InventoryClickEvent e) {
		if(e.getInventory().getName().equals("Animation Test")) {
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
		}
	}
	@SuppressWarnings("deprecation")
	public static void startAnimate(Player p, HashMap<String, String> items) {
		int spins = 0;
		Random gen = new Random();
		while(spins < 30) {
			Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.getInstance(), new Runnable() {
				public void run() {
					if(p.getOpenInventory() != null && p.getOpenInventory().getTitle().equals("Animation Test")) {
						Inventory inv = (Inventory) p.getOpenInventory();
						int item = gen.nextInt(items.size());
						inv.setItem(10, items.get(item));
					}
				}
				
			}, 2);
		}
		
		
	}
	
	
	public static void animate(Player p, HashMap<String, String> items) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			public void run() {
				
				for(int i=0; i < items.size(); i++) {
					for(Entry<String, String> entry : items.entrySet()) {
						
						
					}
				}
				
				
			}
		},0, 10);*/
	
}
