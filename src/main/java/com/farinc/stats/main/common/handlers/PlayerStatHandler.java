package com.farinc.stats.main.common.handlers;

import java.util.Set;
import java.util.Map.Entry;

import com.farinc.stats.api.implementations.instances.Component;
import com.farinc.stats.api.implementations.instances.Stat;
import com.farinc.stats.main.common.capabilities.PlayerStatCapability;
import com.farinc.stats.main.common.capabilities.PlayerStatCapability.IStatHolder;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerStatHandler {

    public final int tickRate; //passed in by config in the future...
    public int count = 0;

    public PlayerStatHandler(int tickRate){
        this.tickRate = tickRate;
    }

    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) 
    {
        if(event.phase == Phase.START) {
            if(this.count++ >= this.tickRate){
                PlayerEntity player = event.player;
                player.getCapability(PlayerStatCapability.STAT_CAPABILITY).ifPresent((IStatHolder holder) -> {
                    this.updateStats(player,holder);
                });
            }
        }
    }

    public void updateStats(PlayerEntity player, IStatHolder holder){
        Set<Entry<String, Stat>> stats = holder.getHeldStats();
        Stat stat;
        for(Entry<String, Stat> entry : stats){
            stat = entry.getValue();
            
            //Stat update
            if(stat.isTickable()) stat.update(player);
            
            //Component updates
            Component[] components = stat.getUpgradeComponents();
            for(Component component : components){
                if(component.isTickable()) component.update(player);
            }
        }
    }
}