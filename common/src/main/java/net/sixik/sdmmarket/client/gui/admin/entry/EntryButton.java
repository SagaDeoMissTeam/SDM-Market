package net.sixik.sdmmarket.client.gui.admin.entry;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.ui.EditConfigScreen;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sixik.sdmmarket.SDMMarket;
import net.sixik.sdmmarket.client.gui.admin.MarketAdminScreen;
import net.sixik.sdmmarket.common.market.config.*;
import net.sixik.sdmmarket.common.network.admin.CreateCategoryEntryC2S;
import net.sixik.sdmmarket.common.network.admin.EditCategoryEntryC2S;
import net.sixik.v2.color.RGBA;
import net.sixik.v2.render.GLRenderHelper;
import net.sixik.v2.render.TextRenderHelper;
import net.sixik.v2.utils.math.Vector2;
import net.sixik.v2.utils.math.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class EntryButton extends SimpleTextButton {

    public boolean isEdit = false;

    public List<Holder<Item>> items = new ArrayList<>();

    public AbstractMarketConfigEntry configEntry;
    public EntryButton(Panel panel, AbstractMarketConfigEntry configEntry) {
        super(panel, Component.empty(), configEntry != null ? configEntry.getIcon() : Icon.empty());
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
                fContext1.add(new ContextMenuItem(Component.translatable("sdm.market.admin.entry.tool"), Icons.ADD, (d) -> {
                    MarketConfigCategory category = ((MarketAdminScreen)getGui()).selectedCategory;
                    DurabilityMarketConfigEntry entry = new DurabilityMarketConfigEntry(category.categoryID, Items.BARRIER.getDefaultInstance());
                    category.entries.add(entry);
                    ((MarketAdminScreen)getGui()).addCategoryEntryButtons();
                    new CreateCategoryEntryC2S(category.categoryID, entry.serialize()).sendToServer();
                }));
                fContext1.add(new ContextMenuItem(Component.translatable("sdm.market.admin.entry.item"), Icons.ADD, (d) -> {
                    MarketConfigCategory category = ((MarketAdminScreen)getGui()).selectedCategory;
                    ItemMarketConfigEntry entry = new ItemMarketConfigEntry(category.categoryID, Items.BARRIER.getDefaultInstance());
                    category.entries.add(entry);
                    ((MarketAdminScreen)getGui()).addCategoryEntryButtons();
                    new CreateCategoryEntryC2S(category.categoryID, entry.serialize()).sendToServer();
                }));
                fContext1.add(new ContextMenuItem(Component.translatable("sdm.market.admin.entry.tag"), Icons.ADD, (d) -> {
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

            contextMenu.add(new ContextMenuItem(Component.translatable("sdm.market.admin.edit"), Icons.SETTINGS, (d) -> {
                editScreen();
            }));

            contextMenu.add(new ContextMenuItem(Component.translatable("sdm.market.delete"), Icons.REMOVE, (d) -> {
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

                list.add(Component.literal("Tag: ").append(tEntry.tagKey.toString()));

            } else if (configEntry.getIcon() instanceof ItemIcon icon) {
                itemStack = icon.getStack();
            }

            if (!itemStack.isEmpty())
                list.add(itemStack.getDisplayName());

            list.add(Component.literal("Min Price: " + (configEntry.minPrice > 0 ? SDMMarket.moneyString(configEntry.minPrice) : "Any Price")));
            list.add(Component.literal("Max Price: " + (configEntry.maxPrice > 0 ? SDMMarket.moneyString(configEntry.maxPrice) : "Any Price")));
        }
    }

    public void editScreen() {
        ConfigGroup group = new ConfigGroup("entry", b -> {
            openGui();

            if (b) {
                new EditCategoryEntryC2S(((MarketAdminScreen)getGui()).selectedCategory.categoryID, configEntry.entryID, configEntry.serialize()).sendToServer();
            }
        }).setNameKey("sidebar_button.sdm.market");


        ConfigGroup g = group.getOrCreateSubgroup("market").getOrCreateSubgroup("entry");
        configEntry.config(g);
        new EditConfigScreen(group).openGui();
        getGui().refreshWidgets();
    }

    private int i = 0;
    private int currentIndex = 0;

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
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
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        RGBA.create(0,0,0,255/3).drawRoundFill(graphics,x,y,w,h, 6);
    }
}
