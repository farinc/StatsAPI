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

/**
 * This packet is sent by the client to the server to request if the client can buy
 * the stat or its upgrade in question. There is an option, a boolean "purchase" 
 * that allows to not outright buy the stat, but to test the potential for 
 * a player to purchase the stat.
 */
public class SPacketUpgradeStat {

    private String statID;
    private boolean purchase;

    /**
     * Create a new upgrade packet request.
     * 
     * @param statID The stat in question of upgrading or buying new
     * @param purchase true if to actually purchase it, false to just test the potential.
     */
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
                //if it didn't exist, then we need a new instance to evaluate the purchase
                if(stat == null){
                    stat = CommonProxy.DATA_STORAGE.getData(packet.statID).get();
                    isNew = true;
                }

                /*
                 * TODO: for now the bypass flag is false...
                 * The purchaseStat method is passed with packet.purchase. If false, it just means that the 
                 * intent is not to actually buy the stat, but to test if the player can potentially buy it.
                 */
                PurchaseResult result = stat.purchaseStat(player, packet.purchase, false);

                //send reply packet...
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new SPacketUpgradeStatReply(packet.statID, isNew, result));
            });
        });
        context.setPacketHandled(true);
    }
    
}