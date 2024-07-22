package net.sixik.sdmmarket.config.property;

import net.minecraft.nbt.CompoundTag;

public class ItemProperty extends AbstractConfigProperty{

    public String itemID;

    public ItemProperty(String itemID){
        this.id = "item";
        this.itemID = itemID;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = super.serializeNBT();
        nbt.putString("itemID", itemID);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.itemID = nbt.getString("itemID");
    }
}
