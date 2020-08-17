package com.farinc.stats.main.proxies;

import com.farinc.stats.StatsMain;
import com.farinc.stats.api.Registry;
import com.farinc.stats.api.implementations.dataholder.StatDataStorage;
import com.farinc.stats.main.common.capabilities.PlayerStatCapability;
import com.farinc.stats.main.common.configuration.StatJSONLoader;
import com.farinc.stats.main.common.handlers.PlayerStatHandler;
import com.farinc.stats.main.network.NetworkHandler;
import com.farinc.stats.test.data.TestComponentData;
import com.farinc.stats.test.data.TestStatData;
import com.farinc.stats.test.factories.TestComponentDataFactory;
import com.farinc.stats.test.factories.TestStatDataFactory;
import com.farinc.stats.test.instances.TestComponent;
import com.farinc.stats.test.instances.TestStat;
import com.farinc.stats.test.item.TestDataStorageItem;
import com.farinc.stats.test.item.TestStatItem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraft.item.Item;
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

    static {
        MinecraftForge.EVENT_BUS.register(ForgeBusHandler.class);
    }

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event){
        MinecraftForge.EVENT_BUS.register(ForgeBusHandler.class);
        MinecraftForge.EVENT_BUS.register(new PlayerStatHandler(20));
        PlayerStatCapability.register();
        NetworkHandler.registerPackets();
        registerTestContent();
    }

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new TestStatItem());
        event.getRegistry().register(new TestDataStorageItem());
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

    public static void registerTestContent(){
        Registry.registerStat(TestStat.class, TestStatData.class, new TestStatDataFactory());
        Registry.registerComponent(TestComponent.class, TestComponentData.class, new TestComponentDataFactory());
    }
    
}