package com.farinc.stats.api.structure;

import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Stores IData instances to be used in runtime. The two main requirements is that 1) the data
 * is serialized as needed and 2) the data is available to users by a unique identifier.
 * 
 * @param <T> The IData type to be stored
 * @param <I> The unique immutable identifier type
 * @param <S> The serialization type for Minecraft
 */
public interface IDataStorage<T extends IData<? extends IInstance>, I, S extends INBT> extends INBTSerializable<S>{
    
    public T getData(I key);

    public void setData(I key, T data);
}