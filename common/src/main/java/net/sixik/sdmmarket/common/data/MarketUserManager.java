package net.sixik.sdmmarket.common.data;

import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sixik.sdmmarket.common.market.config.AbstractMarketConfigEntry;
import net.sixik.sdmmarket.common.market.config.ItemMarketConfigEntry;
import net.sixik.sdmmarket.common.market.config.ItemTagMarketConfigEntry;
import net.sixik.sdmmarket.common.market.config.MarketConfigCategory;
import net.sixik.sdmmarket.common.market.offer.OfferCreateData;
import net.sixik.sdmmarket.common.market.user.MarketUserAnyCategory;
import net.sixik.sdmmarket.common.market.user.MarketUserCategory;
import net.sixik.sdmmarket.common.market.user.MarketUserEntry;
import net.sixik.sdmmarket.common.market.user.MarketUserEntryList;
import net.sixik.sdmmarket.common.network.user.SyncUserDataS2C;
import net.sixik.sdmmarket.common.utils.MarketItemHelper;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
        ItemStack j = itemStack.copy();
        j.setDamageValue(0);

        for (MarketUserEntryList entry : category.entries) {
            if(MarketItemHelper.isEquals(j, entry.itemStack)) {
                return entry;
            }
        }

        if(MarketDataManager.GLOBAL_CONFIG_SERVER != null && MarketDataManager.GLOBAL_CONFIG_SERVER.sellAnyItems){
            if(Objects.equals(category.categoryID.toString(), "619a4773-efd4-46e5-97c5-5ce1ce2da517")){
                MarketUserEntryList entryList = new MarketUserEntryList();
                entryList.itemStack = j;
                category.addEntry(entryList);
                for (MarketUserEntryList entry : category.entries) {
                    if(MarketItemHelper.isEquals(j, entry.itemStack))
                        return entry;
                }
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

        if( (MarketDataManager.GLOBAL_CONFIG_SERVER != null && MarketDataManager.GLOBAL_CONFIG_SERVER.sellAnyItems) ||
            (MarketDataManager.GLOBAL_CONFIG_CLIENT != null && MarketDataManager.GLOBAL_CONFIG_CLIENT.sellAnyItems)
        ) {
            return true;
        }

        return false;
    }

    public static MarketConfigCategory getCategoryForItem(ItemStack itemStack, MarketConfigData configData){
        for (MarketConfigCategory category : configData.CATEGORIES) {
            for (AbstractMarketConfigEntry entry : category.entries) {
                if(entry.isAvailable(itemStack)) return category;
            }
        }

        if( (MarketDataManager.GLOBAL_CONFIG_SERVER != null && MarketDataManager.GLOBAL_CONFIG_SERVER.sellAnyItems) ||
                (MarketDataManager.GLOBAL_CONFIG_CLIENT != null && MarketDataManager.GLOBAL_CONFIG_CLIENT.sellAnyItems)
        ) {
            MarketUserAnyCategory _category = new MarketUserAnyCategory();
            return new MarketConfigCategory(_category.categoryID, _category.categoryName);
        }

        return null;
    }

    public static AbstractMarketConfigEntry getEntryForItem(ItemStack itemStack, MarketConfigData configData, MarketConfigCategory category) {
        for (AbstractMarketConfigEntry entry : category.entries) {
            if(entry.isAvailable(itemStack)) return entry;
        }

        if( (MarketDataManager.GLOBAL_CONFIG_SERVER != null && MarketDataManager.GLOBAL_CONFIG_SERVER.sellAnyItems) ||
                (MarketDataManager.GLOBAL_CONFIG_CLIENT != null && MarketDataManager.GLOBAL_CONFIG_CLIENT.sellAnyItems)
        ) {
            MarketUserAnyCategory _category = new MarketUserAnyCategory();
            return new ItemMarketConfigEntry(_category.categoryID, itemStack);
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

                if(entry instanceof ItemTagMarketConfigEntry tagConfig) {
                    Optional<HolderSet.Named<Item>> t = tagConfig.getItems();

                    if(t.isEmpty()) continue;

                    var ent = t.get();

                    for (Holder<Item> itemHolder : ent) {
                        MarketUserEntryList userEntryList = new MarketUserEntryList();
                        userEntryList.tagID = tagConfig.tagKey;
                        userEntryList.itemStack = itemHolder.value().getDefaultInstance();

                        userCategory.entries.add(userEntryList);
                    }

                } else {
                    MarketUserEntryList userEntryList = new MarketUserEntryList();

                    if(entry.getIcon() instanceof ItemIcon icon) {
                        userEntryList.itemStack = icon.getStack();
                    }

                    userCategory.entries.add(userEntryList);
                }

            }

            userData.categories.add(userCategory);
        }

        if(MarketDataManager.GLOBAL_CONFIG_SERVER.sellAnyItems){
            MarketUserAnyCategory anyCategory = new MarketUserAnyCategory();
            userData.categories.add(anyCategory);
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
        for (ItemStack stack : list) {
            if(MarketItemHelper.isEquals(itemStack.copy(), stack.copy())) {
                return true;
            }
        }
        return false;
    }
}
