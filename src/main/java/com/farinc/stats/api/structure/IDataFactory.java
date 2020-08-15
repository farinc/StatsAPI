package com.farinc.stats.api.structure;

import java.util.function.Supplier;

/**
 * The only purpose of the factory is to be coupled with a registry to act as a lookup for 
 * creations of {@code IData}. This implies that this factory must know the corresponding 
 * {@code IData} and {@code IInstance} types of creation.
 *  
 * @param <I> The IInstance type
 * @param <D> The IData type
 */
public interface IDataFactory<I extends IInstance, D extends IData<I>> extends Supplier<D> {}