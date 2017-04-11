package net.gegy1000.wts.gui;

import net.gegy1000.wts.SlotPlaceInfo;
import net.gegy1000.wts.WhatsThatSlot;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class VanillaSlotGUI extends SlotGUI {
    private static final ResourceLocation TEXTURE = new ResourceLocation(WhatsThatSlot.MODID, "textures/gui/gui.png");

    private SlotPlaceInfo placeInfo;
    private int scroll;
    private boolean draggingScrollbar;

    @Override
    public void show(SlotPlaceInfo placeInfo) {
        this.scroll = 0;
        this.placeInfo = placeInfo;
    }

    @Override
    public void hide() {
        this.placeInfo = null;
    }

    @SubscribeEvent
    public void onGUIRender(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (this.placeInfo != null && MC.currentScreen instanceof GuiContainer) {
            RenderItem renderItem = MC.getRenderItem();
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.disableLighting();
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            MC.getTextureManager().bindTexture(TEXTURE);
            ScaledResolution resolution = new ScaledResolution(MC);
            int renderY = (resolution.getScaledHeight() / 2) - (156 / 2);
            this.drawTexturedRectangle(0, renderY, 0, 0, 117, 156, 256, 256);
            MC.fontRendererObj.drawStringWithShadow("What's That Slot?", 14, renderY - 10, 0xFFFFFF);
            int maxScroll = this.placeInfo.getMaxScroll();
            if (this.scroll > maxScroll) {
                this.scroll = maxScroll;
            }
            int mouseX = event.getMouseX();
            int mouseY = event.getMouseY();
            int slotX = 0;
            int slotY = -this.scroll;
            RenderHelper.enableGUIStandardItemLighting();
            for (ItemStack stack : this.placeInfo.getValidItems()) {
                try {
                    if (slotY >= 0) {
                        int renderSlotX = slotX * 18 + 7;
                        int renderSlotY = slotY * 18 + renderY + 7;
                        renderItem.renderItemIntoGUI(stack, renderSlotX, renderSlotY);
                    }
                    slotX++;
                    if (slotX >= 5) {
                        slotY++;
                        slotX = 0;
                    }
                    if (slotY >= 8) {
                        break;
                    }
                } catch (Exception e) {
                    System.err.println("Exception while rendering ItemStack");
                    e.printStackTrace();
                }
            }
            GlStateManager.disableLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            MC.getTextureManager().bindTexture(TEXTURE);
            int scrollbarY = (renderY + 7) + (int) (maxScroll == 0 ? 0 : (((float) this.scroll / maxScroll) * 127));
            if (Mouse.isButtonDown(0) && mouseX >= 99 && mouseX <= 109 && mouseY >= scrollbarY && mouseY <= scrollbarY + 15) {
                this.draggingScrollbar = true;
            } else if (!Mouse.isButtonDown(0)) {
                this.draggingScrollbar = false;
            }
            if (this.draggingScrollbar) {
                this.scroll = (int) ((mouseY - (renderY + 7)) / 127.0F * maxScroll);
            }
            if (mouseX <= 117) {
                int scrollAmount = Mouse.getDWheel();
                if (scrollAmount > 0) {
                    this.scroll--;
                } else if (scrollAmount < 0) {
                    this.scroll++;
                }
            }
            this.scroll = Math.max(0, Math.min(maxScroll, this.scroll));
            this.drawTexturedRectangle(100, scrollbarY, this.draggingScrollbar ? 246 : 236, 0, 10, 15, 256, 256);
            slotX = 0;
            slotY = -this.scroll;
            for (ItemStack stack : this.placeInfo.getValidItems()) {
                if (slotY >= 0) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    int renderSlotX = slotX * 18 + 7;
                    int renderSlotY = slotY * 18 + renderY + 7;
                    if (mouseX >= renderSlotX && mouseX <= renderSlotX + 17 && mouseY >= renderSlotY && mouseY <= renderSlotY + 17) {
                        this.renderSelected(resolution, mouseX, mouseY, stack, renderSlotX, renderSlotY);
                    }
                }
                slotX++;
                if (slotX >= 5) {
                    slotY++;
                    slotX = 0;
                }
                if (slotY >= 8) {
                    break;
                }
            }
        }
    }
}
