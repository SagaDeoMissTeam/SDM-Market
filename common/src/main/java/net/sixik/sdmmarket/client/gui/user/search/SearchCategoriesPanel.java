package net.sixik.sdmmarket.client.gui.user.search;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.network.chat.TranslatableComponent;
import net.sixik.sdmmarket.client.SearchData;
import net.sixik.sdmmarket.client.gui.ui.TextRenderHelper;
import net.sixik.sdmmarket.client.gui.user.MarketUserScreen;
import net.sixik.sdmmarket.client.widgets.MarketCheckBox;
import net.sixik.sdmmarket.common.data.MarketDataManager;

import net.sixik.sdmmarket.common.market.user.MarketUserCategory;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchCategoriesPanel extends Panel {


    public SearchCategoriesPanel(Panel panel) {
        super(panel);
    }

    @Override
    public void addWidgets() {
        addContent();
    }

    @Override
    public void alignWidgets() {
        addContent();
    }

    public void addContent() {
        clearWidgets();
        List<MarketCheckBox> checkBoxes = new ArrayList<>();
        for (MarketUserCategory category : MarketDataManager.USER_CLIENT.categories) {
            MarketCheckBox box = new MarketCheckBox(this) {
                @Override
                public void onClicked(MouseButton button) {
                    super.onClicked(button);

                    if(isChecked) {
                        SearchData.selectedCategories.add(category.categoryID);
                    } else {
                        SearchData.selectedCategories.removeIf(s -> Objects.equals(s, category.categoryID));
                    }

                    ((MarketUserScreen)getGui()).refreshEntries();
                }
            };

            if(SearchData.selectedCategories.contains(category.categoryID)) {
                box.setSelected(true);
            }

            box.setTitle(new TranslatableComponent(category.categoryName));
            box.setSize(this.width - 4, TextRenderHelper.getTextHeight() + 1);
            checkBoxes.add(box);
        }

        checkBoxes.add(new MarketCheckBox(this) {
            @Override
            public void draw(PoseStack graphics, Theme theme, int x, int y, int w, int h) {

            }

            @Override
            public boolean checkMouseOver(int mouseX, int mouseY) {
                return false;
            }
        });
        calculatePosition(checkBoxes);

        addAll(checkBoxes);

    }

    public void calculatePosition(List<MarketCheckBox> checkBoxes) {
        int y = 1;
        for (MarketCheckBox checkBox : checkBoxes) {
            checkBox.setPos(0, y);
            y += checkBox.height + 1;
        }
    }

    @Override
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        Color4I.rgba(100,100,100,255/3).draw(graphics,x,y,w,h);
    }
}
