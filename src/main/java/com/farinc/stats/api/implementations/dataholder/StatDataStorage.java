package com.farinc.stats.api.implementations.dataholder;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import com.farinc.stats.api.Registry;
import com.farinc.stats.api.implementations.data.StatData;
import com.farinc.stats.api.structure.IDataStorage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public class StatDataStorage implements IDataStorage<StatData<?>, String, CompoundNBT> {

    private HashMap<String, StatData<?>> data = new HashMap<String, StatData<?>>();

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        for(Entry<String, StatData<?>> entry : this.data.entrySet()){
            nbt.put(entry.getKey(), entry.getValue().writeData());
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        Set<String> ids = Registry.getStatsRegistered();
        INBT dataTag;
        StatData<?> data;

        for(String statID : ids){
            dataTag = nbt.get(statID);
            if(dataTag != null){
                data = Registry.getStatDataFactory(statID).get();
                data.readData(dataTag);
                this.setData(statID, data);
            }
        }
    }

    @Override
    public StatData<?> getData(String key) {
        return this.data.get(key);
    }

    @Override
    public void setData(String key, StatData<?> data) {
        this.data.put(key, data);
    }

    public boolean statExists(String key){
        return this.data.containsKey(key);
    }
    
    public int getDataSize(){
        return this.data.size();
    }
}