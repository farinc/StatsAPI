package com.farinc.stats.api.implementations.factories;

import com.farinc.stats.api.implementations.data.StatData;
import com.farinc.stats.api.implementations.instances.Stat;
import com.farinc.stats.api.structure.IDataFactory;

public abstract class StatDataFactory<K extends Stat<?>, T extends StatData<K>> implements IDataFactory<K, T> {

    public abstract String getStatID();
}