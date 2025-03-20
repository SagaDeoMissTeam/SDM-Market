package net.sixik.sdmmarket.client.gui.user.entries;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.sixik.sdmmarket.client.gui.user.MarketUserScreen;
import net.sixik.sdmmarket.client.gui.user.buyer.MarketUserBuyerScreen;
import net.sixik.sdmmarket.common.market.user.MarketUserCategory;
import net.sixik.sdmmarket.common.market.user.MarketUserEntryList;
import net.sixik.v2.color.RGBA;

public class MarketUserCategoryListButton extends SimpleTextButton {

    public MarketUserCategory category;
    public MarketUserEntryList entryList;

    private ItemStack item;
    public MarketUserCategoryListButton(Panel panel) {
        super(panel, Component.empty(), Icon.empty());
    }

    public MarketUserCategoryListButton setItem(ItemStack item){
        this.item = item;
        setIcon(ItemIcon.getItemIcon(item));
        setTitle(Component.literal(item.getDisplayName().getString().replace("[", "").replace("]", "")));
        return this;
    }

    public void setData(MarketUserEntryList entryList, MarketUserCategory category){
        this.category = category;
        this.entryList = entryList;
    }

    @Override
    public void onClicked(MouseButton button) {
        if(button.isLeft()) {
            new MarketUserBuyerScreen(category, entryList).openGui();


            if(getGui() instanceof MarketUserScreen screen) {
                screen.setScroll();
            }

        }
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        item.getTooltipLines(Minecraft.getInstance().player, TooltipFlag.NORMAL).forEach(list::add);
        if(item.isDamageableItem() && item.isDamaged()){
            list.add(Component.literal("Damage: " + (item.getMaxDamage() - item.getDamageValue()) + "/" + item.getMaxDamage()));
        }
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        RGBA.create(100,100,100,255/3).drawRoundFill(graphics,x,y,w,h, 6);
    }
}
