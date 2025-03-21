package net.sixik.sdmmarket.common.market.config;

import com.mojang.datafixers.util.Pair;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ItemTagMarketConfigEntry extends AbstractMarketConfigEntry{

    public static ResourceLocation DEFAULT = new ResourceLocation("minecraft", "anvil");


    public ResourceLocation tagKey;

    public ItemTagMarketConfigEntry(UUID categoryID) {
        super(categoryID);
        this.tagKey = DEFAULT;
    }

    @Override
    public void config(ConfigGroup group) {
        group.addEnum("tags", tagKey.toString(), v -> tagKey = new ResourceLocation(v), getTags());
    }

    @Override
    public boolean isAvailable(ItemStack itemStack) {
        Optional<TagKey<Item>> op = Registry.ITEM.getTags().filter(s -> s.getFirst().location().equals(tagKey)).findFirst().map(Pair::getFirst);
        return op.filter(itemStack::is).isPresent();
    }

    public Optional<HolderSet.Named<Item>> getItems() {
        return Registry.ITEM.getTags().filter(s -> s.getFirst().location().equals(tagKey)).findFirst().map(Pair::getSecond);
    }

    public NameMap<String> getTags(){
        List<String> str = new ArrayList<>();

        Registry.ITEM.getTags().forEach(s -> {
            str.add(s.getFirst().location().toString());
        });
        return NameMap.of(DEFAULT.toString(), str).create();
    }

    @Override
    public Icon getIcon() {
        Optional<Pair<TagKey<Item>, HolderSet.Named<Item>>> optional = Registry.ITEM.getTags().filter(s -> s.getFirst().location().equals(tagKey)).findFirst();
        if(optional.isEmpty()) return ItemIcon.getItemIcon(Items.NAME_TAG);

        HolderSet.Named<Item> itemHolder = optional.get().getSecond();
        return ItemIcon.getItemIcon(itemHolder.get(0).value());

    }

    @Override
    public CompoundTag serialize() {
        CompoundTag nbt = super.serialize();
        nbt.putString("typeID", "itemTagType");
        nbt.putString("tagKey", tagKey.toString());

        return nbt;
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        super.deserialize(nbt);
        this.tagKey = new ResourceLocation(nbt.getString("tagKey"));
    }
}
