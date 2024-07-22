package net.sixik.sdmmarket.client.screen;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.ui.EditConfigScreen;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.NordColors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.sixik.sdmmarket.client.utils.Cursor2D;
import net.sixik.sdmmarket.data.ClientData;
import net.sixik.sdmmarket.data.MarketData;
import net.sixik.sdmmarket.data.entry.MarketEntry;
import net.sixik.sdmmarket.data.entry.type.ItemMarketType;
import net.sixik.sdmmarket.network.SendEntryCreateC2S;

public class SearchPanel extends Panel {

    public TextField searchTitle;
    public TextBox searchField;
    public MarketScreen marketScreen;
    public SimpleTextButton simpleButton;
    public SimpleTextButton createOfferButton;

    public SearchPanel(MarketScreen panel) {
        super(panel);
        this.marketScreen = panel;
    }


    @Override
    public void addWidgets() {


        add(searchTitle = new TextField(this).setText(Component.translatable("sdm.market.search.title")));


        add(searchField = new TextBox(this){
            @Override
            public void onEnterPressed() {
                super.onEnterPressed();
                ClientData.searchField = getText();
                marketScreen.updateEntries();
            }
        });
        searchField.setText(ClientData.searchField);

        add(simpleButton = new SimpleTextButton(this, Component.translatable("sdm.market.search.advanced"), Icons.SETTINGS) {
            @Override
            public void onClicked(MouseButton mouseButton) {
                if(mouseButton.isLeft()) {
                    ConfigGroup group = new ConfigGroup("advanced", groupButton -> {
                        openGui();

                        if(groupButton){

                        }
                    });
                    ClientData.config(group);
                    new EditConfigScreen(group).openGui();
                    MarketScreen.refreshIfOpen();
                }
            }
        });

        add(createOfferButton = new SimpleTextButton(this, Component.translatable("sdm.market.create.button"), Icons.ADD) {
            @Override
            public void onClicked(MouseButton mouseButton) {
                if(mouseButton.isLeft()){
                    MarketEntry<?> marketEntry = new MarketEntry<>(Minecraft.getInstance().player, 1, 1, new ItemMarketType(ItemStack.EMPTY), MarketData.CLIENT.getCategory("test"));


                    ConfigGroup group = new ConfigGroup("entry", groupButton -> {
                        openGui();

                        if(groupButton){
                            if(marketEntry.canCreate()) {
                                new SendEntryCreateC2S(marketEntry.serializeNBT()).sendToServer();
                            }
                        }
                        MarketScreen.refreshAll();
                    });

                    marketEntry.getConfig(group);
                    new EditConfigScreen(group).openGui();
                    MarketScreen.refreshAll();
                }
            }
        });
    }

    @Override
    public void alignWidgets() {
        Cursor2D cursor2D = new Cursor2D(0,0);

        Font font = Minecraft.getInstance().font;
        int searchTitleWight = (int) font.getSplitter().stringWidth(Component.translatable("sdm.market.search.title"));

        searchTitle.setX((searchTitleWight - (searchTitleWight) / 2));
        cursor2D.y += searchTitle.height + 2;

        searchField.setSize(this.width, new Theme().getFontHeight() + 1);
        searchField.setPos(0,cursor2D.y);
        cursor2D.y += searchField.height + 2;

        simpleButton.setSize(Math.min(100, this.width),font.lineHeight + 2);
        simpleButton.setY(cursor2D.y);

        cursor2D.y = this.height - font.lineHeight - 6;
        createOfferButton.setSize(Math.min(100, this.width),font.lineHeight + 2);
        createOfferButton.setY(cursor2D.y);
    }
}
