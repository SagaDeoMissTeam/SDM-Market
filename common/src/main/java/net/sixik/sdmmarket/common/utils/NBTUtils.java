package net.sixik.sdmmarket.common.utils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class NBTUtils {

    public static ListTag serializeList(List<? extends INBTSerialize> serializes){
        ListTag listTag = new ListTag();
        for(INBTSerialize serialize : serializes){
            listTag.add(serialize.serialize());
        }
        return listTag;
    }

    public static void putItem(CompoundTag nbt, String key, Item item){
        nbt.putString(key, BuiltInRegistries.ITEM.getKey(item).toString());
    }

    public static Item getItem(CompoundTag nbt, String key){
        if(nbt.get(key) instanceof StringTag stringTag) {
            return BuiltInRegistries.ITEM.get(new ResourceLocation(stringTag.getAsString()));
        }
        return Items.AIR;
    }

    public static void putItemStack(CompoundTag nbt, String key, ItemStack itemStack) {
        nbt.put(key, itemStack.save(new CompoundTag()));
    }

    public static ItemStack getItemStack(CompoundTag nbt, String key){
        if(nbt.get(key) instanceof StringTag stringTag) {
            Item d1 = BuiltInRegistries.ITEM.get(new ResourceLocation(stringTag.getAsString()));
            if(d1 == null) return ItemStack.EMPTY;
            return d1.getDefaultInstance();
        }

        return ItemStack.of(nbt.getCompound(key));
    }
}
