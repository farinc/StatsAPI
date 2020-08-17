package com.farinc.stats.test.data;

import com.farinc.stats.StatsMain;
import com.farinc.stats.api.StatsAPI.JsonUtils;
import com.farinc.stats.api.implementations.data.StatData;
import com.farinc.stats.main.common.configuration.StatJSONLoader.StatJsonSyntaxException;
import com.farinc.stats.test.instances.TestStat;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = StatsMain.MODID)
public class TestStatData extends StatData<TestStat> {

    private int maxCount;

    @Override
    public INBT writeData() {
        return IntNBT.valueOf(this.maxCount);
    }

    @Override
    public void readData(INBT data) {
        this.maxCount = ((IntNBT) data).getInt();
    }

    @Override
    public void readData(JsonObject data) throws StatJsonSyntaxException {
        JsonElement ele = data.get("maxCount");
        if(JsonUtils.isInteger(ele)){
            this.maxCount = ele.getAsInt();
            System.out.println("Got maxCount parsed: " + this.maxCount);
        }else{
            throw new StatJsonSyntaxException(String.format("missing maxCount member in \"%s\" stat", "test"));
        }
    }

    @Override
    public TestStat get() {
        TestStat stat = new TestStat(this.getComponentInstances(), this.maxCount);
        return stat;
    }

    
}