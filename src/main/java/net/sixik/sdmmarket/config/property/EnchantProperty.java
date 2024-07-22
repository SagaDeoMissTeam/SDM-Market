package net.sixik.sdmmarket.config.property;

import net.minecraft.nbt.CompoundTag;

public class EnchantProperty extends AbstractConfigProperty{

    public String enchantID;
    public int enchantLevel = -1;

    public EnchantProperty(String enchantID){
        this.id = "enchant";
        this.enchantID = enchantID;
    }

    public EnchantProperty(String enchantID, int enchantLevel){
        this.id = "enchant";
        this.enchantID = enchantID;
        this.enchantLevel = enchantLevel;
    }


    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = super.serializeNBT();
        nbt.putString("enchantID", enchantID);
        if(enchantLevel != -1)
            nbt.putInt("enchantLevel", enchantLevel);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.enchantID = nbt.getString("enchantID");

        if(nbt.contains("enchantLevel")){
            this.enchantLevel = nbt.getInt("enchantLevel");
        }
    }
}
