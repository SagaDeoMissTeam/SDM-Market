package net.sixik.sdmmarket.common.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.sixik.sdmmarket.common.market.basketEntry.AbstractBasketEntry;
import net.sixik.sdmmarket.common.market.user.MarketUserCategory;
import net.sixik.sdmmarket.common.market.user.MarketUserEntry;
import net.sixik.sdmmarket.common.market.user.MarketUserEntryList;
import net.sixik.sdmmarket.common.utils.INBTSerialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MarketPlayerData {

    public List<PlayerData> PLAYERS = new ArrayList<>();

    public static class PlayerData implements INBTSerialize {
        public UUID playerID;
        public int countOffers = 0;
        public long numberOfOperations = 0;
        public long countMoneyByOperations = 0;
        public List<UUID> playerOffers = new ArrayList<>();
        public PlayerBasket playerBasket = new PlayerBasket();

        public PlayerData() {
            if(MarketDataManager.GLOBAL_CONFIG_SERVER != null) {
                this.countOffers = MarketDataManager.GLOBAL_CONFIG_SERVER.maxOffersForPlayer;
            }
        }

        public List<MarketUserEntry> findClientOffers() {
            if(playerOffers.isEmpty()) return new ArrayList<>();
            List<MarketUserEntry> list = new ArrayList<>();
            for (MarketUserCategory category : MarketDataManager.USER_CLIENT.categories) {
                for (MarketUserEntryList entry : category.entries) {
                    for (MarketUserEntry userEntry : entry.entries) {
                        if(Objects.equals(userEntry.ownerID, playerID)) {
                            list.add(userEntry);
                        }
                    }
                }
            }

            return list;
        }

        @Override
        public CompoundTag serialize() {
            CompoundTag nbt = new CompoundTag();
            nbt.putUUID("playerID", playerID);
            nbt.putInt("countOffers", countOffers);
            nbt.putLong("numberOfOperations", numberOfOperations);
            nbt.putLong("countMoneyByOperations", countMoneyByOperations);
            nbt.put("playerBasket", playerBasket.serialize());
            ListTag d1 = new ListTag();
            for (UUID playerOffer : playerOffers) {
                d1.add(NbtUtils.createUUID(playerOffer));
            }
            nbt.put("offers", d1);

            return nbt;
        }

        @Override
        public void deserialize(CompoundTag nbt) {
            this.playerID = nbt.getUUID("playerID");
            this.countOffers = nbt.getInt("countOffers");
            this.numberOfOperations = nbt.getLong("numberOfOperations");
            this.countMoneyByOperations = nbt.getLong("countMoneyByOperations");
            this.playerBasket.deserialize(nbt.getCompound("playerBasket"));

            playerOffers.clear();
            if(nbt.contains("offers")) {
                ListTag d1 = (ListTag) nbt.get("offers");
                for (Tag tag : d1) {
                    playerOffers.add(NbtUtils.loadUUID(tag));
                }
            }
        }
    }

    public static class PlayerBasket implements INBTSerialize{
        public List<AbstractBasketEntry> basketMoneyEntries = new ArrayList<>();

        @Override
        public CompoundTag serialize() {
            CompoundTag nbt = new CompoundTag();
            ListTag list = new ListTag();
            for (AbstractBasketEntry basketMoneyEntry : basketMoneyEntries) {
                list.add(basketMoneyEntry.serialize());
            }
            nbt.put("basketMoneyEntries", list);
            return nbt;
        }

        @Override
        public void deserialize(CompoundTag nbt) {
            if(nbt.contains("basketMoneyEntries")){
                ListTag d1 = (ListTag) nbt.get("basketMoneyEntries");
                basketMoneyEntries.clear();
                for (Tag tag : d1) {
                    AbstractBasketEntry f = AbstractBasketEntry.from((CompoundTag) tag);
                    if(f != null)
                        basketMoneyEntries.add(f);
                }
            }
        }
    }
}
