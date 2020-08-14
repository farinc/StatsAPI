package com.farinc.stats.api.implementations.factories;

import com.farinc.stats.api.implementations.data.ComponentData;
import com.farinc.stats.api.implementations.instances.Component;
import com.farinc.stats.api.structure.IDataFactory;

public abstract class ComponentDataFactory<K extends Component, T extends ComponentData<K>>
        implements IDataFactory<K, T> {

    public abstract String getComponentID();
}