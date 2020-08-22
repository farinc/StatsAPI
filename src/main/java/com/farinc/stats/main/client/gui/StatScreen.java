package com.farinc.stats.main.client.gui;

import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Iterator;
import java.util.Map.Entry;

import com.farinc.stats.StatsMain;
import com.farinc.stats.api.implementations.instances.Stat;
import com.farinc.stats.main.common.capabilities.PlayerStatCapability.IStatHolder;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class StatScreen extends Screen {
	
	/*
	 * Since this screen is identical to the inventory itself, we will use the same values and methods
	 */
	public  int xSize = 176;
	public final int ySize = 166;
	
	private int guiLeft;
	private int guiTop;
	
	/**
	 * A instance of the stat capability for quick reference
	 */
	@CapabilityInject(IStatHolder.class)
    private static final Capability<IStatHolder> STAT_CAPABILITY = null;
	
	/*
	 * The absolution positions of the elements for drawing the stat on the texture sheet
	 */
	
	//Color bars (the top one only, a function figures out the extra Y position by color)
	private int absRenderBarX = 92;
	private int absRenderBarY = 171;
	
	/**
	 * Represents the positions a stat can be rendered.
	 */
	private int[][] statPositions = {
			{6,27},
			{6,50},
			{6,73},
			{6,96},
			{6,119},
			{91,27},
			{91,50},
			{91,73},
			{91,96},
			{91,119}
	};
	
	private Entry<String, Stat>[] statsHeld;
	
	/*
	 * The relative positions of the elements for every stat
	 */
	
	//bar
	private int relRenderBarX = 5;
	private int relRenderBarY = 31;
	private int relBarLengthPx = 50;
	private int relBarHeightPx = 5;

	public static final ResourceLocation STAT_SCREEN = new ResourceLocation(StatsMain.MODID, "textures/gui/stats.png");

    public StatScreen() {
        super(new StringTextComponent("Stat Screen"));
    }
    
    @Override
    protected void init() {
    	this.guiLeft = (this.width - this.xSize) / 2;
    	this.guiTop = (this.height - this.ySize) / 2;
    	
    }
    
    @Override
    public boolean isPauseScreen() {
    	return false;
    }
    
    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
        
        RenderSystem.pushMatrix();
        RenderSystem.pushTextureAttributes();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.renderBackground();
        this.minecraft.getTextureManager().bindTexture(STAT_SCREEN);
        blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        RenderSystem.popAttributes();
        RenderSystem.popMatrix();
    }
    
    
	@SuppressWarnings("resource")
	public ClientPlayerEntity getPlayer() {
    	return this.getMinecraft().player;
    }
	
	/**
	 * Normal not required, but since this is a literative and position based rendering, the 
	 * need likewise for a order list is wanted.
	 * @param holder
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Entry<String, Stat>[] getHeldStatsFromHolder(IStatHolder holder){
		Set<Entry<String, Stat>> set = holder.getHeldStats();
		int size = set.size();
		return (Entry<String, Stat>[]) set.toArray();
	}
    
    private void renderStats(int mouseX, int mouseY) {
    	ClientPlayerEntity player = this.getPlayer();
    	
    	
    	player.getCapability(STAT_CAPABILITY).ifPresent((IStatHolder holder) -> {
    		Entry<String, Stat>[] heldStats = this.getHeldStatsFromHolder(holder);
    		
    		
    		// ----------- Testing! -----------
    		
    		Entry<String, Stat> entry;
    		int absPosX;
    		int absPosY;
    		
    		for(int posIndex = 0; posIndex < 6; posIndex++) {
    			entry = heldStats[posIndex];
    			absPosX = statPositions[posIndex][0];
    			absPosY = statPositions[posIndex][1];
    			
    			this.renderStat(mouseX, mouseY, entry.getValue(), absPosX, absPosY);
    		}
    	});
    }
    
    private void renderStat(int mouseX, int mouseY, Stat stat, int absPosX, int absPosY) {
    	this.renderLevelBar(absPosX, absPosY, "blue", stat.getLevel(), stat.getMaxLevel());
    }
    
    private void renderLevelBar(int absPosX, int absPosY, String color, int level, int maxLevel) {
    	int x = absPosX + this.relRenderBarX;
    	int y = absPosY + this.relRenderBarY;
    	
    	int absTextureX = this.absRenderBarX;
    	int absTextureY;
    	
    	/*
    	 * rendering the bar
    	 */
    	
    	//render the unfilled one at full length
    	absTextureY = this.getAbsPosYForBar(color, false);
    	blit(x, y, absTextureX, absTextureY, this.relBarLengthPx, this.relBarHeightPx);
    	//now render the filled one at the relative amount 
    	absTextureY = this.getAbsPosYForBar(color, true);
    	blit(x, y, absTextureX, absTextureY, this.calculateLevelBar(level, maxLevel), this.relBarHeightPx);
    }
    
    /**
     * Calculates the relative bar amount in terms of level
     * @param level the level of the stat
     * @param maxLevel the max level of the stat
     * @return the relative bar amount in pixels
     */
    public int calculateLevelBar(int level, int maxLevel) {
    	return (level * this.relBarLengthPx) / maxLevel;
    }
    
    /**
     * Calculates the absolute Y position of the level bar to be rendered.
     * @param color the color of the render bar
     * @param fill if true, returns the appropriate bar for color as the fill variant
     * @return
     */
    public int getAbsPosYForBar(String color, boolean fill) {
    	int total = this.absRenderBarY + (fill ? 5 : 0);
    	switch(color) {
    		case "purple": return total;
    		case "cyan": return total += 10;
    		case "orange": return total += (10 * 2);
    		case "green": return total += (10 * 3);
    		case "yellow": return total += (10 * 4);
    		case "violet": return total += (10 * 5);
    		case "white": return total += (10 * 6);
    		default: return total;
    	}
    }
    
    /**
     * Determines if the mouse is hovering is above the stat, used for the tooltip
     * @return
     */
    public boolean isHoveringOverStat() {
    	return false;
    }
    
    /**
     * Determines if the mouse is hovering is above the upgrade button for the stat
     * Used to render the tooltip for the upgrade components.
     * @return
     */
    public boolean isHoveringOverUpgrade(int statIndex) {
    	return false;
    }
    
}

//Some blit param namings, thank you Mekanism
//blit(int x, int y, int textureX, int textureY, int width, int height);
//blit(int x, int y, TextureAtlasSprite icon, int width, int height);
//blit(int x, int y, int textureX, int textureY, int width, int height, int textureWidth, int textureHeight);
//blit(int x, int y, int zLevel, float textureX, float textureY, int width, int height, int textureWidth, int textureHeight);
//blit(int x, int y, int desiredWidth, int desiredHeight, int textureX, int textureY, int width, int height, int textureWidth, int textureHeight);
//innerBlit(int x, int endX, int y, int endY, int zLevel, int width, int height, float textureX, float textureY, int textureWidth, int textureHeight);
