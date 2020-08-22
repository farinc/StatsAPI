package com.farinc.stats.main.network;

import java.util.function.Supplier;

import com.farinc.stats.api.implementations.instances.Stat;
import com.farinc.stats.api.implementations.instances.Stat.PurchaseResult;
import com.farinc.stats.api.implementations.instances.Stat.PurchaseResult.Reason;
import com.farinc.stats.main.common.capabilities.PlayerStatCapability;
import com.farinc.stats.main.common.capabilities.PlayerStatCapability.IStatHolder;
import com.farinc.stats.main.proxies.ClientProxy;
import com.farinc.stats.main.proxies.CommonProxy;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

/**
 * This is a reply packet from its sister packet (SPacketUpgradeStat) that basically replies to
 * the client with the results of the "upgrade" request.
 */
public class SPacketUpgradeStatReply {

    private String statID;
    private boolean isNew;
    private PurchaseResult result;

    /**
     * Create a new reply to client of the results of the "upgrade"
     * 
     * @param statID the stat in question
     * @param isNew whether or not the stat is "new" aka not on the player capability and pre-purchase, the stat is level 0
     * @param result the purchase results.
     */
    public SPacketUpgradeStatReply(String statID, boolean isNew, PurchaseResult result) {
        this.statID = statID;
        this.isNew = isNew;
        this.result = result;
    }

    public static void encode(SPacketUpgradeStatReply packet, PacketBuffer buff){
        buff.writeString(packet.statID);
        buff.writeBoolean(packet.isNew);
        packet.result.serialize(buff);
    }
    
    public static SPacketUpgradeStatReply decode(PacketBuffer buff){
        String id = buff.readString();
        boolean isNew = buff.readBoolean();
        PurchaseResult re = new PurchaseResult();
        re.deserialize(buff);
        return new SPacketUpgradeStatReply(id, isNew, re);
    }

    public static void handle(SPacketUpgradeStatReply packet, Supplier<Context> ctx){
        Context context = ctx.get();

        context.enqueueWork(() -> {
            PurchaseResult result = packet.result;
            String statID = packet.statID;
            ServerPlayerEntity player = context.getSender();

            player.getCapability(PlayerStatCapability.STAT_CAPABILITY).ifPresent((IStatHolder holder) -> {
                Reason reason = result.getReason();

                /*
                 * If the reason was that the stat was paid or is free, then add the stat or upgrade the stat on client side.
                 * Recall that on client side we just need to store a reference of what the player owns for gui lookup, so 
                 * that we are not tailing behind the client if we constantly are sending packets (and quite unnecessary)
                 */
                if(reason == Reason.PAID || reason == Reason.FREEPURCHASE) {
                    Stat stat;
                    if(packet.isNew){
                        stat = CommonProxy.DATA_STORAGE.getData(statID).get();
                        holder.addStat(statID, stat);
                    }else{
                        stat = holder.getStat(statID);
                    }

                    //Do a overriding bypass to set the level directly on client-side. While kinda weird, this
                    //allows the capability to hold what the player owns
                    stat.purchaseStat(player, true, true);
                }
                
                //add the data to the client handler for the gui to then update
                ClientProxy.CLIENT_STAT_HANDLER.putData(statID, result);
            });

        });
        context.setPacketHandled(true);
    }

}