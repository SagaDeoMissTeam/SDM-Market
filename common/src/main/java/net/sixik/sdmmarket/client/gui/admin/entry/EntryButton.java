package net.sixik.sdmmarket.client.gui.admin.entry;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.ui.EditConfigScreen;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sixik.sdmmarket.SDMMarket;
import net.sixik.sdmmarket.client.gui.admin.MarketAdminScreen;
import net.sixik.sdmmarket.client.gui.ui.GLRenderHelper;
import net.sixik.sdmmarket.client.gui.ui.TextRenderHelper;
import net.sixik.sdmmarket.client.gui.ui.Vector2;
import net.sixik.sdmmarket.client.gui.ui.Vector2f;
import net.sixik.sdmmarket.common.market.config.*;
import net.sixik.sdmmarket.common.network.admin.CreateCategoryEntryC2S;
import net.sixik.sdmmarket.common.network.admin.EditCategoryEntryC2S;


import java.util.ArrayList;
import java.util.List;

public class EntryButton extends SimpleTextButton {

    public boolean isEdit = false;

    public List<Holder<Item>> items = new ArrayList<>();

    public AbstractMarketConfigEntry configEntry;
    public EntryButton(Panel panel, AbstractMarketConfigEntry configEntry) {
        super(panel, TextComponent.EMPTY, configEntry != null ? configEntry.getIcon() : Icon.EMPTY);
        this.configEntry = configEntry;

        if(configEntry instanceof ItemTagMarketConfigEntry configEntry1) {
            this.items = configEntry1.getItems().get().stream().toList();
        }
    }

    public EntryButton setEdit() {
        isEdit = true;
        setIcon(Icons.ADD);

        return this;
    }

    @Override
    public void onClicked(MouseButton button) {
        if(button.isLeft()) {
            
            if(isEdit) {
                List<ContextMenuItem> fContext1 = new ArrayList<>();
                fContext1.add(new ContextMenuItem(new TranslatableComponent("sdm.market.admin.entry.tool"), Icons.ADD, () -> {
                    MarketConfigCategory category = ((MarketAdminScreen)getGui()).selectedCategory;
                    DurabilityMarketConfigEntry entry = new DurabilityMarketConfigEntry(category.categoryID, Items.BARRIER.getDefaultInstance());
                    category.entries.add(entry);
                    ((MarketAdminScreen)getGui()).addCategoryEntryButtons();
                    new CreateCategoryEntryC2S(category.categoryID, entry.serialize()).sendToServer();
                }));
                fContext1.add(new ContextMenuItem(new TranslatableComponent("sdm.market.admin.entry.item"), Icons.ADD, () -> {
                    MarketConfigCategory category = ((MarketAdminScreen)getGui()).selectedCategory;
                    ItemMarketConfigEntry entry = new ItemMarketConfigEntry(category.categoryID, Items.BARRIER.getDefaultInstance());
                    category.entries.add(entry);
                    ((MarketAdminScreen)getGui()).addCategoryEntryButtons();
                    new CreateCategoryEntryC2S(category.categoryID, entry.serialize()).sendToServer();
                }));
                fContext1.add(new ContextMenuItem(new TranslatableComponent("sdm.market.admin.entry.tag"), Icons.ADD, () -> {
                    MarketConfigCategory category = ((MarketAdminScreen)getGui()).selectedCategory;
                    ItemTagMarketConfigEntry entry = new ItemTagMarketConfigEntry(category.categoryID);
                    category.entries.add(entry);
                    ((MarketAdminScreen)getGui()).addCategoryEntryButtons();
                    new CreateCategoryEntryC2S(category.categoryID, entry.serialize()).sendToServer();
                }));

                getGui().openContextMenu(fContext1);
            } else {
                editScreen();
            }
        } else if(button.isRight() && !isEdit) {
            List<ContextMenuItem> contextMenu = new ArrayList<>();

            contextMenu.add(new ContextMenuItem(new TranslatableComponent("sdm.market.admin.edit"), Icons.SETTINGS, () -> {
                editScreen();
            }));

            contextMenu.add(new ContextMenuItem(new TranslatableComponent("sdm.market.delete"), Icons.REMOVE, () -> {
                ((MarketAdminScreen)getGui()).selectedCategory.entries.removeIf(s -> s.equals(configEntry));
                ((MarketAdminScreen)getGui()).addCategoryEntryButtons();

                new EditCategoryEntryC2S(((MarketAdminScreen)getGui()).selectedCategory.categoryID, configEntry.entryID, new CompoundTag()).sendToServer();
            }));

            getGui().openContextMenu(contextMenu);
        }
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        if(!isEdit) {
            ItemStack itemStack = ItemStack.EMPTY;

            if(configEntry instanceof ItemTagMarketConfigEntry tEntry) {

                list.add(new TextComponent("Tag: ").append(tEntry.tagKey.toString()));

            } else if (configEntry.getIcon() instanceof ItemIcon icon) {
                itemStack = icon.getStack();
            }

            if (!itemStack.isEmpty())
                list.add(itemStack.getDisplayName());

            list.add(new TextComponent("Min Price: " + (configEntry.minPrice > 0 ? SDMMarket.moneyString(configEntry.minPrice) : "Any Price")));
            list.add(new TextComponent("Max Price: " + (configEntry.maxPrice > 0 ? SDMMarket.moneyString(configEntry.maxPrice) : "Any Price")));
        }
    }

    public void editScreen() {
        ConfigGroup group = new ConfigGroup("entry").setNameKey("sidebar_button.sdm.market");

        group.savedCallback = ( s -> {
            openGui();

            if (s) {
                new EditCategoryEntryC2S(((MarketAdminScreen)getGui()).selectedCategory.categoryID, configEntry.entryID, configEntry.serialize()).sendToServer();
            }
        });

        ConfigGroup g = group.getGroup("market").getGroup("entry");
        configEntry.config(g);
        new EditConfigScreen(group).openGui();
        getGui().refreshWidgets();
    }

    private int i = 0;
    private int currentIndex = 0;

    @Override
    public void draw(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        if(isEdit) {
            super.draw(graphics, theme, x, y, w, h);
            return;
        }

        GuiHelper.setupDrawing();
        var s = h >= 16 ? 16 : 8;
        drawBackground(graphics, theme, x, y, w, h);
        drawIcon(graphics, theme, x + (w - s) / 2, y + 2, s, s);

        if(configEntry != null) {
            String text = "Any Price";
            if(configEntry.minPrice > 0) text = SDMMarket.moneyString(configEntry.minPrice);
            Vector2f size = TextRenderHelper.getTextRenderSize(text, w, 1f, 50);
            GLRenderHelper.pushTransform(graphics, new Vector2(x,y), new Vector2(1,1), size.y, 0);
            theme.drawString(graphics, text, x + 1, y + (h - theme.getFontHeight() - 1));
            GLRenderHelper.popTransform(graphics);
        }


        if (items.isEmpty() || items.size() == 1) {
            currentIndex = 0;
            return;
        }
        i++;

        if (i % 40 == 0) {
            currentIndex++;
            if (currentIndex >= items.size()) {
                currentIndex = 0;
            }

            icon = ItemIcon.getItemIcon(items.get(currentIndex).value());
        }
    }

    @Override
    public void drawBackground(PoseStack graphics, Theme theme, int x, int y, int w, int h) {
        Color4I.rgba(0,0,0,255/3).draw(graphics,x,y,w,h);
    }
}
