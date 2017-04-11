package net.gegy1000.wts;

import net.gegy1000.wts.gui.SlotGUI;
import net.gegy1000.wts.gui.provider.SlotGuiProvider;
import net.gegy1000.wts.gui.provider.VanillaGuiProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Mod(modid = WhatsThatSlot.MODID, name = "What's That Slot", version = WhatsThatSlot.VERSION, clientSideOnly = true)
public class WhatsThatSlot {
    public static final String MODID = "wts";
    public static final String VERSION = "1.1.0";

    public static final KeyBinding KEY_CHECK_SLOT = new KeyBinding("Check Slot", Keyboard.KEY_P, "What's That Slot");

    public static final List<ItemStack> ITEMS = new LinkedList<>();

    private static final SlotGuiProvider VANILLA_PROVIDER = new VanillaGuiProvider();
    private static final List<SlotGuiProvider> GUI_PROVIDERS = new ArrayList<>();

    private SlotGUI slotGUI;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        for (Item item : Item.REGISTRY) {
            List<ItemStack> stacks = new ArrayList<>(16);
            item.getSubItems(item, item.getCreativeTab(), stacks);
            ITEMS.addAll(stacks);
        }

        ClientRegistry.registerKeyBinding(KEY_CHECK_SLOT);
    }

    @SubscribeEvent
    public void onGUIChange(GuiOpenEvent event) {
        if (!(event.getGui() instanceof GuiContainer)) {
            this.hide();
        }
    }

    @SubscribeEvent
    public void onKeyPress(GuiScreenEvent.KeyboardInputEvent event) {
        GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
        if (Keyboard.isKeyDown(KEY_CHECK_SLOT.getKeyCode()) && currentScreen instanceof GuiContainer) {
            GuiContainer container = (GuiContainer) currentScreen;
            Slot selectedSlot = container.getSlotUnderMouse();
            if (selectedSlot != null) {
                this.slotGUI = WhatsThatSlot.provideSlotGUI();
                if (this.slotGUI != null) {
                    MinecraftForge.EVENT_BUS.register(this.slotGUI);
                    this.slotGUI.show(SlotPlaceInfo.create(selectedSlot));
                }
            } else {
                this.hide();
            }
        }
    }

    private void hide() {
        if (this.slotGUI != null) {
            MinecraftForge.EVENT_BUS.unregister(this.slotGUI);
            this.slotGUI.hide();
            this.slotGUI = null;
        }
    }

    public static SlotGUI provideSlotGUI() {
        for (SlotGuiProvider provider : GUI_PROVIDERS) {
            if (provider.canUse()) {
                return provider.get();
            }
        }
        return VANILLA_PROVIDER.get();
    }

    public static void registerProvider(SlotGuiProvider provider) {
        GUI_PROVIDERS.add(provider);
    }
}
