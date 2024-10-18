package net.sixik.sdmmarket.common.data;

import dev.ftb.mods.ftblibrary.snbt.SNBT;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelResource;
import net.sixik.sdmmarket.SDMMarket;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class MarketDataManager {

    public static MarketConfigData CONFIG_CLIENT = new MarketConfigData();
    public static MarketConfigData CONFIG_SERVER;

    public static MarketUserData USER_CLIENT = new MarketUserData();
    public static MarketUserData USER_SERVER;

    public static MarketConfig GLOBAL_CONFIG_SERVER = new MarketConfig();
    public static MarketConfig GLOBAL_CONFIG_CLIENT = new MarketConfig();

    public static MarketPlayerData PLAYERS_SERVER_DATA = new MarketPlayerData();
    public static MarketPlayerData.PlayerData PLAYER_CLIENT_DATA = new MarketPlayerData.PlayerData();

    @Nullable
    public static MarketPlayerData.PlayerData getPlayerData(Player player) {
        Optional<MarketPlayerData.PlayerData> data = PLAYERS_SERVER_DATA.PLAYERS.stream().filter(s -> Objects.equals(s.playerID, player.getGameProfile().getId())).findFirst();
        if(data.isEmpty()) {
            MarketPlayerData.PlayerData f = new MarketPlayerData.PlayerData();
            f.playerID = player.getGameProfile().getId();
            PLAYERS_SERVER_DATA.PLAYERS.add(f);

            data = PLAYERS_SERVER_DATA.PLAYERS.stream().filter(s -> Objects.equals(s.playerID, player.getGameProfile().getId())).findFirst();
        }
        return data.orElse(null);
    }

    public static MarketPlayerData.PlayerData getPlayerData(MinecraftServer server, Player player) {
        return getPlayerData(server, player.getGameProfile().getId());
    }

    @Nullable
    public static MarketPlayerData.PlayerData getPlayerData(MinecraftServer server, UUID uuid) {
        for (MarketPlayerData.PlayerData player : PLAYERS_SERVER_DATA.PLAYERS) {
            if(Objects.equals(player.playerID, uuid)) {
                return player;
            }
        }

        loadPlayer(server, uuid);

        for (MarketPlayerData.PlayerData player : PLAYERS_SERVER_DATA.PLAYERS) {
            if(Objects.equals(player.playerID, uuid)) {
                return player;
            }
        }

        return null;
    }

    public static void savePlayers(MinecraftServer server){
        for (MarketPlayerData.PlayerData player : PLAYERS_SERVER_DATA.PLAYERS) {
            savePlayer(server, player);
        }
    }

    public static boolean savePlayer(MinecraftServer server, Player player) {
        return savePlayer(server, player.getGameProfile().getId());
    }

    public static boolean savePlayer(MinecraftServer server, UUID player) {
        MarketPlayerData.PlayerData data = null;
        for (MarketPlayerData.PlayerData playerData : PLAYERS_SERVER_DATA.PLAYERS) {
            if(Objects.equals(playerData.playerID, player)) {
                data = playerData;
                break;
            }
        }

        if(data == null) return false;
        return savePlayer(server, data);
    }

    public static boolean savePlayer(MinecraftServer server, MarketPlayerData.PlayerData data) {
        Path path = server.getWorldPath(LevelResource.ROOT).resolve("SDMMarket/players/" + data.playerID.toString() + ".sdm");
        SNBT.write(path, data.serialize());
        return true;
    }

    public static boolean saveAndRemove(MinecraftServer server, UUID player){
        if(savePlayer(server, player)) {
           return PLAYERS_SERVER_DATA.PLAYERS.removeIf(data -> Objects.equals(data.playerID, player));
        }
        return false;
    }

    public static void loadPlayers(MinecraftServer server){
        Path path = server.getWorldPath(LevelResource.ROOT).resolve("SDMMarket/players");
        if(!path.toFile().exists()) return;
        PLAYERS_SERVER_DATA.PLAYERS.clear();
        for (File file : path.toFile().listFiles()) {
            try {
               SNBTCompoundTag nbt = SNBT.read(file.toPath());
               if(nbt == null) continue;
               MarketPlayerData.PlayerData data = new MarketPlayerData.PlayerData();
                data.deserialize(nbt);
                PLAYERS_SERVER_DATA.PLAYERS.add(data);
            } catch (Exception e){
                SDMMarket.printStackTrace("", e);
            }
        }
    }

    public static void loadPlayer(MinecraftServer server, Player player){
        loadPlayer(server, player.getGameProfile().getId());
    }

    public static void loadPlayer(MinecraftServer server, UUID player){
        Path path = server.getWorldPath(LevelResource.ROOT).resolve("SDMMarket/players/" + player.toString() + ".sdm");
        if(!path.toFile().exists()) {
            MarketPlayerData.PlayerData d = new MarketPlayerData.PlayerData();
            d.playerID = player;
            PLAYERS_SERVER_DATA.PLAYERS.add(d);
            return;
        }

        try {
            UUID uuid = UUID.fromString(FilenameUtils.removeExtension(path.toFile().getName()));
            if(Objects.equals(uuid, player)) {
                SNBTCompoundTag nbt = SNBT.read(path.toFile().toPath());
                if(nbt == null) return;
                MarketPlayerData.PlayerData data = new MarketPlayerData.PlayerData();
                data.deserialize(nbt);
                PLAYERS_SERVER_DATA.PLAYERS.add(data);
            }
        } catch (Exception e){
            SDMMarket.printStackTrace("", e);
        }
    }


    public static void saveMarketData(MinecraftServer server){
        if(USER_SERVER == null) return;
        Path path = server.getWorldPath(LevelResource.ROOT).resolve("SDMMarket/offers/UsersOffers.sdm");
        SNBT.write(path, USER_SERVER.serialize());
    }

    public static void loadMarketData(MinecraftServer server){
        Path path = server.getWorldPath(LevelResource.ROOT).resolve("SDMMarket/offers/UsersOffers.sdm");
        CompoundTag nbt = SNBT.read(path);
        if(nbt == null) {
            USER_SERVER = new MarketUserData();
            return;
        }
        USER_SERVER = new MarketUserData();
        USER_SERVER.deserialize(nbt);
    }
}
