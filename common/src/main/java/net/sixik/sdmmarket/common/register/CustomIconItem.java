package net.sixik.sdmmarket.common.register;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomIconItem extends Item {

    public CustomIconItem() {
        super(new Properties().stacksTo(1));
    }


    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("item.ftbquests.custom_icon.tooltip").withStyle(ChatFormatting.GRAY));
        if (stack.hasTag() && stack.getTag().contains("Icon")) {
            tooltip.add(new TextComponent(stack.getTag().getString("Icon")).withStyle(ChatFormatting.DARK_GRAY));
        } else {
            tooltip.add(new TextComponent("-").withStyle(ChatFormatting.DARK_GRAY));
        }

    }

    public static Icon getIcon(ItemStack stack) {
        if (stack.getItem() instanceof CustomIconItem) {
            return stack.hasTag() && stack.getTag().contains("Icon") ? Icon.getIcon(stack.getTag().getString("Icon")) : Icon.getIcon("minecraft:textures/misc/unknown_pack.png");
        } else {
            return ItemIcon.getItemIcon(stack);
        }
    }
}
