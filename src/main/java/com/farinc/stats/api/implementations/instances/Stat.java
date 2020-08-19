package com.farinc.stats.api.implementations.instances;

import java.util.Arrays;

import com.farinc.stats.api.structure.IInstance;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;

public abstract class Stat implements IInstance {

    /**
     * The current level of this stat
     */
    private int level;

    /**
     * The components of this stat
     */
    private final Component[][] components;

    /**
     * The basic constructor of a stat. Data passed here should be from the
     * stats.json or some constant (at least at runtime) source and should be
     * considered "final" data.
     * 
     * @param components With the current data structure, the components are passed
     *                   by this stats corresponding {@code StatData}
     *                   implementation.
     */
    public Stat(Component[][] components) {
        this.components = components;

        // Start this stat at level zero
        this.level = 0;
    }

    public final int getLevel() {
        return level;
    }

    public final int getMaxLevel() {
        return this.components.length;
    }

    public final Component[][] getComponents() {
        return components;
    }

    /**
     * Give the components required to achieve the upgrade to the next level.
     * 
     * @return the components required to complete
     */
    public final Component[] getUpgradeComponents() {
        return this.components[this.level];
    }

    protected final void setLevel(int level) {
        if (level <= this.getMaxLevel() && level >= 0) {
            this.level = level;
        }
    }

    /**
     * Determines if the player can purchase the next level by checking all
     * components at the current level. For example, to get from level 0 (nothing)
     * to level 1, you must complete all level 0 components before your stat becomes
     * level 1.
     * 
     * @param player   the player in question
     * @param purchase true if the intend is to purchase the stat by letting the
     *                 components enforce the purchase. False if the intend is to
     *                 just check. Also the enforcement will only happen if all
     *                 components can be purchased.
     * @param bypass   a flag to skip checking the components and effectively level
     *                 up directly using {@link #setLevel(int)} used in certain
     *                 situations to just upgrade immediately (i.e creative mode).
     *                 If true, the return array will always be true;
     * @return A array of boolean values indicating the components that either were
     *         satisfied (true) or were not so (false)
     */
    public final PurchaseResult purchaseStat(PlayerEntity player, boolean purchase, boolean bypass) {
        Component[] components = this.getUpgradeComponents();
        boolean[] passing = new boolean[components.length];
        int newLevel = this.level + 1;

        if (bypass) {
            this.setLevel(this.level + 1);
            Arrays.fill(passing, true);
            return new PurchaseResult(passing, true, purchase, bypass, newLevel);
        } else {
            // Effectively, the flag logic here is that once any particular component can't
            // purchase, it goes false.
            // Also, the flag completely fails from the start if purchase is false from the
            // start.

            boolean flag = purchase; // determines when the real "purchase" happens or not.
            Component[] componentsLevel = this.getUpgradeComponents();
            for (int i = 0; i < components.length; i++) {
                passing[i] = componentsLevel[i].enforcePurchase(player, false);
                flag &= passing[i];
            }

            if (flag) {
                // Carry out enforcement, or payment really...
                for (int i = 0; i < components.length; i++) {
                    componentsLevel[i].enforcePurchase(player, true);
                }

                this.setLevel(this.level + 1);
            }

            return new PurchaseResult(passing, flag, purchase, bypass, newLevel);
        }
    }

    public static class PurchaseResult {

        /**
         * A simple enum that gives a reason for the result. 
         */
        public static enum Reason {
            FREEPURCHASE((byte) 0), //used under any circumstance that grants the ability to freely purchase.
            PAID((byte) 1), //normal conditions where the stat is paid for, nothing special.
            FAIL((byte) 2), //normal conditions where the stat is unable to be paid for, nothing special.
            CHECK_PAID((byte) 3), //normal conditions where the stat is not paid to test the players ability to pay.
            CHECK_FAIL((byte) 4);
        
            public final byte value;
            private Reason(byte value){
                this.value = value;
            }

            public static Reason getFromValue(byte value){
                switch(value){
                    case (byte) 0: return FREEPURCHASE;
                    case (byte) 1: return PAID;
                    case (byte) 2: return FAIL;
                    case (byte) 3: return CHECK_PAID;
                    case (byte) 4: return CHECK_FAIL;
                    default: return CHECK_FAIL;
                }
            }
        }

        private Reason reason;
        private boolean[] componentsPassing;
        private int newLevel;

        public boolean[] getComponentsPassing() {
            return this.componentsPassing;
        }

        public int getNewLevel() {
            return this.newLevel;
        }

        public Reason getReason(){
            return this.reason;
        }

        public PurchaseResult(boolean[] componentsPassing, boolean passed, boolean purchased, boolean bypass, int newLevel) {
            this.componentsPassing = componentsPassing;
            this.newLevel = newLevel;

            if(purchased && passed){
                this.reason = Reason.PAID;
            }else if(purchased && !passed){
                this.reason = Reason.FAIL;
            }else if(!purchased && passed){
                this.reason = Reason.CHECK_PAID;
            }else if(!purchased && !passed){
                this.reason = Reason.CHECK_FAIL;
            }
        }

        public PurchaseResult() {}

        public void serialize(PacketBuffer buff){
            buff.writeInt(this.newLevel);
            buff.writeByte(this.reason.value);
            buff.writeByteArray(convertBooleansToBytes(this.componentsPassing));
        }

        public void deserialize(PacketBuffer buff){
            this.newLevel = buff.readInt();
            this.reason = Reason.getFromValue(buff.readByte());
            this.componentsPassing = convertBytesToBooleans(buff.readByteArray());
        }

        public static byte[] convertBooleansToBytes(boolean[] bools){
            byte[] bytes = new byte[bools.length];
            for(int i = 0; i < bools.length; i++){
                bytes[i] = (byte) (bools[i] ? 1 : 0);
            }
    
            return bytes;
        }
    
        public static boolean[] convertBytesToBooleans(byte[] bytes){
            boolean[] bools = new boolean[bytes.length];
            for(int i = 0; i < bytes.length; i++){
                bools[i] = bytes[i] == (byte) 1 ? true : false;
            }
    
            return bools;
        }
    }
}