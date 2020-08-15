package com.farinc.stats.main.proxies;

import com.farinc.stats.StatsMain;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = StatsMain.MODID ,value = Dist.CLIENT)
public class ClientProxy {
    
}