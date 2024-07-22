package net.sixik.sdmmarket.utils;

import net.minecraft.nbt.CompoundTag;
import net.sixik.sdmmarket.api.IMarketEntryType;
import net.sixik.sdmmarket.api.MarketEntryTypeRegister;
import org.jetbrains.annotations.Nullable;

public class MarketSerializeHelper {


    @Nullable
    public static<T extends IMarketEntryType> T deserializeMarketType(CompoundTag nbt){
        if(nbt.contains("typeID")){
            T entryType = (T) MarketEntryTypeRegister.TYPES.get(nbt.getString("typeID")).copy();
            entryType.deserializeNBT(nbt);
            return entryType;
        }
        return null;
    }
}
