package net.sixik.sdmmarket.api;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public interface IMarketEntryType extends INBTSerializable<CompoundTag> {

    String getTypeID();

    void executeOnCreate(ServerPlayer ownerPlayer, int count);
    void executeOnCloseOwnerOffline(UUID ownerPlayer, int price);
    void executeOnCloseOwnerOnline(ServerPlayer ownerPlayer, int price);
    void executeOnBuy(ServerPlayer buyerPlayer, int count, int price);

    Icon getIcon(int count);

    /**
        NBT Icon need only if icon is item
     */
    default CompoundTag getIconNBT(){
        return new CompoundTag();
    }

    IMarketEntryType copy();

    @OnlyIn(Dist.CLIENT)
    int getCountOnPlayer();

    default void addTooltips(TooltipList tooltipList) {

    }

    void getConfig(ConfigGroup group);

    /**
     * use {@link net.sixik.sdmmarket.data.ClientData}
     */
    boolean search();

    @Override
    default CompoundTag serializeNBT(){
        CompoundTag nbt = new CompoundTag();
        nbt.putString("typeID", getTypeID());
        return nbt;
    }
}
