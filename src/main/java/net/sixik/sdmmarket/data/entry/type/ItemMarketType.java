package net.sixik.sdmmarket.data.entry.type;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.ItemStackConfig;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.sdm.sdmshopr.SDMShopR;
import net.sdm.sdmshopr.shop.entry.type.ItemEntryType;
import net.sdm.sdmshopr.utils.NBTUtils;
import net.sixik.sdmmarket.SDMMarket;
import net.sixik.sdmmarket.api.IMarketEntryType;
import net.sixik.sdmmarket.data.ClientData;

import java.util.UUID;

public class ItemMarketType implements IMarketEntryType {

    public ItemStack item;

    public ItemMarketType(ItemStack item){
        this.item = item;
    }

    @Override
    public String getTypeID() {
        return "itemType";
    }

    @Override
    public void executeOnCreate(ServerPlayer ownerPlayer, int count) {
        ItemEntryType.sellItem(ownerPlayer, count, item);
    }

    @Override
    public void executeOnCloseOwnerOffline(UUID ownerPlayer, int price) {
        SDMMarket.offlineData.addPlayer(ownerPlayer, price);
    }

    @Override
    public void executeOnCloseOwnerOnline(ServerPlayer ownerPlayer, int price) {
        SDMShopR.addMoney(ownerPlayer, price);
    }

    @Override
    public void executeOnBuy(ServerPlayer buyerPlayer, int count, int price) {
        long money = SDMShopR.getMoney(buyerPlayer);
        if(money >= price){

            givePlayerItem(buyerPlayer, count);
            SDMShopR.setMoney(buyerPlayer, money - price);
            
        }
    }

    public void givePlayerItem(ServerPlayer player, int count){
        int leftCount = count;

        int ammountleft;
        for(ammountleft = 0; ammountleft < player.getInventory().getContainerSize(); ++ammountleft) {
            if (player.getInventory().getItem(ammountleft) != null && (ItemStack.isSameItem(player.getInventory().getItem(ammountleft), item) || ItemStack.matches(player.getInventory().getItem(ammountleft), item))) {
                ItemStack invItem = player.getInventory().getItem(ammountleft);
                if(invItem.getCount() < invItem.getMaxStackSize()){
                    int c = invItem.getMaxStackSize() - invItem.getCount();

                    if(leftCount >= c) {
                        invItem.setCount(invItem.getCount() + c);
                        leftCount -= c;
                    }
                    else {
                        invItem.setCount(invItem.getCount() + leftCount);
                        leftCount -= leftCount;
                    }
                }
            }
        }


        while (leftCount > 0) {

            boolean isLeft = leftCount > item.getMaxStackSize();

            int slot = player.getInventory().getFreeSlot();
            if(slot == -1) {

                if(isLeft) {
                    ItemStack d1 = item.copy();
                    d1.setCount(d1.getMaxStackSize());
                    player.drop(d1, false);
                    leftCount -= d1.getMaxStackSize();
                } else {
                    ItemStack d1 = item.copy();
                    d1.setCount(leftCount);
                    player.drop(d1, false);
                    leftCount -= leftCount;
                }

            } else {

                if(isLeft) {
                    ItemStack d1 = item.copy();
                    d1.setCount(d1.getMaxStackSize());
                    player.addItem(d1);
                    leftCount -= d1.getMaxStackSize();
                } else {
                    ItemStack d1 = item.copy();
                    d1.setCount(leftCount);
                    player.addItem(d1);
                    leftCount -= leftCount;
                }

            }

        }
    }

    @Override
    public void getConfig(ConfigGroup group) {
        group.add("item", new ItemStackConfig(true,false), item, v -> item = v, ItemStack.EMPTY);
    }

    @Override
    public Icon getIcon(int count) {
        ItemStack itemStack = item.copy();
        itemStack.setCount(count);
        return ItemIcon.getItemIcon(itemStack);
    }

    @Override
    public IMarketEntryType copy() {
        return new ItemMarketType(item);
    }

    @Override
    public int getCountOnPlayer() {
        Player player = Minecraft.getInstance().player;

        int amounts = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if(ItemStack.matches(item, this.item.copy()) || ItemStack.isSameItem(item, this.item.copy())){
                amounts += item.getCount();
            }
        }

        return amounts;
    }

    @Override
    public void addTooltips(TooltipList tooltipList) {
        tooltipList.add(item.getDisplayName());
    }

    @Override
    public boolean search() {
        if(ClientData.searchField.isEmpty()) return true;

        String str = item.getDisplayName().getString().replace('[', ' ').replace(']', ' ').toLowerCase();
        if(ForgeRegistries.ITEMS.getKey(item.getItem()).toString().toLowerCase().contains(ClientData.searchField.toLowerCase())){
            return true;
        } else if(str != null && str.contains(ClientData.searchField.toLowerCase())){
            return true;
        }
        return false;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = IMarketEntryType.super.serializeNBT();
        NBTUtils.putItemStack(nbt, "item", item);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.item = NBTUtils.getItemStack(nbt, "item");
    }

    @Override
    public String toString() {
        return "ItemMarketType{" +
                "item=" + item +
                '}';
    }
}
