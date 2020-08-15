package com.farinc.stats.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class StatsAPI {
    
    public static final StatsAPI instance = new StatsAPI();

    /**
     * A class intended for use in read data from the loader. Contains the logger 
     * from the loader itself on runtime.
     */
    public static class JsonUtils {

        @SuppressWarnings("null")
        public static Logger jsonLoaderLogger;

        public static boolean isJsonObject(JsonElement ele){
            return ele != null && ele.isJsonObject();
        }

        public static boolean isJsonArray(JsonElement ele){
            return ele != null && ele.isJsonArray();
        }

        public static boolean isBoolean(JsonElement ele){
            return ele != null && ele.isJsonPrimitive() && ((JsonPrimitive) ele).isBoolean();
        }

        public static boolean isString(JsonElement ele){
            return ele != null && ele.isJsonPrimitive() && ((JsonPrimitive) ele).isString();
        }

        public static boolean isNumber(JsonElement ele){
            return ele != null && ele.isJsonPrimitive() && ((JsonPrimitive) ele).isNumber();
        }

        public static boolean isInteger(JsonElement ele){
            if(isNumber(ele)){
                try {
                    ele.getAsInt();
                    return true;
                } catch (ClassCastException | IllegalStateException e) {
                    jsonLoaderLogger.catching(Level.DEBUG, e);
                }
            }
            return false;
        }

        public static boolean isFloat(JsonElement ele){
            if(isNumber(ele)){
                try {
                    ele.getAsFloat();
                    return true;
                } catch (ClassCastException | IllegalStateException e) {
                    jsonLoaderLogger.catching(Level.DEBUG, e);
                }
            }
            return false;
        }

        public static boolean isDouble(JsonElement ele){
            if(isNumber(ele)){
                try {
                    ele.getAsDouble();
                    return true;
                } catch (ClassCastException | IllegalStateException e) {
                    jsonLoaderLogger.catching(Level.DEBUG, e);
                }
            }
            return false;
        }
    }
}