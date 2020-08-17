package com.farinc.stats.test.instances;

import com.farinc.stats.api.implementations.instances.Component;
import com.farinc.stats.api.implementations.instances.Stat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;

public class TestStat extends Stat {

    public final int maxCount;
    private int count;

    public TestStat(Component[][] components, int maxCount) {
        super(components);
        this.maxCount = maxCount;
    }

    @Override
    public INBT writeNBT() {
        return IntNBT.valueOf(this.count);
    }

    @Override
    public void readNBT(INBT nbt) {
        this.count = ((IntNBT) nbt).getInt();
    }

    @Override
    public String toString() {
        return String.format("The stat %d/%d", this.count, this.maxCount);
    }

    @Override
    public boolean isTickable() {
        return true;
    }

    @Override
    public void update(PlayerEntity player) {
        if(this.count++ < this.count) this.count++;
        System.out.println(this.toString());
    }
    
}