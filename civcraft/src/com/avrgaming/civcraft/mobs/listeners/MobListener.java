package com.avrgaming.civcraft.mobs.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.mobs.CommonCustomMob;
import com.avrgaming.moblib.MobLib;

public class MobListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onChunkLoad(ChunkLoadEvent event) {
		
		for (Entity e : event.getChunk().getEntities()) {
			if (!(e instanceof LivingEntity)) {
				return;
			}
			
			LivingEntity living = (LivingEntity)e;
			
			if (MobLib.isMobLibEntity(living)) {
				return;
			}
			
			switch (living.getType()) {
			case SLIME:
			case COW:
			case CHICKEN:
			case PIG:
			case SHEEP:
			case HORSE:
			case VILLAGER:
			case MUSHROOM_COW:
			case SQUID:
			case BAT:
			case OCELOT:
			case WOLF:
				return;
			default:
				break;
			}
			
			e.remove();
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityTarget(EntityTargetEvent event) {
		if (!(event.getEntity() instanceof LivingEntity)) {
			return;
		}
		
		if (!MobLib.isMobLibEntity((LivingEntity) event.getEntity())) {
			return;
		}
			
		CommonCustomMob mob = CommonCustomMob.getCCM(event.getEntity());
		if (mob != null) {
			mob.onTarget(event);
		}
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof LivingEntity)) {
			return;
		}
		
		if (!MobLib.isMobLibEntity((LivingEntity) event.getEntity())) {
			return;
		}
		
		switch (event.getCause()) {
		case SUFFOCATION:
			Location loc = event.getEntity().getLocation();
			int y = loc.getWorld().getHighestBlockAt(loc.getBlockX(), loc.getBlockZ()).getY()+4;
			loc.setY(y);
			event.getEntity().teleport(loc);
		case CONTACT:
		case FALL:
		case FIRE:
		case FIRE_TICK:
		case LAVA:
		case MELTING:
		case DROWNING:
		case FALLING_BLOCK:
		case BLOCK_EXPLOSION:
		case ENTITY_EXPLOSION:
		case LIGHTNING:
			event.setCancelled(true);
			break;
		default:
			break;
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLeash(PlayerLeashEntityEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			if (MobLib.isMobLibEntity((LivingEntity) event.getEntity())) {
				CivMessage.sendError(event.getPlayer(), "This beast cannot be tamed.");
				event.setCancelled(true);
				return;
			}
		}
	}
	
}