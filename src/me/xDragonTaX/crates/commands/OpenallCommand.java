package me.xDragonTaX.crates.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.xDragonTaX.crates.listener.CrateListener;
import me.xDragonTaX.crates.main.Main;

public class OpenallCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(Main.onlyplayerscanusethis);
			return true;
		}
		
		Player p = (Player)sender;
		
		if(p.getItemInHand() != null && p.getItemInHand().getItemMeta() != null && p.getItemInHand().getItemMeta().getLore() != null && p.getItemInHand().getItemMeta().getLore().contains("§e➥ Rechtsklick §7um die Crate zu öffnen")) {
			int slot = p.getInventory().getHeldItemSlot();
			int amount = p.getInventory().getItem(slot).getAmount();
			if(checkPerms(amount, p) == true) {
				for(int i=0; i < amount; i++) {
					CrateListener.openCrate(p.getItemInHand(), p, true, false);
				}
			}else {
				p.sendMessage(Main.prefix + "§cDu hast keine Rechte soviele Crates zu öffnen (" + amount + ")");
				return true;
			}
			p.sendMessage(Main.prefix + "§e" + amount + " Crates §7geöffnet");
		}
		
		
		return true;
	}

	
	private boolean checkPerms(int amount, Player p) {
		if(p.hasPermission(Main.perm + "openall.64")) {
			return true;
		}else if(amount <= 48 && p.hasPermission(Main.perm + "openall.48")) {
			return true;
		}else if(amount <= 32 && p.hasPermission(Main.perm + "openall.32")) {
			return true;
		}else if(amount <= 16 && p.hasPermission(Main.perm + "openall.16")) {
			return true;
		}else if(amount <= 8 && p.hasPermission(Main.perm + "openall.8")) {
			return true;
		}else {
			return false;
		}
	}
	
}
