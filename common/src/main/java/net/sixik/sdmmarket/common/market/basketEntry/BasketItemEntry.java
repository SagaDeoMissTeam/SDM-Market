package net.sixik.sdmmarket.common.market.basketEntry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sixik.sdmmarket.common.utils.NBTUtils;
import net.sixik.sdmmarket.common.utils.item.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;

public class BasketItemEntry extends AbstractBasketEntry{

    public ItemStack itemStack;
    public int count;

    public BasketItemEntry(ItemStack itemStack, int count) {
        super();
        this.itemStack = itemStack;
        this.count = count;
    }

    @Override
    public void givePlayer(Player player) {
        List<ItemStack> d1 = new ArrayList<>();

        int i = count / itemStack.getMaxStackSize();
        int j = count % itemStack.getMaxStackSize();

        ItemStack f = null;

        for (int i1 = 0; i1 < i; i1++) {
            f = itemStack.copy();
            f.setCount(itemStack.getMaxStackSize());
            d1.add(f);
        }

        if(j > 0) {
            f = itemStack.copy();
            f.setCount(j);
            d1.add(f);
        }

        for (ItemStack stack : d1) {
            ItemHandlerHelper.giveItemToPlayer(player, stack);
        }
    }

    @Override
    public String getID() {
        return "basketItemEntry";
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag nbt = super.serialize();
        NBTUtils.putItemStack(nbt,"itemStack", itemStack);
        nbt.putInt("count",count);
        return nbt;
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        super.deserialize(nbt);
        this.itemStack = NBTUtils.getItemStack(nbt, "itemStack");
        this.count = nbt.getInt("count");
    }
}
