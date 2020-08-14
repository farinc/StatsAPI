package com.farinc.stats.api.structure;

import java.util.function.Supplier;

import com.google.gson.JsonObject;

public interface IData<T extends IInstance> extends Supplier<T> {

    public Object writeData();

    public void readData(Object data);

    public void readData(JsonObject data);
    
}