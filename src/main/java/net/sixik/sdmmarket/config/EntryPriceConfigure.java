package net.sixik.sdmmarket.config;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;
import net.sixik.sdmmarket.config.property.AbstractConfigProperty;
import net.sixik.sdmmarket.utils.MarketFactory;

import java.util.ArrayList;
import java.util.List;

public class EntryPriceConfigure {



    public static class ConfigureSellable implements INBTSerializable<CompoundTag>{

        public String type;

        public ConfigureSellable(String type){
            this.type = type;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("typeID", type);
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundTag compoundTag) {
            this.type = compoundTag.getString("typeID");
        }
    }

    public static class ConfigureEntry implements INBTSerializable<CompoundTag> {
        public String type;
        public int minPrice = 0;
        public int maxPrice = 0;
        public List<AbstractConfigProperty> properties = new ArrayList<>();

        public ConfigureEntry(String type, int minPrice, int maxPrice){
            this.type = type;
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
        }

        public ConfigureEntry(String type){
            this.type = type;
        }

        public ConfigureEntry addProperties(AbstractConfigProperty... properties){
            this.properties.addAll(new ArrayList<>(List.of(properties)));
            return this;
        }


        @Override
        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("typeID", type);
            if(minPrice >= 1)
                nbt.putInt("minPrice", minPrice);
            if(maxPrice >= 1)
                nbt.putInt("maxPrice", maxPrice);

            ListTag listTag = new ListTag();
            for (AbstractConfigProperty property : properties) {
                listTag.add(property.serializeNBT());
            }
            if(!listTag.isEmpty())
                nbt.put("properties", listTag);

            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.type = nbt.getString("typeID");
            if(nbt.contains("minPrice"))
                this.minPrice = nbt.getInt("minPrice");
            if(nbt.contains("maxPrice"))
                this.minPrice = nbt.getInt("maxPrice");

            if(nbt.contains("properties")){
                ListTag listTag = (ListTag) nbt.get("properties");
                properties.clear();
                for (Tag tag : listTag) {
                    properties.add(MarketFactory.deserializeConfig((CompoundTag) tag));
                }
            }

        }
    }
}
