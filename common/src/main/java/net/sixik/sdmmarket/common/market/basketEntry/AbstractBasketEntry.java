package net.sixik.sdmmarket.common.market.basketEntry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.sixik.sdmmarket.common.utils.INBTSerialize;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class AbstractBasketEntry implements INBTSerialize {

    public UUID basketEntryID;

    public AbstractBasketEntry(){
        this.basketEntryID = UUID.randomUUID();
    }

    @Nullable
    public static AbstractBasketEntry from(CompoundTag nbt){
        String id = nbt.getString("basketEntryType");
        if(id.equals("basketMoneyEntry")){
            BasketMoneyEntry entry =new BasketMoneyEntry(0);
            entry.deserialize(nbt);
            return entry;
        }
        if(id.equals("basketItemEntry")){
            BasketItemEntry entry = new BasketItemEntry(null, 0);
            entry.deserialize(nbt);
            return entry;
        }


        return null;
    }


    public abstract void givePlayer(Player player);

    public abstract String getID();

    @Override
    public CompoundTag serialize() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("basketEntryType", getID());
        nbt.putUUID("basketEntryID", basketEntryID);
        return nbt;
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        basketEntryID = nbt.getUUID("basketEntryID");
    }
}
