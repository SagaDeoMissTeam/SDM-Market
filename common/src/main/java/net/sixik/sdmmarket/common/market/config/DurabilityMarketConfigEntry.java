package net.sixik.sdmmarket.common.market.config;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.sixik.sdmmarket.common.register.CustomIconItem;
import net.sixik.sdmmarket.common.register.ItemsRegister;
import net.sixik.sdmmarket.common.utils.MarketItemHelper;
import net.sixik.sdmmarket.common.utils.NBTUtils;

import java.util.UUID;

public class DurabilityMarketConfigEntry extends AbstractMarketConfigEntry{

    public ItemStack itemStack;
    public boolean enchantable = false;
    public boolean damagebled = false;

    public DurabilityMarketConfigEntry(UUID categoryID, ItemStack itemStack) {
        super(categoryID);
        this.itemStack = itemStack;
    }

    public DurabilityMarketConfigEntry setDamagebled(boolean damagebled) {
        this.damagebled = damagebled;
        return this;
    }

    public DurabilityMarketConfigEntry setEnchantable(boolean enchantable) {
        this.enchantable = enchantable;
        return this;
    }

    @Override
    public boolean isAvailable(ItemStack itemStack1) {
        ItemStack d1 = itemStack1.copy();
        d1.setCount(1);

        if(!damagebled && d1.getDamageValue() > 0) {
            return false;
        }

        if(!enchantable && d1.isEnchanted()) {
            return false;
        }

        return MarketItemHelper.isEquals(d1, this.itemStack);
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag nbt = super.serialize();
        NBTUtils.putItemStack(nbt, "item", itemStack);
        nbt.putString("typeID", "durabilityType");
        nbt.putBoolean("enchantable", enchantable);
        nbt.putBoolean("damagebled", damagebled);
        return nbt;
    }

    @Override
    public Icon getIcon() {
        if(itemStack.is(ItemsRegister.CUSTOM_ICON.get())){
            return CustomIconItem.getIcon(itemStack);
        }
        return ItemIcon.getItemIcon(itemStack);
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        super.deserialize(nbt);
        this.itemStack = NBTUtils.getItemStack(nbt, "item");
        this.enchantable = nbt.getBoolean("enchantable");
        this.damagebled = nbt.getBoolean("damagebled");
    }

    @Override
    public void config(ConfigGroup group) {
        group.addItemStack("item", itemStack, v -> itemStack = v, ItemStack.EMPTY, true,false);
        group.addBool("enchantable", enchantable, v -> enchantable = v, false);
        group.addBool("damagebled", damagebled, v -> damagebled = v, false);
        super.config(group);
    }
}
