package net.gegy1000.wts;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SlotPlaceInfo {
    private final Slot slot;
    private final Set<ItemStack> validItems;
    private final int maxScroll;

    public SlotPlaceInfo(Slot slot, List<ItemStack> validItems) {
        this.slot = slot;
        this.validItems = new LinkedHashSet<>(validItems);
        this.maxScroll = this.validItems.size() > 0 ? (int) Math.max(0, Math.ceil(this.validItems.size() / 5.0F) - 8) : 0;
    }

    public static SlotPlaceInfo create(Slot slot) {
        return new SlotPlaceInfo(slot, WhatsThatSlot.ITEMS.stream().filter(slot::isItemValid).collect(Collectors.toList()));
    }

    public Slot getSlot() {
        return this.slot;
    }

    public Set<ItemStack> getValidItems() {
        return this.validItems;
    }

    public int getMaxScroll() {
        return this.maxScroll;
    }
}
