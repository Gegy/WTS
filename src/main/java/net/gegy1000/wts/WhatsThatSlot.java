package net.gegy1000.wts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.LinkedList;
import java.util.List;

@Mod(modid = WhatsThatSlot.MODID, name = "What's That Slot", version = WhatsThatSlot.VERSION, clientSideOnly = true)
public class WhatsThatSlot {
    public static final String MODID = "wts";
    public static final String VERSION = "1.0.0";

    public static final KeyBinding KEY_CHECK_SLOT = new KeyBinding("Check Slot", Keyboard.KEY_P, "What's That Slot");

    public static Slot infoSlot;

    public static final List<ItemStack> ITEMS = new LinkedList<>();

    public static final List<ItemStack> VALID_ITEMS = new LinkedList<>();
    public static int scroll;
    public static int maxScroll;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new SlotRenderer());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        for (Item item : Item.REGISTRY) {
            item.getSubItems(item, item.getCreativeTab(), ITEMS);
        }
        ClientRegistry.registerKeyBinding(KEY_CHECK_SLOT);
    }

    @SubscribeEvent
    public void onKeyPress(GuiScreenEvent.KeyboardInputEvent event) {
        GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
        if (Keyboard.isKeyDown(KEY_CHECK_SLOT.getKeyCode()) && currentScreen instanceof GuiContainer) {
            GuiContainer container = (GuiContainer) currentScreen;
            infoSlot = container.getSlotUnderMouse();
            VALID_ITEMS.clear();
            if (infoSlot != null) {
                for (ItemStack stack : WhatsThatSlot.ITEMS) {
                    if (stack != null && WhatsThatSlot.infoSlot.isItemValid(stack)) {
                        VALID_ITEMS.add(stack);
                    }
                }
            }
            scroll = 0;
            maxScroll = VALID_ITEMS.size() > 0 ? (int) Math.max(0, Math.ceil(VALID_ITEMS.size() / 5.0F) - 8) : 0;
        }
    }
}
