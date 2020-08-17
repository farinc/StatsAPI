package com.farinc.stats.main.network;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.farinc.stats.StatsMain;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {

    //Default stuff
    private static final String PROTOCOL_VERSION = "1";
    
    private static int id = 0;
    
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(StatsMain.MODID, "main"), () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    private static <P> void register(Class<P> messageType, BiConsumer<P, PacketBuffer> encoder, Function<PacketBuffer, P> decoder, BiConsumer<P, Supplier<Context>> messageConsumer){
        INSTANCE.registerMessage(id++, messageType, encoder, decoder, messageConsumer);
    }

    public static void registerPackets(){
        register(SPacketDataStorageSync.class, SPacketDataStorageSync::encode, SPacketDataStorageSync::decode, SPacketDataStorageSync::handle);
    }


}