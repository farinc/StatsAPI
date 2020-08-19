package com.farinc.stats.main.client.handlers;

import java.util.HashMap;

import com.farinc.stats.api.Registry;
import com.farinc.stats.api.implementations.instances.Stat.PurchaseResult;

/**
 * Effectively, this gives per client the ability to cache what the server decides this client can
 * do. From here, data regarding the components and stats is kept. However, this does NOT give the 
 * client the ability to decide for itself if a stat is purchasable, it has to ask the server via
 * the upgrade packets. Keep in mind that this is volatile, that is, the purchase results are not
 * saved. This makes this class a sort of "cache" where new data can consistently override old data
 * that then is newly rendered on GUI.
 * 
 * Such data includes fulfilled (or not fulfilled) for a particular stat. Even whether or not a stat
 * can just be purchase if the player is in creative mode or whatever.
 */
public class ClientStatHandler {
    
    private final HashMap<String, PurchaseResult> clientCache;

    public ClientStatHandler() {
        this.clientCache = new HashMap<String, PurchaseResult>();
    }

    public void putData(String statID, final PurchaseResult result){
        if(Registry.statExists(statID)) {
            this.clientCache.put(statID, result);
        }
    }

    public PurchaseResult getData(String statID){
        return this.clientCache.get(statID);
    }
}