package com.farinc.stats.main.proxies;

import com.farinc.stats.StatsMain;
import com.farinc.stats.api.Registry;
import com.farinc.stats.api.implementations.dataholder.StatDataStorage;
import com.farinc.stats.main.common.configuration.StatJSONLoader;
import com.farinc.stats.test.data.TestComponentData;
import com.farinc.stats.test.data.TestStatData;
import com.farinc.stats.test.factories.TestComponentDataFactory;
import com.farinc.stats.test.factories.TestStatDataFactory;
import com.farinc.stats.test.instances.TestComponent;
import com.farinc.stats.test.instances.TestStat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    
}