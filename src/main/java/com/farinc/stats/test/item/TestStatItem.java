package com.farinc.stats.test.item;

import com.farinc.stats.StatsMain;
import com.farinc.stats.api.implementations.instances.Stat;
import com.farinc.stats.main.common.capabilities.PlayerStatCapability;
import com.farinc.stats.main.common.capabilities.PlayerStatCapability.IStatHolder;
import com.farinc.stats.main.proxies.CommonProxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class TestStatItem extends Item {

    public TestStatItem() {
        super(new Item.Properties().maxStackSize(1));
        setRegistryName(StatsMain.MODID, "teststatitem");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(!worldIn.isRemote) {
            
            System.out.println(playerIn.getCapability(PlayerStatCapability.STAT_CAPABILITY).isPresent());
            
            playerIn.getCapability(PlayerStatCapability.STAT_CAPABILITY).ifPresent((IStatHolder t) -> {
                Stat stat = CommonProxy.DATA_STORAGE.getData("test").get();
                
                System.out.println(stat);

                t.addStat("test", stat);
                playerIn.sendStatusMessage(new StringTextComponent("Added test stat"), false);
            });
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
    
}