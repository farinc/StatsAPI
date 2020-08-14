package com.farinc.stats.api.structure;

import java.util.function.Supplier;

import com.google.gson.JsonObject;

/**
 * Represents the original data from some source. In this case, data is pulled from 1) the stats.json
 * and 2) the server to client registry packet that effectively copies the data the server has loaded
 * from its json. This {@code IData} acts as a factory, producing unique instances of this data, the
 * {@code IInstance} via the supplier functionality.
 * 
 * @param <T> The {@code IInstance} that corresponds with this {@code IData}
 */
public interface IData<T extends IInstance<?>> extends Supplier<T> {

    public Object writeData();

    public void readData(Object data);

    public void readData(JsonObject data);
    
}