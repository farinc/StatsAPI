package com.farinc.stats.main.network;

import java.util.function.Supplier;

import com.farinc.stats.api.implementations.instances.Stat;
import com.farinc.stats.api.implementations.instances.Stat.PurchaseResult;
import com.farinc.stats.main.common.capabilities.PlayerStatCapability;
import com.farinc.stats.main.common.capabilities.PlayerStatCapability.IStatHolder;
import com.farinc.stats.main.proxies.CommonProxy;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class SPacketUpgradeStat {

    private String statID;
    private boolean purchase;

    public SPacketUpgradeStat(String statID, boolean purchase) {
        this.statID = statID;
        this.purchase = purchase;
    }

    public static void encode(SPacketUpgradeStat packet, PacketBuffer buff){
        buff.writeString(packet.statID);
        buff.writeBoolean(packet.purchase);
    }

    public static SPacketUpgradeStat decode(PacketBuffer buff){
        return new SPacketUpgradeStat(buff.readString(), buff.readBoolean());
    }

    public static void handle(SPacketUpgradeStat packet, Supplier<Context> ctx){
        Context context = ctx.get();

        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            player.getCapability(PlayerStatCapability.STAT_CAPABILITY).ifPresent((IStatHolder holder) -> {
                Stat stat = holder.getStat(packet.statID);
                boolean isNew = false;
                //if it didn't exist, then it is trying to add a new stat entirely
                if(stat == null){
                    stat = CommonProxy.DATA_STORAGE.getData(packet.statID).get();
                    isNew = true;
                }

                //TODO: for now the bypass flag is false...
                PurchaseResult result = stat.purchaseStat(player, packet.purchase, false);

                //send reply packet...
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new SPacketUpgradeReply(packet.statID, isNew, result));
            });
        });
        context.setPacketHandled(true);
    }
    
}