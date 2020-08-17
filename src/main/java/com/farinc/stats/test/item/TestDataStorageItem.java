package com.farinc.stats.test.item;

import com.farinc.stats.StatsMain;
import com.farinc.stats.main.network.NetworkHandler;
import com.farinc.stats.main.network.SPacketTest;
import com.farinc.stats.main.proxies.CommonProxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class TestDataStorageItem extends Item {
    
    public TestDataStorageItem() {
        super(new Item.Properties().maxStackSize(1));
        setRegistryName(StatsMain.MODID, "testdatastorageitem");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        int size;
        if(!worldIn.isRemote) {
            //logical server
            size = CommonProxy.DATA_STORAGE.getDataSize();
            System.out.println("[Server-Side] Data Size: " + size);
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ((ServerPlayerEntity) playerIn)), new SPacketTest());
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}