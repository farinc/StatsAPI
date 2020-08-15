package com.farinc.stats;

import com.farinc.stats.api.implementations.dataholder.StatDataStorage;

import net.minecraftforge.fml.common.Mod;

@Mod(value = StatsMain.MODID)
public class StatsMain {
    
    public static final String MODID = "statsapi";

    public static final StatDataStorage DATA_STORAGE = new StatDataStorage();

}