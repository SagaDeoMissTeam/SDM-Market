package net.sixik.sdmmarket.config.property;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.sixik.sdmcore.impl.utils.serializer.SDMSerializer;

import java.util.List;

public abstract class AbstractConfigProperty implements INBTSerializable<CompoundTag> {

    public String id;
    public int additionPrice;

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("typeID", id);
        nbt.putInt("price", additionPrice);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.id = nbt.getString("typeID");
        this.additionPrice = nbt.getInt("price");
    }
}
