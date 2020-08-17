package com.farinc.stats.main.common.configuration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.farinc.stats.StatsMain;
import com.farinc.stats.api.Registry;
import com.farinc.stats.api.StatsAPI;
import com.farinc.stats.api.StatsAPI.JsonUtils;
import com.farinc.stats.api.implementations.data.ComponentData;
import com.farinc.stats.api.implementations.data.StatData;
import com.farinc.stats.main.proxies.CommonProxy;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.JSONUtils;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

/**
 * This does the loading of the stat JSON. Later, if it becomes available, this
 * will switch to a datapack. However, due to the limited scope of the system,
 * this will suffice for now.
 */
@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = StatsMain.MODID)
public class StatJSONLoader {

    public final static Logger LOGGER = LogManager.getLogger();

    static {
        StatsAPI.JsonUtils.jsonLoaderLogger = LOGGER; //passes a reference to the api.
    }

    public static class StatJsonSyntaxException extends Exception {

        private static final long serialVersionUID = 1L;

        public StatJsonSyntaxException(String message) {
            super(message);
        }
    }

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event) {

        StatsMain.LOGGER.info("-------- Beginning JSON Loading --------");

        if(event.getPhase() == EventPriority.NORMAL){

            File statJSON = event.getServer().getFile(String.format("config%splayer-stats%sstats.json", File.separator, File.separator));
            try {
                JsonObject statData = JSONUtils.fromJson(new FileReader(statJSON.getCanonicalFile()));
                parseStatData(statData);
            } catch (StatJsonSyntaxException | IOException e) {
                LOGGER.catching(Level.ERROR, e);
            }
        }
    }

    private static ComponentData<?> parseComponent(JsonObject component) throws StatJsonSyntaxException{
        JsonElement element0 = component.get("componentID");
        
        if(JsonUtils.isString(element0)) {
            String componentID = element0.getAsString();

            if(Registry.componentExists(componentID)){
                LOGGER.debug(String.format("Parsing component \"%s\"...", componentID));
                ComponentData<?> componentData = Registry.getComponentDataFactory(componentID).get();
                
                JsonElement element1 = component.get("data");
                if(JsonUtils.isJsonObject(element1)){
                    LOGGER.debug(String.format("Reading data for component \"%s\"...", componentID));
                    componentData.readData(element1.getAsJsonObject());
                    return componentData;
                }else{
                    throw new StatJsonSyntaxException("the \"data\" member either does not exits or is not a object!");
                }
            }else{
                throw new StatJsonSyntaxException("the componentID specified is not a known component!");
            }
        }else{
            throw new StatJsonSyntaxException("the member \"componentID\" either does not exist or its not a string!");
        }
    }

    private static void parseStat(JsonObject statJsonData) throws StatJsonSyntaxException {
        JsonElement element00 = statJsonData.get("statID");

        if(JsonUtils.isString(element00)){
            String statID = element00.getAsString();

            //Check the registry if this statID even exists and that it hasn't already been processed
            if(Registry.statExists(statID) && !CommonProxy.DATA_STORAGE.statExists(statID)){
                StatData<?> statData = Registry.getStatDataFactory(statID).get();
                JsonElement element0 = statJsonData.get("components");

                //Check for the components array
                if(JsonUtils.isJsonArray(element0)) {
                    JsonArray levelArray = element0.getAsJsonArray();
                    
                    JsonArray componentsPerLevelArray;
                    ComponentData<?>[][] components = new ComponentData<?>[levelArray.size()][];
                    ComponentData<?>[] componentsPerLevel;
                    int level = 0;
                    int next;

                    //So for a given stat, the outer array are arrays of components for a level (index + 1). 
                    for(JsonElement element1 : levelArray){
                        if(JsonUtils.isJsonArray(element1)){

                            componentsPerLevelArray = element1.getAsJsonArray();

                            //So for a given level, determine the components one by one.
                            componentsPerLevel = new ComponentData[componentsPerLevelArray.size()];
                            next = 0;
                            for(JsonElement element2 : componentsPerLevelArray){
                                if(JsonUtils.isJsonObject(element2)){
                                    //insert each parsed component into a sub array
                                    componentsPerLevel[next] = parseComponent(element2.getAsJsonObject());
                                }else{
                                    throw new StatJsonSyntaxException("this is not a object, a component object is required");
                                }
                                next++;
                            }

                            LOGGER.debug(String.format("For stat \"%s\", at level %d there where %d components", statID, level, componentsPerLevelArray.size()));
                            //thus the sub array are the components per level.
                            components[level] = componentsPerLevel;
                        }else{
                            throw new StatJsonSyntaxException("the inner array of the components array is not an array");
                        }

                        level++;
                    }

                    statData.setComponentData(components);

                }else{
                    throw new StatJsonSyntaxException(String.format("the stat \"%s\" does not have a \"components\" member that is a array",statID));
                }

                //extract properties 
                
                JsonElement element3 = statJsonData.get("data");
                if(JsonUtils.isJsonObject(element3)){
                    LOGGER.debug(String.format("Reading data of stat \"%s\"", statID));
                    statData.readData(element3.getAsJsonObject());
                }else{
                    throw new StatJsonSyntaxException(String.format("no data member exits for stat \"%s\"!", statID));
                }

                //add it to the runtime storage
                CommonProxy.DATA_STORAGE.setData(statID, statData);

            }else{
                throw new StatJsonSyntaxException("the statID specified is not a known stat or has already been processed!");
            }
        }else{
            throw new StatJsonSyntaxException("the member \"statID\" either does not exist or its not a string!");
        }
    }

    private static void parseStatData(JsonObject json) throws StatJsonSyntaxException {
        JsonElement element01 = json.get("version");
        if(JsonUtils.isNumber(element01)){
            float version = element01.getAsFloat();
            LOGGER.info(String.format("Loaded stats.json file with version %f", version));
        }else{
            LOGGER.warn("There was no version member in the stats.json. While not required, consider if this stats.json is up to date!");
        }

        JsonElement element00 = json.get("stats");
        if(JsonUtils.isJsonArray(element00)) {
            JsonArray statsDataArray = element00.getAsJsonArray();

            //So here we literate over each json stat object, this contains the id and components
            for(JsonElement element : statsDataArray) {
                if(JsonUtils.isJsonObject(element)) {
                    parseStat(element.getAsJsonObject());
                }else{
                    throw new StatJsonSyntaxException("the stats array contains a non-object value, which cannot be a stat object!");
                }
            }
        }else{
            throw new StatJsonSyntaxException("the stats array either doesn't exist or is not an array!");
        }
    }
}