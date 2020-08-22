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
    
    private SimpleChannel channel;
    
    public void registerChannel() {
    	channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(StatsMain.MODID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    }

    public void registerPackets(){
        register(SPacketDataStorageSync.class, SPacketDataStorageSync::encode, SPacketDataStorageSync::decode, SPacketDataStorageSync::handle);
        register(SPacketUpgradeStat.class, SPacketUpgradeStat::encode, SPacketUpgradeStat::decode, SPacketUpgradeStat::handle);
        register(SPacketUpgradeStatReply.class, SPacketUpgradeStatReply::encode, SPacketUpgradeStatReply::decode, SPacketUpgradeStatReply::handle);
    }
    
    private <P> void register(Class<P> messageType, BiConsumer<P, PacketBuffer> encoder, Function<PacketBuffer, P> decoder, BiConsumer<P, Supplier<Context>> messageConsumer){
        channel.registerMessage(id++, messageType, encoder, decoder, messageConsumer);
    }

	public SimpleChannel getChannel() {
		return channel;
	}


}