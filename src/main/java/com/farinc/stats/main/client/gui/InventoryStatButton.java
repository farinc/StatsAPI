package com.farinc.stats.main.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;

public class InventoryStatButton extends ImageButton {
	
	private Screen parentScreen;

	public InventoryStatButton(Screen parentScreen, int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn,
			int yDiffTextIn, ResourceLocation resourceLocationIn) {
		super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, (button) -> {
			if(Minecraft.getInstance().player == null) return;

			/*
			 * This is a button, so if the context of the current screen is the normal inventory before we switch,
			 * then the new screen will be the stat one. If it is the stat screen and we click it, we get the 
			 * normal inventory.
			 */
            if(parentScreen instanceof InventoryScreen){
                InventoryScreen inventory = (InventoryScreen) parentScreen;
                RecipeBookGui recipeBookGui = inventory.getRecipeGui();

                //disable the recipe book gui
                if (recipeBookGui.isVisible()) {
                    recipeBookGui.toggleVisibility();
                }

                Minecraft.getInstance().displayGuiScreen(new StatScreen());
            
            } else if (parentScreen instanceof StatScreen){
                StatScreen stats = (StatScreen) parentScreen;
                
                //tell minecraft to open its inventory screen again.
                //NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CPacketOpenVanilla());
            }
		});
		this.parentScreen = parentScreen;
	}

}
