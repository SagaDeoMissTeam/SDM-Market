package net.sixik.sdmmarket.common.data;

import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sixik.sdmmarket.common.market.config.AbstractMarketConfigEntry;
import net.sixik.sdmmarket.common.market.config.MarketConfigCategory;
import net.sixik.sdmmarket.common.market.offer.OfferCreateData;
import net.sixik.sdmmarket.common.market.user.MarketUserCategory;
import net.sixik.sdmmarket.common.market.user.MarketUserEntry;
import net.sixik.sdmmarket.common.market.user.MarketUserEntryList;
import net.sixik.sdmmarket.common.network.user.SyncUserDataS2C;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MarketUserManager {

    @Nullable
    public static MarketUserCategory getCategoryByID(UUID categoryID) {
        for (MarketUserCategory category : MarketDataManager.USER_SERVER.categories) {
            if(Objects.equals(category.categoryID, categoryID)) return category;
        }

        return null;
    }

    @Nullable
    public static MarketUserEntry getEntryByID(UUID entryID) {
        for (MarketUserCategory category : MarketDataManager.USER_SERVER.categories) {
            for (MarketUserEntryList entryList : category.entries) {
                for (MarketUserEntry entry : entryList.entries) {
                    if(Objects.equals(entry.entryID, entryID)) return entry;
                }
            }
        }

        return null;
    }

    @Nullable
    public static MarketUserEntryList getEntryListByID(UUID categoryID, ItemStack itemStack) {
        for (MarketUserCategory category : MarketDataManager.USER_SERVER.categories) {
            if(Objects.equals(category.categoryID, categoryID)) {
                for (MarketUserEntryList entry : category.entries) {
                    if(entry.itemStack.hasTag() && ItemStack.isSameItemSameTags(itemStack, entry.itemStack)) {
                        return entry;
                    } else if(!entry.itemStack.hasTag() && ItemStack.isSameItem(itemStack, entry.itemStack)){
                        return entry;
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    public static MarketUserEntryList getEntryListByCategory(MarketUserCategory category, ItemStack itemStack) {
        for (MarketUserEntryList entry : category.entries) {
            if(entry.itemStack.hasTag() && ItemStack.isSameItemSameTags(itemStack, entry.itemStack)) {
                return entry;
            } else if(!entry.itemStack.hasTag() && (ItemStack.isSameItem(itemStack, entry.itemStack) || ItemStack.matches(itemStack, entry.itemStack))){
                return entry;
            }
        }
        return null;
    }

    public static boolean syncUserData(ServerPlayer player) {
        for (MarketPlayerData.PlayerData data : MarketDataManager.PLAYERS_SERVER_DATA.PLAYERS) {
            if(data.playerID.equals(player.getGameProfile().getId())) {
                new SyncUserDataS2C(data.serialize()).sendTo(player);
                return true;
            }
        }

        return false;
    }

    public static boolean canCreateOfferWithItem(ItemStack itemStack, MarketConfigData configData){
        for (MarketConfigCategory category : configData.CATEGORIES) {
            for (AbstractMarketConfigEntry entry : category.entries) {
                if(entry.isAvailable(itemStack)) return true;
            }
        }

        return false;
    }

    public static MarketConfigCategory getCategoryForItem(ItemStack itemStack, MarketConfigData configData){
        for (MarketConfigCategory category : configData.CATEGORIES) {
            for (AbstractMarketConfigEntry entry : category.entries) {
                if(entry.isAvailable(itemStack)) return category;
            }
        }

        return null;
    }

    public static AbstractMarketConfigEntry getEntryForItem(ItemStack itemStack, MarketConfigData configData, MarketConfigCategory category) {
        for (AbstractMarketConfigEntry entry : category.entries) {
            if(entry.isAvailable(itemStack)) return entry;
        }

        return null;
    }

    public static AbstractMarketConfigEntry getEntryForItem(ItemStack itemStack, MarketConfigData configData) {
        for (MarketConfigCategory category : configData.CATEGORIES) {
            for (AbstractMarketConfigEntry entry : category.entries) {
                if(entry.isAvailable(itemStack)) return entry;
            }
        }

        return null;
    }

    public static void createOfferWithItem(OfferCreateData offerData, MarketConfigData configData, MarketUserData userData) {
        MarketConfigCategory configCategory = getCategoryForItem(offerData.item, configData);
        if(configCategory == null) return;

        MarketUserCategory marketUserCategory = null;

        for (MarketUserCategory category : userData.categories) {
            if(Objects.equals(category.categoryID, configCategory.categoryID)) {
                marketUserCategory = category;
                break;
            }
        }

        if(marketUserCategory == null) return;

        MarketUserEntryList entryList = null;

        for (MarketUserEntryList entry : marketUserCategory.entries) {
            if(ItemStack.isSameItem(offerData.item, entry.itemStack)) {
                entryList = entry;
                break;
            }
        }

        if(entryList == null) return;

        MarketUserEntry userEntry = MarketUserEntry.fromOfferData(marketUserCategory.categoryID, offerData);
        entryList.addElement(userEntry);

    }

    public static void createOffersCategories(MarketConfigData data, MarketUserData toUserData) {
        MarketUserData userData = new MarketUserData();

        for (MarketConfigCategory category : data.CATEGORIES) {

            MarketUserCategory userCategory = new MarketUserCategory();
            userCategory.categoryID = category.categoryID;
            userCategory.categoryName = category.categoryName;
            userCategory.icon = category.icon;


            for (AbstractMarketConfigEntry entry : category.entries) {
                MarketUserEntryList userEntryList = new MarketUserEntryList();

                if(entry.getIcon() instanceof ItemIcon icon) {
                    userEntryList.itemStack = icon.getStack();
                }

                userCategory.entries.add(userEntryList);
            }

            userData.categories.add(userCategory);
        }

        toUserData.copyFrom(userData);
    }

    public static List<ItemStack> getAvaliableItems(Player player, MarketConfigData configData) {
        List<ItemStack> items = new ArrayList<>();
        Inventory inventory = player.getInventory();

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if(itemStack.isEmpty()) continue;

            if(canCreateOfferWithItem(itemStack, configData)) {

                if (!isContains(items, itemStack)) {
                    items.add(itemStack);
                }

            }
        }

        return items;
    }

    public static boolean isContains(List<ItemStack> list, ItemStack itemStack){
        ItemStack f1 = itemStack.copy();
        f1.setCount(1);
        for (ItemStack stack : list) {
            ItemStack d = stack.copy();
            d.setCount(1);
            if(ItemStack.matches(d, f1)) {
                return true;
            }

            if(f1.hasTag() && ItemStack.isSameItemSameTags(f1, d)) {
                return true;
            }

            if(!f1.hasTag() && !d.hasTag() && ItemStack.isSameItem(f1, d)){
                return true;
            }
        }
        return false;
    }
}