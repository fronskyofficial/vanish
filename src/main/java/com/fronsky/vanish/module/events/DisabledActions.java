package com.fronsky.vanish.module.events;

import com.fronsky.vanish.module.enums.VanishState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.block.Block;
import java.util.Collection;
import org.bukkit.GameMode;
import org.bukkit.event.block.Action;
import org.bukkit.block.EnderChest;
import org.bukkit.block.Chest;
import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import com.fronsky.vanish.module.players.VanishPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import com.fronsky.vanish.module.VanishModule;
import com.fronsky.vanish.module.players.PlayerState;
import java.util.UUID;
import java.util.HashMap;
import com.fronsky.vanish.module.data.Data;

public class DisabledActions implements Listener {
    private final Data data;
    private final boolean disabledActionsEnabled;
    private final HashMap<UUID, PlayerState> playerStateInfo;

    public DisabledActions() {
        this.data = VanishModule.getData();
        this.disabledActionsEnabled = this.data.getConfig().get().getBoolean("disabledActionsEnabled");
        this.playerStateInfo = new HashMap<>();
    }

    @EventHandler
    public void entityDamageEvent(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        final VanishPlayer vplayer = new VanishPlayer((Player)event.getEntity());
        if (this.disabledActionsEnabled && this.data.getVanishedPlayers().containsKey(vplayer.getUuid())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void foodLevelChangeEvent(final FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        final VanishPlayer vplayer = new VanishPlayer((Player)event.getEntity());
        if (this.disabledActionsEnabled && this.data.getVanishedPlayers().containsKey(vplayer.getUuid())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void entityTargetEvent(final EntityTargetEvent event) {
        final Entity entity = event.getTarget();
        if (!(entity instanceof Player)) {
            return;
        }
        final VanishPlayer vplayer = new VanishPlayer((Player)entity);
        if (this.disabledActionsEnabled && this.data.getVanishedPlayers().containsKey(vplayer.getUuid())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void playerInteractEvent(final PlayerInteractEvent event) {
        final Collection<Material> pressurePlates = Arrays.asList(Material.STONE_PRESSURE_PLATE, Material.OAK_PRESSURE_PLATE, Material.SPRUCE_PRESSURE_PLATE, Material.BIRCH_PRESSURE_PLATE, Material.JUNGLE_PRESSURE_PLATE, Material.ACACIA_PRESSURE_PLATE, Material.DARK_OAK_PRESSURE_PLATE, Material.LIGHT_WEIGHTED_PRESSURE_PLATE, Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
        final Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        if (!(block.getState() instanceof Chest) && !(block.getState() instanceof EnderChest) && !pressurePlates.contains(block.getType())) {
            return;
        }
        final VanishPlayer vplayer = new VanishPlayer(event.getPlayer());
        if (this.disabledActionsEnabled && this.data.getVanishedPlayers().containsKey(vplayer.getUuid())) {
            vplayer.setState(VanishState.HIDDEN);
            if (pressurePlates.contains(block.getType())) {
                event.setCancelled(event.getAction().equals((Object)Action.PHYSICAL));
                return;
            }
            if (this.playerStateInfo.containsKey(vplayer.getUuid())) {
                this.restorVanishState(vplayer);
                event.setCancelled(true);
                return;
            }
            if (!event.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK)) {
                return;
            }
            this.addToPlayerState(vplayer);
            vplayer.getPlayer().setGameMode(GameMode.SPECTATOR);
            if (block.getState() instanceof EnderChest) {
                vplayer.getPlayer().openInventory(vplayer.getPlayer().getEnderChest());
            }
            else {
                final Chest chest = (Chest)block.getState();
                final Inventory inventory = chest.getInventory();
                vplayer.getPlayer().openInventory(inventory);
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void inventoryCloseEvent(final InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        final VanishPlayer vplayer = new VanishPlayer((Player)event.getPlayer());
        this.restorVanishState(vplayer);
    }

    @EventHandler
    public void playerDeathEvent(final PlayerDeathEvent event) {
        final VanishPlayer vplayer = new VanishPlayer(event.getEntity());
        if (this.disabledActionsEnabled && this.data.getVanishedPlayers().containsKey(vplayer.getUuid())) {
            final String deathMessage = event.getDeathMessage();
            event.setDeathMessage((String)null);
            if (deathMessage != null) {
                vplayer.sendMessage(deathMessage);
            }
        }
    }

    private void addToPlayerState(final VanishPlayer vplayer) {
        final PlayerState playerState = new PlayerState(vplayer);
        if (!this.playerStateInfo.containsKey(vplayer.getUuid())) {
            this.playerStateInfo.put(vplayer.getUuid(), playerState);
        }
    }

    private void restorVanishState(final VanishPlayer vplayer) {
        if (!this.playerStateInfo.containsKey(vplayer.getUuid())) {
            return;
        }
        final PlayerState playerState = this.playerStateInfo.get(vplayer.getUuid());
        Bukkit.getScheduler().runTaskLater(this.data.getPlugin(), () -> {
            vplayer.getPlayer().setGameMode(playerState.getGameMode());
            vplayer.getPlayer().setAllowFlight(playerState.isAllowFlying());
            vplayer.getPlayer().setFlying(playerState.isFlying());
            vplayer.getPlayer().teleport(playerState.getLocation());
            vplayer.getPlayer().setSneaking(playerState.isSneaking());
            return;
        }, 1L);
        this.playerStateInfo.remove(vplayer.getUuid());
    }
}
