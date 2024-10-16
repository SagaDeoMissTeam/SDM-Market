package net.sixik.sdmmarket.client.widgets;

import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.sixik.v2.color.RGBA;
import net.sixik.v2.render.RenderHelper;

public class MarketCheckBox extends SimpleTextButton {

    public boolean isChecked = false;

    public MarketCheckBox(Panel panel) {
        super(panel, Component.empty(), Icons.ADD);
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
    public void drawIcon(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        if(isChecked){
            RGBA.create(255,255,255, 255).draw(graphics, x, y, w, h );
        }
        RenderHelper.drawHollowRect(graphics, x, y, w, h, RGBA.create(100,100,100, 255/3), false);
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        RGBA.create(0,0,0, 255/3).drawRoundFill(graphics, x, y, w, h, 2);
    }
}
