package net.sixik.sdmmarket.common.data;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import net.minecraft.nbt.CompoundTag;
import net.sixik.sdmmarket.common.utils.INBTSerialize;

public class MarketConfig implements INBTSerialize {



    public boolean sellAnyItems = false;
    public int maxOffersForPlayer = 10;
    public boolean disableKeyBinding = false;

    public MarketConfig() {}

    public void getConfig(ConfigGroup group){
        group.addBool("sellAnyItems", sellAnyItems, v -> sellAnyItems = v, false);
        group.addInt("maxOffersForPlayer", maxOffersForPlayer, v -> maxOffersForPlayer = v, 10, 1, Integer.MAX_VALUE);
        group.addBool("disableKeyBinding", disableKeyBinding, v -> disableKeyBinding = v, false);
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("sellAnyItems", sellAnyItems);
        nbt.putBoolean("disableKeyBinding", disableKeyBinding);
        nbt.putInt("maxOffersForPlayer", maxOffersForPlayer);
        return nbt;
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        this.sellAnyItems = nbt.getBoolean("sellAnyItems");
        if(nbt.contains("disableKeyBinding")) {
            this.disableKeyBinding = nbt.getBoolean("disableKeyBinding");
        }
        this.maxOffersForPlayer = nbt.getInt("maxOffersForPlayer");
    }
}
