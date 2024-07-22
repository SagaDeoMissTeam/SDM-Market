package net.sixik.sdmmarket.utils;

import net.minecraft.nbt.CompoundTag;
import net.sixik.sdmmarket.config.property.AbstractConfigProperty;
import net.sixik.sdmmarket.config.property.EnchantProperty;

public class MarketFactory {


    //TODO:
    public static AbstractConfigProperty deserializeConfig(CompoundTag nbt){


        return new EnchantProperty("");
    }
}
