package net.sixik.sdmmarket.client.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;

import net.minecraft.network.chat.TextComponent;
import net.sixik.sdmmarket.client.gui.ui.RenderHelper;

public class MarketCheckBox extends SimpleTextButton {

    public boolean isChecked = false;

    public MarketCheckBox(Panel panel) {
        super(panel, TextComponent.EMPTY, Icons.ADD);
    }

    @Override
    public void onClicked(MouseButton button) {
        if(button.isLeft()) {
            setSelected(!isChecked);
        }
    }

    public MarketCheckBox setSelected(boolean value) {
        isChecked = value;
        return this;
    }

    @Override
    public void drawIcon(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        if(isChecked){
            Color4I.rgba(255,255,255, 255).draw(graphics, x, y, w, h );
        }
        RenderHelper.drawHollowRect(graphics, x, y, w, h, Color4I.rgba(100, 100, 100, 255 / 3), false);
    }

    @Override
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        Color4I.rgba(0,0,0, 255/3).draw(graphics, x, y, w, h);
    }
}
