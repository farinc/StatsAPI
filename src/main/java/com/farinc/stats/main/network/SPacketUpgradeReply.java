package com.farinc.stats.main.network;

import java.util.function.Supplier;

import com.farinc.stats.api.implementations.instances.Stat;
import com.farinc.stats.api.implementations.instances.Stat.PurchaseResult;
import com.farinc.stats.main.common.capabilities.PlayerStatCapability;
import com.farinc.stats.main.common.capabilities.PlayerStatCapability.IStatHolder;
import com.farinc.stats.main.proxies.CommonProxy;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class SPacketUpgradeReply {

    private String statID;
    private boolean isNew;
    private PurchaseResult result;

    public SPacketUpgradeReply(String statID, boolean isNew, PurchaseResult result) {
        this.statID = statID;
        this.isNew = isNew;
        this.result = result;
    }

    public static void encode(SPacketUpgradeReply packet, PacketBuffer buff){
        buff.writeString(packet.statID);
        buff.writeBoolean(packet.isNew);
        packet.result.serialize(buff);
    }
    
    public static SPacketUpgradeReply decode(PacketBuffer buff){
        String id = buff.readString();
        boolean isNew = buff.readBoolean();
        PurchaseResult re = new PurchaseResult();
        re.deserialize(buff);
        return new SPacketUpgradeReply(id, isNew, re);
    }

    public static void handle(SPacketUpgradeReply packet, Supplier<Context> ctx){
        Context context = ctx.get();

        context.enqueueWork(() -> {
            PurchaseResult result = packet.result;
            String statID = packet.statID;
            ServerPlayerEntity player = context.getSender();

            player.getCapability(PlayerStatCapability.STAT_CAPABILITY).ifPresent((IStatHolder holder) -> {
                if(result.isPassed()){
                    Stat stat;
                    if(packet.isNew){
                        stat = CommonProxy.DATA_STORAGE.getData(statID).get();
                        holder.addStat(statID, stat);
                    }else{
                        stat = holder.getStat(statID);
                    }

                    //This is one of those "certain situations"
                    stat.purchaseStat(player, result.isPurchased(), true);

                    //update gui here...
                }
            });

        });
        context.setPacketHandled(true);
    }

}