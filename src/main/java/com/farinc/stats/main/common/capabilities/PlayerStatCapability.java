package com.farinc.stats.main.common.capabilities;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import com.farinc.stats.api.Registry;
import com.farinc.stats.api.implementations.instances.Stat;
import com.farinc.stats.main.proxies.CommonProxy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.Capability.IStorage;

/**
 * Stores the instance of stats on a individual player.
 */
public class PlayerStatCapability implements ICapabilitySerializable<CompoundNBT> {

    public static interface IStatHolder {

        public void addStat(String statID, Stat stat);

        public void removeStat(String statID);

        public Set<Entry<String, Stat>> getHeldStats();

        public Stat getStat(String statID);
    }

    public static class PlayerStatHolder implements IStatHolder {

        private HashMap<String, Stat> stats = new HashMap<String, Stat>();

        @Override
        public void addStat(String statID, Stat stat) {
            if (!this.stats.containsKey(statID)) {
                this.stats.put(statID, stat);
            }
        }

        @Override
        public void removeStat(String statID) {
            this.stats.remove(statID);
        }

        @Override
        public Set<Entry<String, Stat>> getHeldStats() {
            return this.stats.entrySet();
        }

        @Override
        public Stat getStat(String statID) {
            return this.stats.get(statID);
        }
    }

    public static class PlayerStatHolderStorage implements IStorage<IStatHolder> {

        @Override
        public INBT writeNBT(Capability<IStatHolder> capability, IStatHolder instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();

            for (Entry<String, Stat> entry : instance.getHeldStats()) {
                nbt.put(entry.getKey(), entry.getValue().writeNBT());
            }

            return nbt;
        }

        @Override
        public void readNBT(Capability<IStatHolder> capability, IStatHolder instance, Direction side, INBT tag) {
            CompoundNBT nbt = (CompoundNBT) tag;

            INBT statTag;
            Stat stat;
            for (String statID : Registry.getStatsRegistered()) {
                statTag = nbt.get(statID);

                // if the stat was not on the player, the tag will be null
                if (statTag != null) {
                    stat = CommonProxy.DATA_STORAGE.getData(statID).get();
                    stat.readNBT(statTag);
                    instance.addStat(statID, stat);
                }
            }

        }

    }

    private static class PlayerStatHolderFactory implements Callable<IStatHolder> {

        @Override
        public IStatHolder call() throws Exception {
            return new PlayerStatHolder();
        }

    }

    @CapabilityInject(IStatHolder.class)
    public static final Capability<IStatHolder> STAT_CAPABILITY = null;
    private LazyOptional<IStatHolder> instance = LazyOptional.of(STAT_CAPABILITY::getDefaultInstance);

    public static void register() {
        CapabilityManager.INSTANCE.register(IStatHolder.class, new PlayerStatHolderStorage(), new PlayerStatHolderFactory());
    } 

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return STAT_CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) STAT_CAPABILITY.getStorage().writeNBT(STAT_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        STAT_CAPABILITY.getStorage().readNBT(STAT_CAPABILITY, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);

    }
    
}