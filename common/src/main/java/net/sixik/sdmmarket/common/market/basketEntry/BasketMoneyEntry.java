package net.sixik.sdmmarket.common.market.basketEntry;

import dev.ftb.mods.ftbteams.data.ClientTeamManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.sdm.sdmshopr.SDMShopR;


public class BasketMoneyEntry extends AbstractBasketEntry{

    public long moneyCount;
    public BasketMoneyEntry(long moneyCount){
        super();
        this.moneyCount = moneyCount;
    }


    @Override
    public void givePlayer(Player player) {

        if (player instanceof ServerPlayer serverPlayer)
            SDMShopR.addMoney(serverPlayer, moneyCount);
        else {
            SDMShopR.addMoney(ClientTeamManager.INSTANCE.selfKnownPlayer, moneyCount);
        }
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
