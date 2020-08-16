package com.farinc.stats.main.proxies;

import com.farinc.stats.StatsMain;
import com.farinc.stats.api.implementations.dataholder.StatDataStorage;
import com.farinc.stats.main.common.capabilities.PlayerStatCapability;
import com.farinc.stats.main.common.configuration.StatJSONLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = StatsMain.MODID)
public class CommonProxy {

    public static final Logger LOGGER = LogManager.getLogger();
    
    public static final StatDataStorage DATA_STORAGE = new StatDataStorage();

    public static final StatJSONLoader JSON_LOADER = new StatJSONLoader(); 

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event){}

    static {
        MinecraftForge.EVENT_BUS.register(ForgeBusHandler.class);
    }

    public static class ForgeBusHandler {

        @SubscribeEvent
        public static void attachCap(final AttachCapabilitiesEvent<Entity> event) {
            if(event.getObject() instanceof PlayerEntity ) {
                PlayerStatCapability cap = new PlayerStatCapability();
                event.addCapability(new ResourceLocation(StatsMain.MODID, "stat"), cap);
            }
        }
    }
}