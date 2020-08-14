package com.farinc.stats.api.structure;

import java.util.function.Supplier;

public interface IDataFactory<K extends IInstance, T extends IData<K>> extends Supplier<T> {}