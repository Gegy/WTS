package net.gegy1000.wts.gui;

import net.gegy1000.wts.SlotPlaceInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.opengl.GL11;

public abstract class SlotGUI {
    protected static final Minecraft MC = Minecraft.getMinecraft();

    private int scroll;

    public abstract void show(SlotPlaceInfo placeInfo);

    public abstract void hide();

    public int getScroll() {
        return this.scroll;
    }

    public void setScroll(int scroll) {
        this.scroll = scroll;
    }

    protected void renderSelected(ScaledResolution resolution, int mouseX, int mouseY, ItemStack stack, int renderSlotX, int renderSlotY) {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.colorMask(true, true, true, false);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
        this.drawRectangle(renderSlotX, renderSlotY, 16, 16);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ITooltipFlag.TooltipFlags flag = MC.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL;
        GuiUtils.drawHoveringText(stack.getTooltip(MC.player, flag), mouseX, mouseY, resolution.getScaledWidth(), resolution.getScaledHeight(), -1, MC.fontRenderer);
    }

    protected void drawRectangle(int x, int y, int width, int height) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
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

    protected void drawTexturedRectangle(int x, int y, int textureX, int textureY, int width, int height, int textureWidth, int textureHeight) {
        float widthScale = 1.0F / textureWidth;
        float heightScale = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, 0.0).tex(textureX * widthScale, (textureY + height) * heightScale).endVertex();
        buffer.pos(x + width, y + height, 0.0).tex((textureX + width) * widthScale, (textureY + height) * heightScale).endVertex();
        buffer.pos(x + width, y, 0.0).tex((textureX + width) * widthScale, textureY * heightScale).endVertex();
        buffer.pos(x, y, 0.0).tex(textureX * widthScale, textureY * heightScale).endVertex();
        tessellator.draw();
    }
}
