package com.farinc.stats.main.proxies;

import com.farinc.stats.StatsMain;
import com.farinc.stats.main.client.handlers.ClientStatHandler;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = StatsMain.MODID ,value = Dist.CLIENT)
public class ClientProxy {

    public static final ClientStatHandler CLIENT_STAT_HANDLER = new ClientStatHandler();    
}