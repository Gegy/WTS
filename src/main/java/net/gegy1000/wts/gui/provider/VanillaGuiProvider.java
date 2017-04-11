package net.gegy1000.wts.gui.provider;

import net.gegy1000.wts.gui.SlotGUI;
import net.gegy1000.wts.gui.VanillaSlotGUI;

public class VanillaGuiProvider implements SlotGuiProvider {
    private VanillaSlotGUI gui = new VanillaSlotGUI();

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public SlotGUI get() {
        return this.gui;
    }
}
