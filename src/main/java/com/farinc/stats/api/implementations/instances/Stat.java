package com.farinc.stats.api.implementations.instances;

import com.farinc.stats.api.structure.IInstance;

public abstract class Stat implements IInstance {

    /**
     * The current level of this stat
     */
    private int level;

    /**
     * The components of this stat
     */
    private final Component[][] components;

    /**
     * The basic constructor of a stat. Data passed here should be from the
     * stats.json or some constant (at least at runtime) source and should be
     * considered "final" data.
     * 
     * @param components With the current data structure, the components are passed
     *                   by this stats corresponding {@code StatData}
     *                   implementation.
     */
    public Stat(Component[][] components) {
        this.components = components;

        //Start this stat at level zero
        this.level = 0;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxLevel(){
        return this.components.length;
    }

    public Component[][] getComponents() {
        return components;
    }

    /**
     * Give the components required to achieve the upgrade to the next level. Effectively,
     * to get from level 0 to level 1, you must complete all level 0 components before your
     * stat becomes level 2.
     * @return the components required to complete
     */
    public Component[] getUpgradeComponents(){
        return this.components[this.level];
    }

    public void setLevel(int level) {
        if(level <= this.getMaxLevel() && level >= 0){
            this.level = level;
        }
    }
}