package net.sixik.sdmmarket.common.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.sixik.sdmmarket.common.market.user.MarketUserCategory;
import net.sixik.sdmmarket.common.utils.INBTSerialize;
import net.sixik.sdmmarket.common.utils.NBTUtils;

import java.util.ArrayList;
import java.util.List;

public class MarketUserData implements INBTSerialize {


    public List<MarketUserCategory> categories = new ArrayList<>();


    public void copyFromOverwrite(MarketUserData other) {
        this.categories.clear();
        this.categories.addAll(other.categories);
    }

    public void copyFrom(MarketUserData other) {
        if(this.categories.isEmpty()) {
            this.categories.addAll(other.categories);
        }

        var iterator = this.categories.iterator();

        while (iterator.hasNext()) {
            MarketUserCategory category = iterator.next();

            boolean find = false;
            for (MarketUserCategory marketUserCategory : other.categories) {
                if(category.categoryID.equals(marketUserCategory.categoryID)) {
                    find = true;
                    break;
                }
            }

            if(!find && category.entries.isEmpty()) {
                iterator.remove();
            }
        }

        iterator = other.categories.iterator();

        while (iterator.hasNext()){
            MarketUserCategory category = iterator.next();

            boolean find = false;
            for (MarketUserCategory marketUserCategory : this.categories) {
                if(category.categoryID.equals(marketUserCategory.categoryID)) {
                    find = true;
                    break;
                }
            }

            if(!find) {
                this.categories.add(category);
            }
        }
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag nbt = new CompoundTag();
        ListTag list = NBTUtils.serializeList(categories);
        nbt.put("categories", list);
        return nbt;
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        if (nbt.contains("categories")) {
            ListTag d1 = (ListTag) nbt.get("categories");
            categories.clear();
            for (Tag entryTag : d1) {
                MarketUserCategory category = new MarketUserCategory();
                category.deserialize((CompoundTag) entryTag);
                categories.add(category);
            }
        }
    }
}
