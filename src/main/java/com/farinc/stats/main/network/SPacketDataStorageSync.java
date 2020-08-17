package com.farinc.stats.main.network;

import java.util.function.Supplier;

import com.farinc.stats.main.proxies.CommonProxy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class SPacketDataStorageSync {

    private CompoundNBT tag;

    public SPacketDataStorageSync(CompoundNBT tag) {
        this.tag = tag;
    }

    public static void encode(SPacketDataStorageSync packet, PacketBuffer buff) {
        buff.writeCompoundTag(packet.tag);
    }

    public static SPacketDataStorageSync decode(PacketBuffer buff) {
        CompoundNBT tag = buff.readCompoundTag();
        SPacketDataStorageSync packet = new SPacketDataStorageSync(tag);
        return packet;
    }

    public static void handle(SPacketDataStorageSync packet, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() -> {
            CommonProxy.DATA_STORAGE.deserializeNBT(packet.tag);
        });
    }
}