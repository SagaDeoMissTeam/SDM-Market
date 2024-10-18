package net.sixik.sdmmarket.common.market.user;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.sixik.sdmmarket.common.utils.INBTSerialize;
import net.sixik.sdmmarket.common.utils.NBTUtils;

import java.util.LinkedList;
import java.util.List;

public class MarketUserEntryList implements INBTSerialize {

    public ItemStack itemStack = ItemStack.EMPTY;
    public LinkedList<MarketUserEntry> entries = new LinkedList<>();

    public MarketUserEntryList() {

    }

    public MarketUserEntryList sort(boolean less) {
        entries.sort((s,j) -> less ? Long.compare(s.price,j.price) : Long.compare(j.price, s.price));
        return this;
    }

    public MarketUserEntryList addElement(MarketUserEntry entry){
        this.entries.add(entry);
        sort(true);
        return this;
    }

    public int getMinPrice() {
        if(entries.isEmpty()) return -1;

        int i = -1;
        for (MarketUserEntry entry : entries) {
            if(i == -1) {
                i = (int) entry.price;
                continue;
            }

            i = (int) Math.min(i, entry.price);
        }

        return i;
    }

    public int getMaxPrice() {
        if(entries.isEmpty()) return -1;

        int i = -1;
        for (MarketUserEntry entry : entries) {
            if(i == -1) {
                i = (int) entry.price;
                continue;
            }

            i = (int) Math.max(i, entry.price);
        }

        return i;
    }

    public int getMinCount() {
        if(entries.isEmpty()) return -1;
        int i = -1;
        for (MarketUserEntry entry : entries) {
            if(i == -1) {
                i = (int) entry.count;
                continue;
            }

            i = (int) Math.min(i, entry.count);
        }

        return i;
    }

    public int getMaxCount() {
        if(entries.isEmpty()) return -1;

        int i = -1;
        for (MarketUserEntry entry : entries) {
            if(i == -1) {
                i = (int) entry.count;
                continue;
            }

            i = (int) Math.max(i, entry.count);
        }

        return i;
    }

    public boolean hasEnchantments() {
        if(itemStack.isEnchantable()) return false;

        for (MarketUserEntry entry : entries) {
            if(entry.itemStack.isEnchanted()) return true;
        }

        return false;
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag nbt = new CompoundTag();
        NBTUtils.putItemStack(nbt, "item", itemStack);
        nbt.put("entries", NBTUtils.serializeList(entries));

        return nbt;
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        this.itemStack = NBTUtils.getItemStack(nbt, "item");
        if(nbt.contains("entries")) {
            ListTag d1 = (ListTag) nbt.get("entries");
            entries.clear();
            for (Tag tag : d1) {
                MarketUserEntry userEntry = new MarketUserEntry();
                userEntry.deserialize((CompoundTag) tag);
                entries.add(userEntry);
            }
        }
    }
}
