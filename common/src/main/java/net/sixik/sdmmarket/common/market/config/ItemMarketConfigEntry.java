package net.sixik.sdmmarket.common.market.config;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sixik.sdmmarket.common.register.CustomIconItem;
import net.sixik.sdmmarket.common.register.ItemsRegister;
import net.sixik.sdmmarket.common.utils.MarketItemHelper;
import net.sixik.sdmmarket.common.utils.NBTUtils;

import java.util.UUID;

public class ItemMarketConfigEntry extends AbstractMarketConfigEntry{
    public ItemStack itemStack;
    public ItemMarketConfigEntry(UUID categoryID, ItemStack item) {
        super(categoryID);
        this.itemStack = item;
    }

    @Override
    public Icon getIcon() {
        if(itemStack.is(ItemsRegister.CUSTOM_ICON.get())){
            return CustomIconItem.getIcon(itemStack);
        }
        return ItemIcon.getItemIcon(itemStack);
    }

    @Override
    public boolean isAvailable(ItemStack itemStack1) {
        return MarketItemHelper.isEquals(itemStack1.copy(), this.itemStack.copy());
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag nbt = super.serialize();
        NBTUtils.putItemStack(nbt, "item", itemStack);
        nbt.putString("typeID", "itemType");
        return nbt;
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        super.deserialize(nbt);
        this.itemStack = NBTUtils.getItemStack(nbt, "item");
    }

    @Override
    public void config(ConfigGroup group) {
        group.addItemStack("item", itemStack, v -> itemStack = v, ItemStack.EMPTY, 1);
        super.config(group);
    }
}
