package net.sixik.sdmmarket.client.screen;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.NordColors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.sdm.sdmshopr.SDMShopR;
import net.sdm.sdmshopr.SDMShopRClient;
import net.sixik.sdmmarket.client.utils.Cursor2D;
import net.sixik.sdmmarket.data.entry.MarketEntry;
import net.sixik.sdmmarket.network.SendBuyEntryC2S;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.function.Consumer;

public class MarketBuyerScreen extends BaseScreen {

    @Override public boolean isDefaultScrollVertical() {return false;}

    public MarketEntry<?> entry;
    public TextField infoField;
    public TextField infoPriceField;
    public TextField moneyLeft;

    public MarketBuyerScreen.BuyButton buyButton;
    public MarketBuyerScreen.CancelButton cancelButton;
    public int cantBuy = 0;
    public int count = 0;

    public Cursor2D cursor2D = new Cursor2D(0,0);
    public MarketBuyerScreen(MarketEntry<?> entry){
        this.entry = entry;
        int bsize = this.width / 2 - 10;

        this.infoField = new TextField(this);
        this.infoField.setText(Component.translatable("sdm.market.buy.title"));
        this.infoField.setPos(2, 2);
        cursor2D.x += 2;
        cursor2D.y += 2 + infoField.height + 2;


        cursor2D.y += 32 + 2;
        Cursor2D f = new Cursor2D(0,0);

        this.infoPriceField = new TextField(this);
        this.infoPriceField.setText(Component.translatable("sdm.market.lot.info.price").append(" ").append(SDMShopR.moneyString(entry.price)));
        this.infoPriceField.setPos(cursor2D.x, cursor2D.y);

        cursor2D.y += this.infoField.height + 2;
        f.y += this.infoField.height + 2;

        this.moneyLeft = new TextField(this);
        this.moneyLeft.setText(Component.translatable("sdm.market.buy.leftmoney", SDMShopR.moneyString(SDMShopR.getClientMoney() - entry.price)));
        this.moneyLeft.setPos(cursor2D.x, cursor2D.y);

        cursor2D.y += moneyLeft.height + 2;
        f.y += moneyLeft.height + 2;
        f.y += 32 + 2;

        cursor2D.y -= f.y;

        this.cancelButton = new MarketBuyerScreen.CancelButton(this);
        this.cancelButton.setPosAndSize(8, this.height - 24, bsize, 16);

        this.buyButton = new MarketBuyerScreen.BuyButton(this);
        this.buyButton.setPosAndSize(this.width - bsize - 8, this.height - 24, bsize, 16);

    }

    @Override
    public ContextMenu openContextMenu(@NotNull List<ContextMenuItem> menu) {
        ContextMenu contextMenu = new ContextMenu(this, menu){
            @Override
            public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                NordColors.POLAR_NIGHT_3.draw(graphics, x + 1, y + 1, w - 2, h - 2);
                GuiHelper.drawHollowRect(graphics, x, y, w, h, Color4I.BLACK, false);
            }
        };
        this.openContextMenu(contextMenu);
        return contextMenu;
    }

    public void updateCountInfo(int count){


        refreshWidgets();
    }


    @Override
    public boolean onInit() {
        closeContextMenu();
        return true;
    }

    @Override
    public void addWidgets() {

        if(entry.price <= SDMShopR.getClientMoney())
            add(buyButton);
        add(cancelButton);

        add(infoField);
        add(infoPriceField);
        add(moneyLeft);

    }

    @Override
    public void alignWidgets() {
    }

    @Override
    public void drawOffsetBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        entry.getIcon().draw(graphics, x + cursor2D.x + 4,y + cursor2D.y,32,32);
    }

    @Override
    public void drawBackground(GuiGraphics matrixStack, Theme theme, int x, int y, int w, int h) {
        SDMShopRClient.shopTheme.getShadow().draw(matrixStack, x, y, w, h + 4);
        SDMShopRClient.shopTheme.getBackground().draw(matrixStack, x + 1, y + 1, w - 2, h - 2);
        GuiHelper.drawHollowRect(matrixStack, x, y, w, h, SDMShopRClient.shopTheme.getReact(), false);
        GuiHelper.drawHollowRect(matrixStack, x - 1, y - 1, w + 2, h + 5, SDMShopRClient.shopTheme.getStoke(), false);
    }

    @Override
    public boolean onClosedByKey(Key key) {
        if (super.onClosedByKey(key)) {
            if(key.is(GLFW.GLFW_KEY_E) || key.is(GLFW.GLFW_KEY_BACKSPACE)) return false;

            if (key.esc()) {
                cancelButton.onClicked(MouseButton.LEFT);
            } else {

            }

            return true;
        } else {
            return false;
        }
    }

    public boolean parse(@Nullable Consumer<Integer> callback, String string, int min, int max) {
        try {
            int v = Long.decode(string).intValue();
            if (v >= (Integer)min && v <= (Integer)max) {
                if (callback != null) {
                    callback.accept(v);
                }

                return true;
            }
        } catch (Exception var4) {
        }

        return false;
    }

    protected class CancelButton extends SimpleTextButton{

        public CancelButton(Panel panel) {
            super(panel, Component.translatable("sdm.shop.buyer.button.cancel"), Icon.empty());
        }

        @Override
        public void onClicked(MouseButton mouseButton) {
            if(mouseButton.isLeft()) {
                MarketBuyerScreen screen = (MarketBuyerScreen) getGui();
                screen.closeGui();

            }

        }

        @Override
        public boolean renderTitleInCenter() {
            return true;
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            SDMShopRClient.shopTheme.getShadow().draw(graphics, x, y, w, h + 4);
            SDMShopRClient.shopTheme.getBackground().draw(graphics, x + 1, y + 1, w - 2, h - 2);
            GuiHelper.drawHollowRect(graphics, x, y, w, h, SDMShopRClient.shopTheme.getReact(), false);
            GuiHelper.drawHollowRect(graphics, x - 1, y - 1, w + 2, h + 5, SDMShopRClient.shopTheme.getStoke(), false);
        }
    }

    protected class BuyButton extends SimpleTextButton{

        public BuyButton(Panel panel) {
            super(panel, Component.translatable("sdm.shop.buyer.button.accept"), Icon.empty());
        }

        @Override
        public void onClicked(MouseButton mouseButton) {
            if(mouseButton.isLeft()){

                new SendBuyEntryC2S(entry.uuid).sendToServer();
                MarketBuyerScreen.this.closeGui();
                MarketScreen.refreshAll();
            }
        }

        @Override
        public boolean renderTitleInCenter() {
            return true;
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            SDMShopRClient.shopTheme.getShadow().draw(graphics, x, y, w, h + 4);
            SDMShopRClient.shopTheme.getBackground().draw(graphics, x + 1, y + 1, w - 2, h - 2);
            GuiHelper.drawHollowRect(graphics, x, y, w, h, SDMShopRClient.shopTheme.getReact(), false);
            GuiHelper.drawHollowRect(graphics, x - 1, y - 1, w + 2, h + 5, SDMShopRClient.shopTheme.getStoke(), false);
        }
    }
}
