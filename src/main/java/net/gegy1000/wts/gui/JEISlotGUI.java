package net.gegy1000.wts.gui;

import mezz.jei.api.IJeiRuntime;
import net.gegy1000.wts.SlotPlaceInfo;

import java.util.Collections;

public class JEISlotGUI extends SlotGUI {
    private IJeiRuntime runtime;

    public JEISlotGUI(IJeiRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    public void show(SlotPlaceInfo placeInfo) {
        this.runtime.getItemListOverlay().highlightStacks(placeInfo.getValidItems());
    }

    @Override
    public void hide() {
        this.runtime.getItemListOverlay().highlightStacks(Collections.emptyList());
    }
}
