package io.github.leothawne.thedoctorreborn.event;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.leothawne.thedoctorreborn.ConsoleLoader;
import io.github.leothawne.thedoctorreborn.TheDoctorRebornLoader;

public class RegenerationEvent implements Listener {
	private static TheDoctorRebornLoader plugin;
	private static Connection connection;
	private static ConsoleLoader myLogger;
	public RegenerationEvent(TheDoctorRebornLoader plugin, Connection connection, ConsoleLoader myLogger) {
		RegenerationEvent.plugin = plugin;
		RegenerationEvent.connection = connection;
		RegenerationEvent.myLogger = myLogger;
	}
	public static final void regenerate(Player timelord, boolean specialRegeneration) {
		Player player = (Player) timelord;
		if(player.hasPermission("TheDoctorReborn.regenerate")) {
			try {
		    	Statement stmtGettimelord = connection.createStatement();
		    	ResultSet rsGettimelord = stmtGettimelord.executeQuery("SELECT * FROM timelords WHERE mid = '" + player.getUniqueId() + "';");
		    	if(rsGettimelord.getInt("regeneration") < 12 || specialRegeneration == true) {
			   		Statement stmtGetPreferences = connection.createStatement();
			   		ResultSet rsGetPreferences = stmtGetPreferences.executeQuery("SELECT * FROM preferences WHERE id = '" + rsGettimelord.getInt("id") + "';");
			   		if(rsGetPreferences.getInt("enabled") == 1) {
			   			if(rsGetPreferences.getInt("locked") == 0) {
			   				if(player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
			   					player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "You already are regenerating!");
			   				} else {
					    		if(player.getFoodLevel() == 0) {
					    			player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "You were too hungry! Starting regeneration proccess...");
					    		} else {
					    			player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Starting regeneration proccess...");
					    		}
					    		if(specialRegeneration == false) {
						    		player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Regeneration number: " + (rsGettimelord.getInt("regeneration") + 1));
					    		} else {
					    			player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "You earned more 12 regenerations!");
					    		}
					    		player.setInvulnerable(true);
					    		int regeneration;
					    		if(specialRegeneration == false) {
					    			regeneration = rsGettimelord.getInt("regeneration") + 1;
					    		} else {
					    			regeneration = 0;
					    		}
					    		int id = rsGettimelord.getInt("id");
					    		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 15, 1));
					    		player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 30, 1));
					    		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 75, 2));
					    		player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 75, 1));
					    		player.sendTitle(ChatColor.YELLOW + "You are", ChatColor.YELLOW + "" + ChatColor.ITALIC + "regenerating...", 20 * 1, 20 * 10, 20 * 1);
					    		int regenerationTask1 = 0;
					    		regenerationTask1 = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
									public void run() {
										if(player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
											plugin.getServer().getWorld(player.getWorld().getName()).playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 100);
										}
					    			}
					    		}, 20, 0);
					    		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					    			public void run() {
					    				try {
					    					Statement stmtUpdateRegeneration = connection.createStatement();
							   				stmtUpdateRegeneration.executeUpdate("UPDATE timelords SET regeneration = '" + regeneration + "' WHERE id = '" + id + "';");
							   				stmtUpdateRegeneration.close();
							   				player.setHealth(20);
						    				player.setFoodLevel(20);
								    		player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 20, 10));
								    		player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * 60, 14));
								    		player.setInvulnerable(false);
								    		player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Regeneration proccess completed!");
					    				} catch(SQLException ne) {
					    					player.setInvulnerable(false);
					    					myLogger.severe("Error while update a Time Lord's regeneration number!");
					    					myLogger.severe(ne.getMessage());
					    		    		player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.RED + "You can't regenerate if we can't update your regeneration number!");
					    				}
					    			}
					    		}, 20 * 15);
					    		while(plugin.getServer().getScheduler().isCurrentlyRunning(regenerationTask1)) {
					    			if(player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
					    			} else {
					    				plugin.getServer().getScheduler().cancelTask(regenerationTask1);
					    			}
					    		}
			   				}
			   			} else {
			   				player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "You manually locked your regeneration ability. Regeneration disabled.");
			   			}
			   		} else {
			   			player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Your regeneration ability was disabled by an admin. Regeneration disabled.");
			   		}
			   		rsGetPreferences.close();
			   		stmtGetPreferences.close();
		    	} else {
		    		player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "You already regenerated 12 times. Regeneration disabled.");
		    	}
		    	rsGettimelord.close();
		    	stmtGettimelord.close();
	    	} catch(SQLException event) {
	    		myLogger.severe("Error while regenerating a Time Lord!");
	    		myLogger.severe(event.getMessage());
	    	}
		} else {
			player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "You can't do that! You don't have permission.");
			myLogger.severe(player.getName() + " don't have permission [TheDoctorReborn.regenerate].");
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public static final void onDamageEvent(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			try {
				Statement stmtRegeneration = connection.createStatement();
				ResultSet rsRegeneration = stmtRegeneration.executeQuery("SELECT * FROM timelords WHERE mid = '" + player.getUniqueId() + "';");
				Statement stmtPreferences = connection.createStatement();
				ResultSet rsPreferences = stmtPreferences.executeQuery("SELECT * FROM preferences WHERE id = '" + rsRegeneration.getInt("id") + "';");
				if(rsPreferences.getInt("enabled") == 1) {
					if(rsPreferences.getInt("locked") == 0) {
						if(rsRegeneration.getInt("regeneration") != 12) {
							if((player.getHealth() - e.getFinalDamage()) <= 0) {
								if(player.hasPermission("TheDoctorReborn.regenerate") && e.getCause() != DamageCause.VOID && e.getCause() != DamageCause.SUICIDE) {
									e.setCancelled(true);
									regenerate(player, false);
								}
							}
						}
					}
				}
			} catch(SQLException event) {
				myLogger.severe("Error while checking a regeneration cycle!");
				myLogger.severe(event.getMessage());
			}
		}
	}
	@EventHandler
	public static final void onDeath(PlayerDeathEvent e) {
		if(e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			player.setInvulnerable(false);
			if(player.hasPermission("TheDoctorReborn.regenerate")) {
				try {
					Statement stmtRegeneration = connection.createStatement();
					ResultSet rsRegeneration = stmtRegeneration.executeQuery("SELECT * FROM timelords WHERE mid = '" + player.getUniqueId() + "';");
					Statement stmtPreferences = connection.createStatement();
					ResultSet rsPreferences = stmtPreferences.executeQuery("SELECT * FROM preferences WHERE id = '" + rsRegeneration.getInt("id") + "';");
					if(rsRegeneration.getInt("regeneration") == 12) {
						plugin.getServer().broadcastMessage(ChatColor.AQUA + "[TDR] " + ChatColor.GREEN + "" + player.getName() + " " + ChatColor.YELLOW + "used all of 12 regenerations.");
						player.sendMessage(ChatColor.AQUA + "[TDR] " + ChatColor.YELLOW + "Restarting your regeneration cycle...");
						Statement stmtRestartRegeneration = connection.createStatement();
						stmtRestartRegeneration.executeUpdate("UPDATE timelords SET regeneration = '0' WHERE id = '" + rsRegeneration.getInt("id") + "';");
						stmtRestartRegeneration.close();
					} else {
						if(rsPreferences.getInt("enabled") == 0) {
							plugin.getServer().broadcastMessage(ChatColor.AQUA + "[TDR] " + ChatColor.GREEN + "" + player.getName() + " " + ChatColor.YELLOW + "couldn't regenerate.");
						} else if(rsPreferences.getInt("locked") == 1){
							plugin.getServer().broadcastMessage(ChatColor.AQUA + "[TDR] " + ChatColor.GREEN + "" + player.getName() + " " + ChatColor.YELLOW + "refused to regenerate.");
						}
					}
					rsRegeneration.close();
					stmtRegeneration.close();
		    	} catch(SQLException event) {
		    		myLogger.severe("Error while restarting a regeneration cycle!");
		    		myLogger.severe(event.getMessage());
				}
			}
		}
	}
}
