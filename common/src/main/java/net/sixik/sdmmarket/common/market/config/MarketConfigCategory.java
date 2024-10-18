package net.sixik.sdmmarket.common.market.config;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sixik.sdmmarket.common.ftb.ConfigIconItemStack;
import net.sixik.sdmmarket.common.utils.INBTSerialize;
import net.sixik.sdmmarket.common.utils.NBTUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MarketConfigCategory implements INBTSerialize {

    public UUID categoryID;
    public ItemStack icon = Items.BARRIER.getDefaultInstance();
    public String categoryName;

    public List<AbstractMarketConfigEntry> entries = new ArrayList<>();

    public MarketConfigCategory(String categoryName) {
        this.categoryName = categoryName;
        this.categoryID = UUID.randomUUID();
    }

    public MarketConfigCategory(UUID uuid, String categoryName) {
        this.categoryName = categoryName;
        this.categoryID = uuid;
    }

    @Nullable
    public AbstractMarketConfigEntry getEntry(UUID entryID){
        for (AbstractMarketConfigEntry entry : entries) {
            if(Objects.equals(entry.entryID, entryID)) return entry;
        }
        return null;
    }

    public MarketConfigCategory setIcon(ItemStack icon) {
        this.icon = icon;
        return this;
    }

    public void config(ConfigGroup group) {
        group.add("icon", new ConfigIconItemStack(), icon, v -> icon =v, Items.BARRIER.getDefaultInstance());
        group.addString("categoryName", categoryName, v -> categoryName = v, "Empty Name");
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag nbt = new CompoundTag();

        nbt.putUUID("categoryID", categoryID);
        NBTUtils.putItemStack(nbt, "icon", icon);
        nbt.putString("categoryName", categoryName);

        ListTag d1 = new ListTag();
        for (AbstractMarketConfigEntry entry : entries) {
            d1.add(entry.serialize());
        }
        nbt.put("entries", d1);

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
                AbstractMarketConfigEntry entry = AbstractMarketConfigEntry.create((CompoundTag) entryTag);
                if(entry!= null) entries.add(entry);
            }
        }
    }
}
