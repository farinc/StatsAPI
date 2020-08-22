package com.farinc.stats.main.common.handlers;

import com.farinc.stats.StatsMain;
import com.farinc.stats.main.network.NetworkHandler;
import com.farinc.stats.main.network.SPacketDataStorageSync;
import com.farinc.stats.main.proxies.CommonProxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.PacketDistributor;;

@EventBusSubscriber(modid = StatsMain.MODID)
public class ClientConnection {
	

    @SubscribeEvent
    public static void onClientConnection(PlayerLoggedInEvent event){
        PlayerEntity player = event.getPlayer();

        if(!player.world.isRemote){
            //As far as I know, a ServerPlayerEntity also is a container for the inventory...so it should be castable...
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;

            //In the future, lets store the serialization for future reference and generate it on post-init or something.
            SPacketDataStorageSync packet = new SPacketDataStorageSync(CommonProxy.DATA_STORAGE.serializeNBT());
            CommonProxy.NETWORK_HANDLER.getChannel().send(PacketDistributor.PLAYER.with(() -> serverPlayerEntity), packet);
        }
    }
    
}