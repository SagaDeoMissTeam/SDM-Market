package net.sixik.sdmmarket.client.gui.admin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.network.chat.TextComponent;
import net.sixik.sdmmarket.common.network.admin.config.InvokeMarketEditC2S;

public class MarketAdminGlobalConfigButton extends SimpleTextButton {

    public MarketAdminGlobalConfigButton(Panel panel) {
        super(panel, TextComponent.EMPTY, Icons.SETTINGS);
        setSize(32,32);
    }

    @Override
    public void onClicked(MouseButton button) {
        if(button.isLeft()) {
            new InvokeMarketEditC2S().sendToServer();
        }
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        list.add(new TextComponent("Global Settings"));
    }

    @Override
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        Color4I.rgba(0,0,0, 255/3).draw(graphics, x,y,w,h);
    }
}
