package net.sixik.sdmmarket.client.gui.user.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.network.chat.TextComponent;
import net.sixik.sdmmarket.SDMMarketIcons;
import net.sixik.sdmmarket.client.gui.admin.MarketAdminScreen;

public class OpenAdminMenuButton extends SimpleTextButton {

    public OpenAdminMenuButton(Panel panel) {
        super(panel, TextComponent.EMPTY, SDMMarketIcons.TOGGLE_WHITE);
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
        list.add(new TextComponent("Admin Menu"));
    }

    @Override
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        Color4I.rgba(0,0,0,255/3).draw(graphics,x,y,w,h);
    }
}
