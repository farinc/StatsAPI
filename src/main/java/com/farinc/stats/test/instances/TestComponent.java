package com.farinc.stats.test.instances;

import com.farinc.stats.api.implementations.instances.Component;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.INBT;

public class TestComponent extends Component {

    public final int xp;

    public TestComponent(int xp) {
        this.xp = xp;
    }

    @Override
    public INBT writeNBT() {
        return null;
    }

    @Override
    public void readNBT(INBT nbt) {
    }

    @Override
    public boolean isTickable() {
        return false;
    }

    @Override
    public void update(PlayerEntity player) {}
}