package com.farinc.stats.api.structure;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.INBT;

/**
 * Represents a unique representation of the initial data from a corresponding {@code IData}.
 * The structure here is that data passed to the instance from the {@code IData} is considered
 * unchanging, making this instance represent the original data. However, this grants this
 * instance complete independence from the original data, as alone this instance can have its
 * own data accordingly. In Minecraft, this instance should persist so some serialization is
 * needed.
 * 
 * @param <T> The serialization type.
 */
public interface IInstance {
    
    public INBT writeNBT();

    public void readNBT(INBT nbt);

    public boolean isTickable();

    public void update(PlayerEntity player);
}