package net.sixik.sdmmarket.common.data;

import dev.architectury.platform.Platform;
import dev.ftb.mods.ftblibrary.snbt.SNBT;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.LevelResource;
import net.sixik.sdmmarket.common.market.config.MarketConfigCategory;
import net.sixik.sdmmarket.common.market.config.AbstractMarketConfigEntry;
import net.sixik.sdmmarket.common.utils.INBTSerialize;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.*;

public class MarketConfigData implements INBTSerialize {

    public List<MarketConfigCategory> CATEGORIES = new ArrayList<>();

    @Nullable
    public MarketConfigCategory getCategory(UUID uuid) {
        for (MarketConfigCategory category : CATEGORIES) {
            if(Objects.equals(category.categoryID, uuid)) return category;
        }
        return null;
    }

    @Nullable
    public AbstractMarketConfigEntry getConfigEntry(Item item) {
        for (MarketConfigCategory category : CATEGORIES) {
            for (AbstractMarketConfigEntry entry : category.entries) {
                if(entry.isAvailable(item.getDefaultInstance())) return entry;
            }
        }

        return null;
    }


    public static void save(MinecraftServer server){
        if(MarketDataManager.CONFIG_SERVER == null) return;
        Path f1 = Platform.getConfigFolder().resolve("SDMMarket/configData.sdm");
        SNBT.write(f1,MarketDataManager.CONFIG_SERVER.serialize());
    }

    public static void load(MinecraftServer server) {
        SNBTCompoundTag nbt = SNBT.read(Platform.getConfigFolder().resolve("SDMMarket/configData.sdm"));
        if(nbt == null) return;
        MarketDataManager.CONFIG_SERVER = new MarketConfigData();
        MarketDataManager.CONFIG_SERVER.deserialize(nbt);
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag nbt = new CompoundTag();

        ListTag d1 = new ListTag();
        for (MarketConfigCategory category : CATEGORIES) {
            d1.add(category.serialize());
        }
        nbt.put("categories", d1);
        return nbt;
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        if(nbt.contains("categories")){
            ListTag d1 = (ListTag) nbt.get("categories");
            CATEGORIES.clear();
            for (Tag tag : d1) {
                MarketConfigCategory category = new MarketConfigCategory("");
                category.deserialize((CompoundTag) tag);
                CATEGORIES.add(category);
            }
        }
    }
}
