package net.sixik.sdmmarket.common.data;

import dev.ftb.mods.ftblibrary.snbt.SNBT;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.sixik.sdmmarket.SDMMarket;
import net.sixik.sdmmarket.common.utils.INBTSerialize;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;

public class MarketFileManager {
    private static final String[] paths = new String[] {
            "SDMMarket",
            "SDMMarket/config",
            "SDMMarket/offers",
            "SDMMarket/players"
    };


    public static void init(MinecraftServer server) {
        createFolders(server);
    }


    public static void createFolders(MinecraftServer server) {
        for (String path : paths) {
           Path f1 = server.getWorldPath(LevelResource.ROOT).resolve(path);
           if(!f1.toFile().exists()) {
               f1.toFile().mkdir();
           }
        }
    }


    public static void saveData(MinecraftServer server, String path, INBTSerialize serialize) {
        Path f1 = server.getWorldPath(LevelResource.ROOT).resolve(path);
        try {
            NbtIo.write(serialize.serialize(), f1.toFile());
        } catch (IOException e) {
            SDMMarket.printStackTrace("", e);
        }
    }

    public static void saveDataReadable(MinecraftServer server, String path, INBTSerialize serialize){
        Path f1 = server.getWorldPath(LevelResource.ROOT).resolve(path);
        try {
            SNBT.write(f1,serialize.serialize());
        } catch (Exception e) {
            SDMMarket.printStackTrace("", e);
        }
    }

    @Nullable
    public static CompoundTag readData(MinecraftServer server, String path) {
        Path f1 = server.getWorldPath(LevelResource.ROOT).resolve(path);
        if(!f1.toFile().exists()) return null;
        try {
            return NbtIo.read(f1.toFile());
        } catch (Exception e) {
            SDMMarket.printStackTrace("", e);
        }
        return null;
    }

    @Nullable
    public static SNBTCompoundTag readDataReadable(MinecraftServer server, String path) {
        Path f1 = server.getWorldPath(LevelResource.ROOT).resolve(path);
        if(!f1.toFile().exists()) return null;
        try {
            return SNBT.read(f1);
        } catch (Exception e) {
            SDMMarket.printStackTrace("", e);
        }
        return null;
    }
}
