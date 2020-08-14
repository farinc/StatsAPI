package com.farinc.stats.api;

import java.util.HashMap;
import com.farinc.stats.api.implementations.data.*;
import com.farinc.stats.api.implementations.factories.*;
import com.farinc.stats.api.implementations.instances.*;

public final class Registry {
    
    private static HashMap<String, StatDataFactory<? extends Stat<?>, ? extends StatData<?>>> statRegistry = new HashMap<String, StatDataFactory<? extends Stat<?>, ? extends StatData<?>>>();
    private static HashMap<String, ComponentDataFactory<? extends Component<?>, ? extends ComponentData<?>>> componentRegistry = new HashMap<String, ComponentDataFactory<? extends Component<?>, ? extends ComponentData<?>>>();

    public static <I extends Stat<?>,D extends StatData<I>> void registerStat(Class<I> stat, Class<D> statData, StatDataFactory<I, D> factory){
        String id = factory.getStatID();
        if(!statRegistry.containsKey(id)){
            statRegistry.put(id, factory);
        }
    }

    public static <I extends Component<?>,D extends ComponentData<I>> void registerComponent(Class<I> component, Class<D> componentData, ComponentDataFactory<I, D> factory){
        String id = factory.getComponentID();
        if(!componentRegistry.containsKey(id)){
            componentRegistry.put(id, factory);
        }
    }
}