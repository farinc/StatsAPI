package com.farinc.stats.api.implementations.instances;

import com.farinc.stats.api.structure.IInstance;

import net.minecraft.entity.player.PlayerEntity;

public abstract class Component implements IInstance {
    
    /**
     * Determines if this player is capable of fulfilling this component. 
     * 
     * @param player the player in question
     * @param enforce true if the intend is to enforce the fulfillment or false to just check
     * @return true if the player can purchase or false otherwise
     */
    public abstract boolean enforcePurchase(PlayerEntity player, boolean enforce);
}