package com.farinc.stats.main.common.handlers;

import java.util.Set;
import java.util.Map.Entry;

import com.farinc.stats.api.implementations.instances.Stat;
import com.farinc.stats.main.common.capabilities.PlayerStatCapability;
import com.farinc.stats.main.common.capabilities.PlayerStatCapability.IStatHolder;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerStatHandler {

    public final int tickRate;
    public int count = 0;

    public PlayerStatHandler(int tickRate){
        this.tickRate = tickRate;
    }

    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) 
    {
        if(this.count++ >= this.tickRate){
            PlayerEntity player = event.player;
            player.getCapability(PlayerStatCapability.STAT_CAPABILITY).ifPresent((IStatHolder holder) -> {
                this.updateStats(player,holder);
            });
        }
    }

    public void updateStats(PlayerEntity player, IStatHolder holder){
        Set<Entry<String, Stat>> stats = holder.getHeldStats();
        Stat stat;
        for(Entry<String, Stat> entry : stats){
            stat = entry.getValue();
            if(stat.isTickable()) stat.update(player);
        }
    }
}