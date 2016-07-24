package net.gegy1000.wts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class SlotRenderer {
    private static final Minecraft MC = Minecraft.getMinecraft();

    private static final ResourceLocation TEXTURE = new ResourceLocation(WhatsThatSlot.MODID, "textures/gui/gui.png");

    private boolean draggingScrollbar;

    @SubscribeEvent
    public void onGUIRender(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (WhatsThatSlot.infoSlot != null && MC.currentScreen instanceof GuiContainer) {
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
            if (WhatsThatSlot.scroll > WhatsThatSlot.maxScroll) {
                WhatsThatSlot.scroll = WhatsThatSlot.maxScroll;
            }
            int mouseX = event.getMouseX();
            int mouseY = event.getMouseY();
            int slotX = 0;
            int slotY = -WhatsThatSlot.scroll;
            RenderHelper.enableGUIStandardItemLighting();
            for (ItemStack stack : WhatsThatSlot.VALID_ITEMS) {
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
            }
            GlStateManager.disableLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            MC.getTextureManager().bindTexture(TEXTURE);
            int scrollbarY = (renderY + 7) + (int) (WhatsThatSlot.maxScroll == 0 ? 0 : (((float) WhatsThatSlot.scroll / WhatsThatSlot.maxScroll) * 127));
            if (Mouse.isButtonDown(0) && mouseX >= 99 && mouseX <= 109 && mouseY >= scrollbarY && mouseY <= scrollbarY + 15) {
                this.draggingScrollbar = true;
            } else if (!Mouse.isButtonDown(0)) {
                this.draggingScrollbar = false;
            }
            if (this.draggingScrollbar) {
                WhatsThatSlot.scroll = (int) ((mouseY - (renderY + 7)) / 127.0F * WhatsThatSlot.maxScroll);
            }
            if (mouseX <= 117) {
                int scrollwheel = Mouse.getDWheel();
                if (scrollwheel > 0) {
                    WhatsThatSlot.scroll--;
                } else if (scrollwheel < 0) {
                    WhatsThatSlot.scroll++;
                }
            }
            WhatsThatSlot.scroll = Math.max(0, Math.min(WhatsThatSlot.maxScroll, WhatsThatSlot.scroll));
            this.drawTexturedRectangle(100, scrollbarY, this.draggingScrollbar ? 246 : 236, 0, 10, 15, 256, 256);
            slotX = 0;
            slotY = -WhatsThatSlot.scroll;
            for (ItemStack stack : WhatsThatSlot.VALID_ITEMS) {
                if (slotY >= 0) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    int renderSlotX = slotX * 18 + 7;
                    int renderSlotY = slotY * 18 + renderY + 7;
                    if (mouseX >= renderSlotX && mouseX <= renderSlotX + 16 && mouseY >= renderSlotY && mouseY <= renderSlotY + 16) {
                        GlStateManager.disableLighting();
                        GlStateManager.disableDepth();
                        GlStateManager.colorMask(true, true, true, false);
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
                        this.drawRectangle(renderSlotX, renderSlotY, 16, 16);
                        GlStateManager.colorMask(true, true, true, true);
                        GlStateManager.enableLighting();
                        GlStateManager.enableDepth();
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                        GuiUtils.drawHoveringText(stack.getTooltip(MC.thePlayer, MC.gameSettings.advancedItemTooltips), mouseX, mouseY, resolution.getScaledWidth(), resolution.getScaledHeight(), -1, MC.fontRendererObj);
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

    @SubscribeEvent
    public void onGUIChange(GuiOpenEvent event) {
        if (!(event.getGui() instanceof GuiContainer)) {
            WhatsThatSlot.infoSlot = null;
            WhatsThatSlot.VALID_ITEMS.clear();
        }
    }

    private void drawRectangle(int x, int y, int width, int height) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        buffer.pos(x, y + height, 0.0).endVertex();
        buffer.pos(x + width, y + height, 0.0).endVertex();
        buffer.pos(x + width, y, 0.0).endVertex();
        buffer.pos(x, y, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    private void drawTexturedRectangle(int x, int y, int textureX, int textureY, int width, int height, int textureWidth, int textureHeight) {
        float widthScale = 1.0F / textureWidth;
        float heightScale = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, 0.0).tex(textureX * widthScale, (textureY + height) * heightScale).endVertex();
        buffer.pos(x + width, y + height, 0.0).tex((textureX + width) * widthScale, (textureY + height) * heightScale).endVertex();
        buffer.pos(x + width, y, 0.0).tex((textureX + width) * widthScale, textureY * heightScale).endVertex();
        buffer.pos(x, y, 0.0).tex(textureX * widthScale, textureY * heightScale).endVertex();
        tessellator.draw();
    }
}
