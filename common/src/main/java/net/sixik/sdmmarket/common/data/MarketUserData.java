package net.sixik.sdmmarket.common.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.sixik.sdmmarket.common.market.user.MarketUserCategory;
import net.sixik.sdmmarket.common.market.user.MarketUserEntryList;
import net.sixik.sdmmarket.common.utils.INBTSerialize;
import net.sixik.sdmmarket.common.utils.NBTUtils;

import java.util.ArrayList;
import java.util.List;

public class MarketUserData implements INBTSerialize {


    public List<MarketUserCategory> categories = new ArrayList<>();


    public void copyFromOverwrite(MarketUserData other) {
        this.categories.clear();
        this.categories.addAll(other.categories);
    }

    public void copyFrom(MarketUserData other) {
        // Если в текущих данных нет категорий, просто копируем все категории из other
        if (this.categories.isEmpty()) {
            this.categories.addAll(other.categories);
            return;
        }

        // Идем по текущим категориям и обновляем их или удаляем, если нет записей
        var iterator = this.categories.iterator();
        while (iterator.hasNext()) {
            MarketUserCategory category = iterator.next();

            // Поиск категории в другой UserData
            boolean found = false;
            for (MarketUserCategory otherCategory : other.categories) {
                if (category.categoryID.equals(otherCategory.categoryID)) {
                    found = true;
                    // Обновляем имя категории
                    category.categoryName = otherCategory.categoryName;

                    // Если записи пусты, копируем все записи из другой категории
                    if (category.entries.isEmpty()) {
                        category.entries.addAll(otherCategory.entries);
                    } else {
                        // Проверяем и добавляем только отсутствующие записи
                        for (MarketUserEntryList otherEntry : otherCategory.entries) {
                            boolean entryExists = false;
                            for (MarketUserEntryList currentEntry : category.entries) {
                                if (ItemStack.isSameItem(otherEntry.itemStack, currentEntry.itemStack)) {
                                    entryExists = true;
                                    break;
                                }
                            }
                            if (!entryExists) {
                                category.entries.add(otherEntry);
                            }
                        }
                    }
                    break;
                }
            }

            // Удаляем категорию, если она не найдена и пустая
            if (!found && category.entries.isEmpty()) {
                iterator.remove();
            }
        }

        // Теперь добавляем новые категории, которых нет в текущих данных
        for (MarketUserCategory otherCategory : other.categories) {
            boolean found = false;
            for (MarketUserCategory currentCategory : this.categories) {
                if (otherCategory.categoryID.equals(currentCategory.categoryID)) {
                    found = true;
                    break;
                }
            }

            // Если категория не найдена, добавляем её
            if (!found) {
                this.categories.add(otherCategory);
            }
        }
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag nbt = new CompoundTag();
        ListTag list = NBTUtils.serializeList(categories);
        nbt.put("categories", list);
        return nbt;
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        if (nbt.contains("categories")) {
            ListTag d1 = (ListTag) nbt.get("categories");
            categories.clear();
            for (Tag entryTag : d1) {
                MarketUserCategory category = new MarketUserCategory();
                category.deserialize((CompoundTag) entryTag);
                categories.add(category);
            }
        }
    }
}
