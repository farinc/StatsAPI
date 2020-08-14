package com.farinc.stats.api.implementations.data;

import com.farinc.stats.api.implementations.instances.Component;
import com.farinc.stats.api.implementations.instances.Stat;
import com.farinc.stats.api.structure.IData;

public abstract class StatData<I extends Stat<?>> implements IData<I> {

    private ComponentData<?>[][] componentData;

    public ComponentData<?>[][] getComponentData() {
        return componentData;
    }

    public void setComponentData(ComponentData<?>[][] componentData) {
        this.componentData = componentData;
    }

    /**
     * Helper function to obtain the components themselves from their data originals.
     * This is very "{@code Brutus}-ish" (to kill "{@code Caesar}-level" performance) so later on some cache of the 
     * instances might be used.
     */
    protected final Component<?>[][] getComponentInstances(){
        int levels = this.componentData.length;
        Component<?>[][] components = new Component[levels][];

        int sizePerLevel;

        for(int level = 0; level < levels; level++){

            sizePerLevel = this.componentData[level].length;
            components[level] = new Component[sizePerLevel];

            for(int i = 0; i < sizePerLevel; i++){
                components[level][i] = this.componentData[level][i].get();
            }
        }

        return components;
    }
}