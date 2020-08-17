package com.farinc.stats.test.data;

import com.farinc.stats.api.StatsAPI.JsonUtils;
import com.farinc.stats.api.implementations.data.ComponentData;
import com.farinc.stats.test.instances.TestComponent;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;

public class TestComponentData extends ComponentData<TestComponent> {

    private int xp;

    @Override
    public INBT writeData() {
        return IntNBT.valueOf(this.xp);
    }

    @Override
    public void readData(INBT data) {
        this.xp = ((IntNBT) data).getInt();
    }

    @Override
    public void readData(JsonObject data) {
        JsonElement ele0 = data.get("xp");
        if(JsonUtils.isInteger(ele0)){
            this.xp = ele0.getAsInt();
        }
    }

    @Override
    public TestComponent get() {
        return new TestComponent(this.xp);
    }
    
}