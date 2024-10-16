package net.sixik.sdmmarket.common.market.config;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.icon.Icon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.sixik.sdmmarket.common.utils.INBTSerialize;

import javax.swing.*;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractMarketConfigEntry implements INBTSerialize {
    public UUID categoryID;
    public UUID entryID;
    public long minPrice = 0;
    public long maxPrice = 0;


    public AbstractMarketConfigEntry(UUID categoryID) {
        this.categoryID = categoryID;
        this.entryID = UUID.randomUUID();
    }

    public static AbstractMarketConfigEntry create(CompoundTag nbt) {
        String id = nbt.getString("typeID");
        if(Objects.equals(id, "durabilityType")) {
            DurabilityMarketConfigEntry marketConfigEntry = new DurabilityMarketConfigEntry(null, ItemStack.EMPTY);
            marketConfigEntry.deserialize(nbt);
            return marketConfigEntry;
        }
        if(Objects.equals(id, "itemType")){
            ItemMarketConfigEntry marketConfigEntry = new ItemMarketConfigEntry(null, ItemStack.EMPTY);
            marketConfigEntry.deserialize(nbt);
            return marketConfigEntry;
        }
        return null;
    }

    public boolean isAvailable(ItemStack itemStack) {
        return false;
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag nbt = new CompoundTag();
        nbt.putUUID("categoryID", categoryID);
        nbt.putUUID("entryID", entryID);
        nbt.putLong("minPrice", minPrice);
        nbt.putLong("maxPrice", maxPrice);
        return nbt;
    }

    public abstract Icon getIcon();

    @Override
    public void deserialize(CompoundTag nbt) {
        this.categoryID = nbt.getUUID("categoryID");
        this.entryID = nbt.getUUID("entryID");
        this.minPrice = nbt.getLong("minPrice");
        this.maxPrice = nbt.getLong("maxPrice");
    }

    public void config(ConfigGroup group) {
        group.addLong("minPrice", minPrice, v -> minPrice = v, 0, 0, Integer.MAX_VALUE);
        group.addLong("maxPrice", maxPrice, v -> maxPrice = v, 0, 0, Integer.MAX_VALUE);
    }
}
