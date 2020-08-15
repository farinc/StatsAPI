package com.farinc.stats.api;

import java.util.HashMap;
import java.util.Set;

import com.farinc.stats.api.implementations.data.ComponentData;
import com.farinc.stats.api.implementations.data.StatData;
import com.farinc.stats.api.implementations.factories.ComponentDataFactory;
import com.farinc.stats.api.implementations.factories.StatDataFactory;
import com.farinc.stats.api.implementations.instances.Component;
import com.farinc.stats.api.implementations.instances.Stat;

public final class Registry {
    
    private static HashMap<String, StatDataFactory<? extends Stat, ? extends StatData<? extends Stat>>> statRegistry = new HashMap<String, StatDataFactory<? extends Stat, ? extends StatData<? extends Stat>>>();
    private static HashMap<String, ComponentDataFactory<? extends Component, ? extends ComponentData<? extends Component>>> componentRegistry = new HashMap<String, ComponentDataFactory<? extends Component, ? extends ComponentData<? extends Component>>>();

    public static <I extends Stat,D extends StatData<I>> void registerStat(Class<I> stat, Class<D> statData, StatDataFactory<I, D> factory){
        String id = factory.getStatID();
        if(!statRegistry.containsKey(id)){
            statRegistry.put(id, factory);
        }
    }

    public static <I extends Component,D extends ComponentData<I>> void registerComponent(Class<I> component, Class<D> componentData, ComponentDataFactory<I, D> factory){
        String id = factory.getComponentID();
        if(!componentRegistry.containsKey(id)){
            componentRegistry.put(id, factory);
        }
    }

    public static boolean statExists(String id){
        return statRegistry.containsKey(id);
    }

    public static boolean componentExists(String id){
        return componentRegistry.containsKey(id);
    }

    public static StatDataFactory<? extends Stat, ? extends StatData<? extends Stat>> getStatDataFactory(String id){
        return statRegistry.get(id);
    }

    public static ComponentDataFactory<? extends Component, ? extends ComponentData<? extends Component>> getComponentDataFactory(String id){
        return componentRegistry.get(id);
    }

    public static Set<String> getStatsRegistered(){
        return statRegistry.keySet();
    }
}