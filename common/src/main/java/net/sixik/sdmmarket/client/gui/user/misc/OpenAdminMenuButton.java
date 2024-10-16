package net.sixik.sdmmarket.client.gui.user.misc;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.sixik.sdmmarket.SDMMarketIcons;
import net.sixik.sdmmarket.client.gui.admin.MarketAdminScreen;
import net.sixik.v2.color.RGBA;

public class OpenAdminMenuButton extends SimpleTextButton {

    public OpenAdminMenuButton(Panel panel) {
        super(panel, Component.empty(), SDMMarketIcons.TOGGLE_WHITE);
        setSize(32,32);
    }

    @Override
    public void onClicked(MouseButton button) {
        if(button.isLeft()) {
            new MarketAdminScreen().openGui();
        }
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        list.add(Component.literal("Admin Menu"));
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        RGBA.create(0,0,0,255/3).drawRoundFill(graphics,x,y,w,h,6);
    }
}
