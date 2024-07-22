package net.sixik.sdmmarket.api;

import net.minecraft.world.item.ItemStack;
import net.sdm.sdmshopr.api.IEntryType;
import net.sdm.sdmshopr.shop.entry.type.ItemEntryType;
import net.sixik.sdmmarket.data.entry.type.ItemMarketType;

import java.util.LinkedHashMap;
import java.util.Map;

public interface MarketEntryTypeRegister {

    Map<String, IMarketEntryType> TYPES = new LinkedHashMap<>();


    static IMarketEntryType register(IMarketEntryType provider) {
        return (IMarketEntryType) TYPES.computeIfAbsent(provider.getTypeID(), (id) -> {
            return provider;
        });
    }

    IMarketEntryType ITEM = register(new ItemMarketType(ItemStack.EMPTY));


    static void init(){

    }
}
