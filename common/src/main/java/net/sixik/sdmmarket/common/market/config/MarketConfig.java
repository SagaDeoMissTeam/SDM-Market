package net.sixik.sdmmarket.common.market.config;

import net.minecraft.nbt.CompoundTag;
import net.sixik.sdmmarket.common.utils.INBTSerialize;

public class MarketConfig implements INBTSerialize {



    public boolean sellAnyItems = false;
    public int maxOffersForPlayer = 10;

    public MarketConfig() {}

    @Override
    public CompoundTag serialize() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("sellAnyItems", sellAnyItems);
        nbt.putInt("maxOffersForPlayer", maxOffersForPlayer);
        return nbt;
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        this.sellAnyItems = nbt.getBoolean("sellAnyItems");
        this.maxOffersForPlayer = nbt.getInt("maxOffersForPlayer");
    }
}
