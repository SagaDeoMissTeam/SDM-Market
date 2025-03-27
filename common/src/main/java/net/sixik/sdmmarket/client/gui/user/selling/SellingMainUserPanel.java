package net.sixik.sdmmarket.client.gui.user.selling;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.sixik.sdmmarket.SDMMarket;
import net.sixik.sdmmarket.client.gui.ui.TextRenderHelper;
import net.sixik.sdmmarket.client.widgets.MarketTextBox;
import net.sixik.sdmmarket.client.widgets.MarketTextField;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.market.user.MarketUserEntry;
import net.sixik.sdmmarket.common.network.user.CreateOfferC2S;
import net.sixik.sdmmarket.common.utils.MarketItemHelper;




public class SellingMainUserPanel extends Panel {

    public int countSell = 0;
    public int priceSell = 0;
    public int countSellOffers = 0;

    public MarketTextField minPrice;
    public MarketTextField maxPrice;
    public MarketTextField countOffers;
    public MarketTextField canBeSell;

    public MarketTextField howSellingText;
    public MarketTextBox howSelling;

    public MarketTextField howPriceText;
    public MarketTextBox howPrice;

    public MarketTextField countOffersText;
    public MarketTextBox countOffersInput;

    public SimpleTextButton acceptSell;

    public SellingUserScreen panel;
    public SellingMainUserPanel(SellingUserScreen panel) {
        super(panel);
        this.panel = panel;
    }

    @Override
    public void addWidgets() {
        recreate();
    }

    @Override
    public void alignWidgets() {
        recreate();
    }

    public int countItems = 0;

    public void recreate() {
        clearWidgets();

        if(!panel.selectable.selectedItem.isEmpty()) {

            add(minPrice = new MarketTextField(this));
            add(maxPrice = new MarketTextField(this));

            minPrice.setWidth(this.width - 38);
            minPrice.setMaxWidth(this.width - 38);
            minPrice.setPos(38, 4);

            maxPrice.setWidth(minPrice.width);
            maxPrice.setMaxWidth(minPrice.maxWidth);
            maxPrice.setPos(38, minPrice.posY + TextRenderHelper.getTextHeight() + 2);

            boolean f = panel.selectable.entry.minPrice > 0;
            minPrice.setText("Min Price: " + (f ? SDMMarket.moneyString(panel.selectable.entry.minPrice) : new TranslatableComponent("sdm.market.user.create.any_price").getString()));
            f = panel.selectable.entry.maxPrice > 0;
            maxPrice.setText("Max Price: " + (f ? SDMMarket.moneyString(panel.selectable.entry.maxPrice) : new TranslatableComponent("sdm.market.user.create.any_price").getString()));

            add(countOffers = new MarketTextField(this));
            countOffers.setWidth(minPrice.width);
            countOffers.setMaxWidth(minPrice.maxWidth);
            countOffers.setPos(38, maxPrice.posY + TextRenderHelper.getTextHeight() + 2);
            countOffers.setText(new TranslatableComponent("sdm.market.user.create.count_offers", MarketDataManager.PLAYER_CLIENT_DATA.countOffers));

            add(canBeSell = new MarketTextField(this));

            countItems = MarketItemHelper.countItems(Minecraft.getInstance().player, panel.selectable.selectedItem.copy());

            canBeSell.setText(new TranslatableComponent("sdm.market.user.create.can_sell", countItems));
            canBeSell.setWidth(this.width - 8);
            canBeSell.setMaxWidth(this.width - 8);
            canBeSell.setPos(4, 38);

            add(howSellingText = new MarketTextField(this));
            howSellingText.setPos(4, canBeSell.posY + canBeSell.height + 2);
            howSellingText.setWidth(this.width / 2);
            howSellingText.setText(new TranslatableComponent("sdm.market.user.create.how_selling"));

            add(howSelling = new MarketTextBox(this) {
                @Override
                public void onTextChanged() {
                    if(!getText().isEmpty()) {
                        countSell = Integer.parseInt(getText());

                        if(countItems != 0 && countSell != 0) {
                            countSellOffers = Math.min(1, countItems / countSell);
                            countOffersInput.setText(String.valueOf(countSellOffers));
                        }
                    }
                }

                @Override
                public boolean isValid(String txt) {
                    if(txt.matches("\\d+")) {
                        if(txt.startsWith("0") && txt.length() > 1) txt = txt.substring(1);

                        int i = Integer.parseInt(txt);
                        return i >= 0 && i <= countItems;
                    }

                    return false;
                }
            });
            howSelling.ghostText = new TranslatableComponent("sdm.market.user.create.write_text").getString();
            howSelling.setText(String.valueOf(countSell));
            howSelling.setPos(5 + this.width / 2, canBeSell.posY + canBeSell.height + 2);
            howSelling.setSize(this.width / 2 - 10, howSellingText.height);

            add(howPriceText = new MarketTextField(this));
            howPriceText.setPos(4, howSellingText.posY + howSellingText.height + 2);
            howPriceText.setWidth(this.width / 2);
            howPriceText.setText(new TranslatableComponent("sdm.market.user.create.how_price"));

            add(howPrice = new MarketTextBox(this) {
                @Override
                public void onTextChanged() {
                    if(!getText().isEmpty()) {
                        priceSell = Integer.parseInt(getText());
                    }
                }

                @Override
                public boolean isValid(String txt) {
                    if(txt.matches("\\d+")) {

                        if(txt.startsWith("0") && txt.length() > 1) txt = txt.substring(1);

                        int i = Integer.parseInt(txt);



                        if(i <  panel.selectable.entry.minPrice) {
                            txt = String.valueOf(panel.selectable.entry.minPrice + 1);
                            i = (int) (panel.selectable.entry.minPrice + 1);
                            priceSell = i;
                            setText(txt);
                            panel.refreshWidgets();
                        }

                        if(panel.selectable.entry.maxPrice == 0) {
                            return i >= panel.selectable.entry.minPrice;
                        }

                        return i > 0 && i <= panel.selectable.entry.maxPrice;
                    }

                    return false;
                }
            });
            howPrice.ghostText = new TranslatableComponent("sdm.market.user.create.write_text").getString();
            howPrice.setText(String.valueOf(priceSell));
            howPrice.setPos(5 + this.width / 2, howSellingText.posY + howSellingText.height + 2);
            howPrice.setSize(this.width / 2 - 10, howPriceText.height);

            add(countOffersText = new MarketTextField(this));
            countOffersText.setPos(4, howPrice.posY + howSellingText.height + 2);
            countOffersText.setWidth(this.width / 2);
            countOffersText.setText(new TranslatableComponent("sdm.market.user.create.count_offers_want"));

            add(countOffersInput = new MarketTextBox(this) {
                @Override
                public void onTextChanged() {
                    if(!getText().isEmpty()) {
                        countSellOffers = Integer.parseInt(getText());
                    }
                }

                @Override
                public boolean isValid(String txt) {
                    if(txt.matches("\\d+")) {
                        int i = Integer.parseInt(txt);
                        if(countItems != 0 && countSell != 0) {
                            countSellOffers = countItems / countSell;
                            return i > 0 && i <= countSellOffers;
                        }
                    }

                    return false;
                }
            });
            countOffersInput.ghostText = new TranslatableComponent("sdm.market.user.create.write_text").getString();
            countOffersInput.setText(String.valueOf(countSellOffers));
            countOffersInput.setPos(5 + this.width / 2, howPrice.posY + howSellingText.height + 2);
            countOffersInput.setSize(this.width / 2 - 10, howPriceText.height);

            add(acceptSell = new SimpleTextButton(this, new TranslatableComponent("sdm.market.user.create.create_lot"), Icons.ADD) {
                @Override
                public void onClicked(MouseButton button) {
                    if(button.isLeft() && countSellOffers > 0) {
                        try {
                            //for (int i = 0; i < countSellOffers; i++) {
                                MarketUserEntry entry = new MarketUserEntry(panel.selectable.configCategory.categoryID);
                                entry.itemStack = panel.selectable.selectedItem.copy();
                                entry.price = priceSell;
                                entry.count = countSell;
                                entry.ownerID = Minecraft.getInstance().player.getGameProfile().getId();


                                new CreateOfferC2S(countSellOffers,entry.serialize()).sendToServer();
                            //}

                            ((SellingUserScreen) getGui()).mainUserPanel.priceSell = 0;
                            ((SellingUserScreen) getGui()).mainUserPanel.countItems = 0;
                            ((SellingUserScreen) getGui()).mainUserPanel.countSell = 0;
                            ((SellingUserScreen) getGui()).sellingPanel.addSellableItems();
                            ((SellingUserScreen) getGui()).mainUserPanel.recreate();

                        } catch (Exception e) {
                            SDMMarket.printStackTrace("", e);
                        }

                        getGui().closeGui();
                    }
                }

                @Override
                public void draw(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
                    if(canCreate()) {
                        super.draw(graphics, theme, x, y, w, h);
                    }
                }

                @Override
                public boolean checkMouseOver(int mouseX, int mouseY) {
                    return canCreate() && super.checkMouseOver(mouseX, mouseY);
                }

                @Override
                public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
                    Color4I.rgba(100,100,100, 255/3).draw(graphics,x,y,w,h);
                }

                @Override
                public boolean renderTitleInCenter() {
                    return true;
                }
            });

            acceptSell.setSize(this.width - 8, TextRenderHelper.getTextHeight() + 3);
            acceptSell.setPos(4, this.height - (TextRenderHelper.getTextHeight() + 5));
        }
    }

    public boolean canCreate() {
        boolean flag1 = MarketDataManager.PLAYER_CLIENT_DATA.countOffers > 0;
        boolean flag2 = countSell > 0;
        boolean flag3 = priceSell >= panel.selectable.entry.minPrice;
        return flag1 && flag2 && flag3;
    }

    @Override
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        Color4I.rgba(0,0,0,255/3).draw(graphics,x,y,w,h);

        int x1 = x + 4;
        int y1 = y + 4;
        Color4I.rgba(100,100,100,255/3).draw(graphics, x1,y1,32,32);
        if(!panel.selectable.selectedItem.isEmpty()) {
            ItemIcon.getItemIcon(panel.selectable.selectedItem).draw(graphics,x1,y1,32,32);
        }
    }
}
