package net.sixik.sdmmarket.data;

import dev.ftb.mods.ftblibrary.snbt.SNBT;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.sdm.sdmshopr.SDMShopR;
import net.sdm.sdmshopr.shop.entry.type.ItemEntryType;
import net.sdm.sdmshopr.shop.entry.type.XPEntryType;
import net.sixik.sdmmarket.data.entry.MarketCategory;
import net.sixik.sdmmarket.data.entry.MarketEntry;
import net.sixik.sdmmarket.data.entry.type.ItemMarketType;
import net.sixik.sdmmarket.network.SyncMarketDataS2C;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.TreeMap;
import java.util.UUID;

public class MarketData implements INBTSerializable<CompoundTag> {
    public static MarketData SERVER;
    public static MarketData CLIENT;

    public static Path getFile(MinecraftServer server){
       return server.getWorldPath(LevelResource.PLAYER_DATA_DIR).resolve("SDMMarketData.snbt");
    }

    public TreeMap<String, MarketCategory> categories = new TreeMap<>();

    public MarketData(){

    }

    public MarketCategory addEntry(String name, MarketEntry<?> entry){
        MarketCategory category = getOrCreate(name);
        entry.parentCategory = category;
        category.entries.add(entry);
        return category;
    }

    public MarketCategory getOrCreate(String name){
        if(!categories.containsKey(name)) {
            categories.put(name, new MarketCategory(name));
        }

        return categories.get(name);
    }

    @Nullable
    public MarketEntry<?> getEntry(UUID entryID){
        for (MarketCategory value : categories.values()) {
            for (MarketEntry<?> entry : value.entries) {
                if(entry.uuid.equals(entryID)) return entry;
            }
        }

        return null;
    }

    public static class Methods{

        public static void sync(MinecraftServer server){
            new SyncMarketDataS2C(MarketData.SERVER.serializeNBT()).sendToAll(server);
        }
        public static void sync(ServerPlayer player){
            new SyncMarketDataS2C(MarketData.SERVER.serializeNBT()).sendTo(player);
        }
    }

    @Nullable
    public MarketCategory getCategory(String name){
        return categories.get(name);
    }

    public MarketCategory getOrCreateCategory(String name){
        MarketCategory marketCategory =  getCategory(name);
        if(marketCategory == null){
            categories.put(name, new MarketCategory(name));
            marketCategory = categories.get(name);
        }

        return marketCategory;
    }

    public void save(){
        SNBT.write(getFile(ServerLifecycleHooks.getCurrentServer()), serializeNBT());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();

        ListTag listTag = new ListTag();

        for (MarketCategory value : categories.values()) {
            listTag.add(value.serializeNBT());
        }

        nbt.put("categories", listTag);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        ListTag listTag = (ListTag) tag.get("categories");
        categories.clear();
        for (Tag tag1 : listTag) {
            MarketCategory marketCategory = new MarketCategory("null");
            marketCategory.deserializeNBT((CompoundTag) tag1);
            categories.put(marketCategory.id, marketCategory);
        }
    }
}
