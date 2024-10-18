package net.sixik.sdmmarket.client.gui.user.entries;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.sixik.sdmmarket.client.SearchData;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.market.user.MarketUserCategory;
import net.sixik.sdmmarket.common.market.user.MarketUserEntryList;
import net.sixik.v2.color.RGBA;
import net.sixik.v2.render.TextRenderHelper;

import java.util.ArrayList;
import java.util.List;

public class EntriesPanel extends Panel {

    public EntriesPanel(Panel panel) {
        super(panel);
    }

    @Override
    public void addWidgets() {
        addEntries();
    }

    @Override
    public void alignWidgets() {
        addEntries();
    }

    public void addEntries() {
        clearWidgets();
        List<MarketUserCategoryListButton> list = new ArrayList<>();
        for (MarketUserCategory category : MarketDataManager.USER_CLIENT.categories) {
            if(SearchData.selectedCategories.isEmpty() || SearchData.selectedCategories.contains(category.categoryID)) {
                for (MarketUserEntryList entry : category.entries) {
                    if(entry.entries.isEmpty()) continue;

                    if(SearchData.isEncantable && !entry.hasEnchantments()) continue;

                    if(
                            (SearchData.priceFrom <= 0 || SearchData.priceFrom >= entry.getMinPrice()) &&
                            (SearchData.priceTo <= 0 || SearchData.priceTo <= entry.getMaxPrice())
                    ) {

                        if(
                                (SearchData.countFrom <= 0 || SearchData.countFrom >= entry.getMinCount()) &&
                                (SearchData.countTo <= 0 || SearchData.countTo <= entry.getMaxCount())
                        ) {

                            if (SearchData.name.isEmpty() ||
                                    (
                                            entry.itemStack.getDisplayName().getString().toLowerCase().contains(SearchData.name.toLowerCase()) ||
                                                    BuiltInRegistries.ITEM.getKey(entry.itemStack.getItem()).toString().contains(SearchData.name.toLowerCase())
                                    )
                            ) {

                                MarketUserCategoryListButton button = new MarketUserCategoryListButton(this);
                                button.setItem(entry.itemStack);
                                button.setSize(this.width - 8, TextRenderHelper.getTextHeight() + 3);
                                button.setData(entry, category);
                                list.add(button);
                            }
                        }
                    }
                }
            }
        }

        MarketUserCategoryListButton button = new MarketUserCategoryListButton(this) {
            @Override
            public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {

            }

            @Override
            public boolean checkMouseOver(int mouseX, int mouseY) {
                return false;
            }
        };
        button.setSize(this.width - 8, TextRenderHelper.getTextHeight() + 3);
        list.add(button);

        calculatePosition(list);

        addAll(list);
    }

    public void calculatePosition(List<MarketUserCategoryListButton> list) {
        int y = 4;
        for (MarketUserCategoryListButton button : list) {
            button.setPos(4, y);
            y += button.height + 2;
        }
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        RGBA.create(0,0,0,255/3).drawRoundFill(graphics,x,y,w,h,6);
    }
}
