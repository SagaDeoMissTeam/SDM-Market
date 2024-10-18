package net.sixik.sdmmarket.common.market.user;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sixik.sdmmarket.common.utils.INBTSerialize;
import net.sixik.sdmmarket.common.utils.NBTUtils;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class MarketUserCategory implements INBTSerialize {

    public UUID categoryID;
    public ItemStack icon = Items.BARRIER.getDefaultInstance();
    public String categoryName;

    public CopyOnWriteArrayList<MarketUserEntryList> entries = new CopyOnWriteArrayList<>();

    public MarketUserCategory() {
        this(UUID.randomUUID(), "");
    }

    public MarketUserCategory(UUID categoryID, String name) {
        this.categoryID = categoryID;
        this.categoryName = name;
    }

    public MarketUserCategory setIcon(ItemStack icon) {
        this.icon = icon;
        return this;
    }

    public MarketUserCategory addEntry(MarketUserEntryList entry) {
        this.entries.add(entry);
        return this;
    }

    public void removeEntry(ItemStack item, UUID entryID) {
        for (MarketUserEntryList entry : entries) {
            if(ItemStack.matches(entry.itemStack, item)) {
                entry.entries.removeIf(s -> Objects.equals(s.entryID, entryID));
                return;
            }
        }
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag nbt = new CompoundTag();
        nbt.putUUID("categoryID", categoryID);
        NBTUtils.putItemStack(nbt, "icon", icon);
        nbt.putString("categoryName", categoryName);
        nbt.put("entries", NBTUtils.serializeList(entries));
        return nbt;
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        this.categoryID = nbt.getUUID("categoryID");
        this.icon = NBTUtils.getItemStack(nbt, "icon");
        this.categoryName = nbt.getString("categoryName");

        if(nbt.contains("entries")){
            ListTag d1 = (ListTag) nbt.get("entries");
            for (Tag entryTag : d1) {
                MarketUserEntryList entry = new MarketUserEntryList();
                entry.deserialize((CompoundTag) entryTag);
                entries.add(entry);
            }
        }
    }
}
