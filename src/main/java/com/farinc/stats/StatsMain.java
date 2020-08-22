package com.farinc.stats;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(value = StatsMain.MODID)
public class StatsMain {
    
    public static final String MODID = "statsapi";

    public static final Logger LOGGER = LogManager.getLogger();
}