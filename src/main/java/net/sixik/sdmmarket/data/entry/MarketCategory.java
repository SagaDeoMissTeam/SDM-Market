package net.sixik.sdmmarket.data.entry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

public class MarketCategory implements INBTSerializable<CompoundTag> {

    public String id;

    public List<MarketEntry<?>> entries = new ArrayList<>();

    public MarketCategory(String id){
        this.id = id;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("id", id);

        ListTag entiresTagList = new ListTag();
        for (MarketEntry<?> entry : entries) {
            entiresTagList.add(entry.serializeNBT());
        }

        nbt.put("entries", entiresTagList);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.id = nbt.getString("id");
        ListTag entriesTagList = (ListTag) nbt.get("entries");
        entries.clear();
        for (Tag tag : entriesTagList) {
            MarketEntry<?> entry = new MarketEntry<>(this);
            entry.deserializeNBT((CompoundTag) tag);
            entries.add(entry);
        }
    }
}
