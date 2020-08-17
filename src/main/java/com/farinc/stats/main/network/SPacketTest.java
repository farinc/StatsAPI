package com.farinc.stats.main.network;

import java.util.function.Supplier;

import com.farinc.stats.main.proxies.CommonProxy;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class SPacketTest {
    
    public static void encode(SPacketTest packet, PacketBuffer buff) {

    }

    public static SPacketTest decode(PacketBuffer buff) {
        return new SPacketTest();
    }

    public static void handle(SPacketTest packet, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() -> {
            System.out.println(String.format("[Client-Side] Data Size: %d", CommonProxy.DATA_STORAGE.getDataSize()));
        });
    }
}