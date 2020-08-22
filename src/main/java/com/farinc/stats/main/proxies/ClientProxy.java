package com.farinc.stats.main.proxies;

import com.farinc.stats.StatsMain;
import com.farinc.stats.main.client.handlers.ClientStatHandler;
import com.farinc.stats.main.client.handlers.InventoryButtonHandler;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = StatsMain.MODID ,value = Dist.CLIENT)
public class ClientProxy {

    public static final ClientStatHandler CLIENT_STAT_HANDLER = new ClientStatHandler();  
    
    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
    	MinecraftForge.EVENT_BUS.register(new InventoryButtonHandler());
    }
}