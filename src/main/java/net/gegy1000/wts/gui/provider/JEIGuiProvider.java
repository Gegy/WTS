package net.gegy1000.wts.gui.provider;

import mezz.jei.api.IJeiRuntime;
import net.gegy1000.wts.gui.JEISlotGUI;
import net.gegy1000.wts.gui.SlotGUI;
import net.minecraftforge.fml.common.Loader;

public class JEIGuiProvider implements SlotGuiProvider {
    private JEISlotGUI gui;

    public JEIGuiProvider(IJeiRuntime runtime) {
        this.gui = new JEISlotGUI(runtime);
    }

    @Override
    public boolean canUse() {
        return Loader.isModLoaded("JEI");
    }

    @Override
    public SlotGUI get() {
        return this.gui;
    }
}
