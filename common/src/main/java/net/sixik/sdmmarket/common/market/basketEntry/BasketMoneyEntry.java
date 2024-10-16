package net.sixik.sdmmarket.common.market.basketEntry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.sixik.sdm_economy.api.CurrencyHelper;

public class BasketMoneyEntry extends AbstractBasketEntry{

    public long moneyCount;
    public BasketMoneyEntry(long moneyCount){
        super();
        this.moneyCount = moneyCount;
    }


    @Override
    public void givePlayer(Player player) {
        CurrencyHelper.Basic.addMoney(player, moneyCount);
    }

    @Override
    public String getID() {
        return "basketMoneyEntry";
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag nbt = super.serialize();
        nbt.putLong("moneyCount", moneyCount);
        return nbt;
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        super.deserialize(nbt);
        this.moneyCount = nbt.getLong("moneyCount");
    }
}
