package net.sixik.sdmmarket.common.serializer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.sixik.sdmmarket.common.market.user.MarketUserCategory;
import net.sixik.sdmmarket.common.market.user.MarketUserEntry;
import net.sixik.sdmmarket.common.market.user.MarketUserEntryList;
import net.sixik.sdmmarket.common.utils.INBTSerialize;
import net.sixik.sdmmarket.common.utils.NBTUtils;

import java.util.ArrayList;
import java.util.List;

public class MarketSerializer {

    public static class MarketConfig {

    }

    public static class MarketEntry {

        public static CompoundTag serializeMarketUserList(MarketUserEntryList entryList, int bits) {
            CompoundTag nbt = new CompoundTag();

            if((bits & SDMSerializeParam.SERIALIZE_STAGE_1) != 0) {
                NBTUtils.putItemStack(nbt, "item", entryList.itemStack);

                if(entryList.tagID != null)
                    nbt.putString("tagID", entryList.tagID.toString());
            }

            if((bits & SDMSerializeParam.SERIALIZE_STAGE_2) != 0) {
                nbt.put("entries", NBTUtils.serializeList(entryList.entries));
            }

            return nbt;
        }

        public static void deserializeMarketUserList(MarketUserEntryList entryList, CompoundTag nbt, int bits) {
            if((bits & SDMSerializeParam.SERIALIZE_STAGE_1) != 0) {
                entryList.itemStack = NBTUtils.getItemStack(nbt, "item");

                if(nbt.contains("tagID")) entryList.tagID = new ResourceLocation(nbt.getString("tagID"));
            }

            if((bits & SDMSerializeParam.SERIALIZE_STAGE_2) != 0) {
                if(nbt.contains("entries")) {
                    ListTag d1 = (ListTag) nbt.get("entries");
                    entryList.entries.clear();
                    for (Tag tag : d1) {
                        MarketUserEntry userEntry = new MarketUserEntry();
                        userEntry.deserialize((CompoundTag) tag);
                        entryList.entries.add(userEntry);
                    }
                }
            }
        }

        public static CompoundTag serializeCategory(MarketUserCategory category, int bits) {
            CompoundTag nbt = new CompoundTag();

            if((bits & SDMSerializeParam.SERIALIZE_STAGE_1) != 0) {
                nbt.putUUID("categoryID", category.categoryID);
                NBTUtils.putItemStack(nbt, "icon", category.icon);
                nbt.putString("categoryName", category.categoryName);
            }

            if((bits & SDMSerializeParam.SERIALIZE_STAGE_2) != 0) {
                nbt.put("entries", NBTUtils.serializeList(category.entries));
            }

            return nbt;
        }

        public static void deserializeCategory(MarketUserCategory category, CompoundTag nbt, int bits) {
            if((bits & SDMSerializeParam.SERIALIZE_STAGE_1) != 0) {
                category.categoryID = nbt.getUUID("categoryID");
                category.icon = NBTUtils.getItemStack(nbt, "icon");
                category.categoryName = nbt.getString("categoryName");
            }

            if((bits & SDMSerializeParam.SERIALIZE_STAGE_2) != 0) {

                if(nbt.contains("entries")) {
                    category.entries.clear();
                    ListTag d1 = (ListTag) nbt.get("entries");
                    for (Tag entryTag : d1) {
                        MarketUserEntryList entry = new MarketUserEntryList();
                        entry.deserialize((CompoundTag) entryTag);
                        category.entries.add(entry);
                    }
                }
            }
        }

        public static CompoundTag serializeCategoryWithoutEntries(MarketUserCategory category) {
            CompoundTag nbt = new CompoundTag();


            nbt.putUUID("categoryID", category.categoryID);
            NBTUtils.putItemStack(nbt, "icon", category.icon);
            nbt.putString("categoryName", category.categoryName);


            ListTag listTag = new ListTag();
            for(MarketUserEntryList serialize : category.entries){
                listTag.add(serialize.serialize(SDMSerializeParam.SERIALIZE_STAGE_1));
            }
            nbt.put("entries", listTag);

            return nbt;
        }

        public static void deserializeCategoryWithoutEntries(MarketUserCategory category, CompoundTag nbt) {
            category.categoryID = nbt.getUUID("categoryID");
            category.icon = NBTUtils.getItemStack(nbt, "icon");
            category.categoryName = nbt.getString("categoryName");


            if(nbt.contains("entries")) {
                category.entries.clear();
                ListTag d1 = (ListTag) nbt.get("entries");
                for (Tag entryTag : d1) {
                    MarketUserEntryList entry = new MarketUserEntryList();
                    entry.deserialize((CompoundTag) entryTag, SDMSerializeParam.SERIALIZE_STAGE_1);
                    category.entries.add(entry);
                }
            }

        }


        public static List<Tag> serializeCategoryEntryListToList(MarketUserEntryList category) {
            List<Tag> tags = new ArrayList<>();

            for (MarketUserEntry entry : category.entries) {
                tags.add(entry.serialize());
            }

            return tags;
        }

        public static List<MarketUserEntry> deserializeCategoryEntryListToList(List<Tag> tags) {
            List<MarketUserEntry> list = new ArrayList<>();

            for (Tag tag : tags) {
                MarketUserEntry userEntry = new MarketUserEntry();
                userEntry.deserialize((CompoundTag) tag);
                list.add(userEntry);
            }

            return list;
        }

    }
}
