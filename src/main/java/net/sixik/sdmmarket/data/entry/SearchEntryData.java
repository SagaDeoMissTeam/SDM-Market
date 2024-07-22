package net.sixik.sdmmarket.data.entry;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.StringConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

public class SearchEntryData implements INBTSerializable<CompoundTag> {

    public List<String> tags = new ArrayList<>();

    public SearchEntryData(){

    }

    public void getConfig(ConfigGroup group){
        group.addList("tags", tags, new StringConfig(null), "");
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();

        ListTag tag = new ListTag();
        for (String s : tags) {
            tag.add(StringTag.valueOf(s));
        }

        nbt.put("tags", tag);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {

        ListTag tag = (ListTag) compoundTag.get("tags");

        tags.clear();
        for (Tag tag1 : tag) {
            tags.add(tag1.getAsString());
        }

    }
}
