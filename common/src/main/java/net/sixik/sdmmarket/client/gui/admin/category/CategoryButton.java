package net.sixik.sdmmarket.client.gui.admin.category;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.ui.EditConfigScreen;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.sixik.sdmmarket.client.gui.admin.MarketAdminScreen;
import net.sixik.sdmmarket.common.data.MarketDataManager;
import net.sixik.sdmmarket.common.market.config.MarketConfigCategory;
import net.sixik.sdmmarket.common.network.admin.CreateCategoryC2S;
import net.sixik.sdmmarket.common.network.admin.EditCategoryC2S;
import net.sixik.sdmmarket.common.register.CustomIconItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryButton extends SimpleTextButton {
    public boolean isEdit = false;


    public CategoryPanel parentPanel;
    public MarketConfigCategory category;
    public CategoryButton(CategoryPanel panel, MarketConfigCategory category) {
        super(panel, category != null ? new TextComponent(category.categoryName) : TextComponent.EMPTY, category != null ? CustomIconItem.getIcon(category.icon) : Icon.EMPTY);
        this.parentPanel = panel;
        this.category = category;
    }

    public CategoryButton setEdit() {
        this.isEdit = true;
        setIcon(Icons.ADD);
        setTitle(new TextComponent("Create"));
        return this;
    }

    @Override
    public void onClicked(MouseButton button) {
        if(button.isLeft()) {

            if(isEdit) {
                MarketConfigCategory category = new MarketConfigCategory("Empty Name");
                MarketDataManager.CONFIG_CLIENT.CATEGORIES.add(category);
                ((MarketAdminScreen)getGui()).addCategoryButtons();
                new CreateCategoryC2S(category.serialize()).sendToServer();
            }
            else {
                ((MarketAdminScreen)getGui()).selectedCategory = category;
                ((MarketAdminScreen)getGui()).addCategoryEntryButtons();
            }
        } else if(button.isRight() && !isEdit) {
            List<ContextMenuItem> contextMenu = new ArrayList<>();

            contextMenu.add(new ContextMenuItem(new TranslatableComponent("sdm.market.admin.edit"), Icons.SETTINGS, () -> {
                editScreen();
            }));

            contextMenu.add(new ContextMenuItem(new TranslatableComponent("sdm.market.delete"), Icons.REMOVE, () -> {
                if(MarketDataManager.CONFIG_CLIENT.CATEGORIES.removeIf(s -> s.categoryID.equals(category.categoryID))) {
                    if(Objects.equals(((MarketAdminScreen) getGui()).selectedCategory, category)) {
                        ((MarketAdminScreen)getGui()).selectedCategory = null;
                        ((MarketAdminScreen)getGui()).addCategoryEntryButtons();
                    }
                    ((MarketAdminScreen)getGui()).addCategoryButtons();

                    new EditCategoryC2S(category.categoryID, new CompoundTag()).sendToServer();
                }

                getGui().refreshWidgets();
            }));

            getGui().openContextMenu(contextMenu);
        }
    }

    public void editScreen() {
        ConfigGroup group = new ConfigGroup("category").setNameKey("sidebar_button.sdm.market");
        group.savedCallback = s -> {
            openGui();
            if(s) new EditCategoryC2S(category.categoryID, category.serialize()).sendToServer();
        };


        ConfigGroup g = group.getGroup("market").getGroup("category");
        category.config(g);
        new EditConfigScreen(group).openGui();
        getGui().refreshWidgets();
    }


    @Override
    public void addMouseOverText(TooltipList list) {

        if(!isEdit) {
            super.addMouseOverText(list);
        } else {
            if(Screen.hasShiftDown()) {
                list.add(new TranslatableComponent("sdm.market.admin.edit.description_1"));
                list.add(new TranslatableComponent("sdm.market.admin.edit.description_2"));
                list.add(new TranslatableComponent("sdm.market.admin.edit.description_3"));
                list.add(new TranslatableComponent("sdm.market.admin.edit.description_4"));
            } else {
                list.add(new TranslatableComponent("sdm.market.shift_info"));
            }
        }
    }

    public boolean isSelected() {
        return category != null && ((MarketAdminScreen)getGui()).selectedCategory == category;
    }

    @Override
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        if(isSelected()) {
            Color4I.rgba(255, 255, 255, 255 / 3).draw(graphics, x, y, w, h);
        } else {
            Color4I.rgba(0, 0, 0, 255 / 3).draw(graphics, x, y, w, h);
        }
    }
}
