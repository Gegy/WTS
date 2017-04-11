package net.gegy1000.wts.gui.provider;

import net.gegy1000.wts.gui.SlotGUI;

public interface SlotGuiProvider {
    boolean canUse();

    SlotGUI get();
}
