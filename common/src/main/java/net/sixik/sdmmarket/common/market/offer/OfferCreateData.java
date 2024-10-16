package net.sixik.sdmmarket.common.market.offer;

import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class OfferCreateData {

    public UUID ownerID;
    public ItemStack item;
    public long price;
    public int count;
    public OfferCreateData(UUID ownerID, ItemStack item, long price, int count) {
        this.ownerID = ownerID;
        this.item = item;
        this.price = price;
        this.count = count;
    }
}
