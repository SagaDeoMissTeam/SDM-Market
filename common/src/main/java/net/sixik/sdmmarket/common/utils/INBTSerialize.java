package net.sixik.sdmmarket.common.utils;

import net.minecraft.nbt.CompoundTag;

public interface INBTSerialize {
    CompoundTag serialize();
    void deserialize(CompoundTag nbt);
}
