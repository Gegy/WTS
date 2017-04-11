package net.gegy1000.wts.jei;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class WTSPluginJEI extends BlankModPlugin {
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
//        WhatsThatSlot.registerProvider(new JEIGuiProvider(jeiRuntime));
    }
}
