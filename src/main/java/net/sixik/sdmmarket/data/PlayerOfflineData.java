package net.sixik.sdmmarket.data;

import dev.ftb.mods.ftblibrary.snbt.SNBT;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.common.util.INBTSerializable;
import net.sdm.sdmshopr.SDMShopR;
import net.sixik.sdmcore.impl.utils.serializer.data.IData;
import net.sixik.sdmcore.impl.utils.serializer.data.ListData;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerOfflineData implements INBTSerializable<CompoundTag> {

    public Path path;

    public HashMap<UUID, Integer> MONEY_DATA = new HashMap<>();

    public PlayerOfflineData(MinecraftServer server){
        Path path1 = server.getWorldPath(LevelResource.ROOT).resolve("marketData");
        if(!path1.toFile().exists()) path1.toFile().mkdirs();
        path = path1.resolve("offlineData.snbt");
    }

    public void addPlayer(UUID player, int money){
        if(MONEY_DATA.containsKey(player)) MONEY_DATA.put(player, MONEY_DATA.get(player) + money);
        else MONEY_DATA.put(player, money);
    }

    public void givePlayerMoney(ServerPlayer player){
        if(MONEY_DATA.containsKey(player.getUUID())){
            SDMShopR.addMoney(player, MONEY_DATA.get(player.getUUID()));
            MONEY_DATA.remove(player.getUUID());

            player.sendSystemMessage(Component.literal("You sell some lot !"));
        }
    }

    public void givePlayerMoney(ServerPlayer oldPlayer, ServerPlayer newPlayer){
        if(MONEY_DATA.containsKey(oldPlayer.getUUID())){
            SDMShopR.addMoney(newPlayer, MONEY_DATA.get(oldPlayer.getUUID()));
            MONEY_DATA.remove(oldPlayer.getUUID());

            newPlayer.sendSystemMessage(Component.literal("You sell some lot !"));
        }
    }

    public boolean removePlayer(ServerPlayer player){
        if(MONEY_DATA.containsKey(player.getUUID())) {
            MONEY_DATA.remove(player.getUUID());
            return true;
        }
        return false;
    }

    public void write(){
        SNBT.write(path, serializeNBT());
    }
    public void read(){
        SNBTCompoundTag nbt = SNBT.read(path);
        if(nbt != null) deserializeNBT(nbt);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();

        ListTag listTag = new ListTag();
        for (Map.Entry<UUID, Integer> uuidLongEntry : MONEY_DATA.entrySet()) {
            CompoundTag d1 = new CompoundTag();
            d1.putUUID("player", uuidLongEntry.getKey());
            d1.putInt("money", uuidLongEntry.getValue());
        }
        nbt.put("data", listTag);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ListTag listTag = (ListTag) nbt.get("data");
        MONEY_DATA.clear();
        for (Tag tag : listTag) {
            CompoundTag d1 = (CompoundTag) tag;
            MONEY_DATA.put(d1.getUUID("player"), d1.getInt("money"));
        }
    }
}
