package net.sixik.sdmmarket.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public class PlayerData implements INBTSerializable<CompoundTag> {

    public UUID playerUUID;
    public String nickName;

    public PlayerData(){}

    public PlayerData(Player player) {
        playerUUID = player.getUUID();
        nickName = player.getName().getString();
    }


    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putUUID("playerUUID", playerUUID);
        nbt.putString("nickName", nickName);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        playerUUID = compoundTag.getUUID("playerUUID");
        nickName = compoundTag.getString("nickName");
    }
}
