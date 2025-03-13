package net.sixik.sdmmarket.common.utils;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class MarketItemHelper {

    public static boolean isEquals(ItemStack stack1, ItemStack stack2) {
        if(stack1 == null || stack2 == null) return false;

        boolean flag = stack2.hasTag();
        if(flag && ItemStack.matches(stack1, stack2)) {
           return true;
        } else return !flag && ItemStack.isSameItem(stack1, stack2) || ItemStack.matches(stack1, stack2);
    }

    public static int countItems(Player p, ItemStack item) {
        boolean flag = item.hasTag();

        int totalamm = 0; //общее количество вещей в инвентаре
        for (int a = 0; a<p.getInventory().getContainerSize(); a++) { //считаем эти вещи
            if (!p.getInventory().getItem(a).isEmpty()){
                /*весь ItemStack можно описать тремя параметрами. item.getData, item.getItemMeta и item.getAmmaount.
                 *При item.equas(item2)ammount тоже сравнивается, поэтому видим такое сравнение
                 */
                if(flag && ItemStack.matches(p.getInventory().getItem(a), item)) {
                    totalamm += p.getInventory().getItem(a).getCount();
                } else if(!flag && ItemStack.isSameItem(p.getInventory().getItem(a), item) || ItemStack.matches(p.getInventory().getItem(a), item)){
                    totalamm += p.getInventory().getItem(a).getCount();
                }
            }
        }

        return totalamm;
    }


    public static boolean sellItem(Player p, int amm, ItemStack item, boolean ignoreNBT) {
        int totalAmount = 0; // Общее количество предметов в инвентаре
        Inventory inventory = p.getInventory();

        // Подсчёт предметов в инвентаре
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);

            if (!stack.isEmpty() && (ignoreNBT ? ItemStack.isSameItem(stack, item) : ItemStack.matches(stack, item))) {
                totalAmount += stack.getCount();
            }
        }

        // Если предметов недостаточно — выход
        if (totalAmount < amm) {
            return false;
        }

        int amountLeft = amm; // Сколько ещё нужно удалить

        // Удаление предметов
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (amountLeft == 0) break; // Если всё удалили — выход

            ItemStack stack = inventory.getItem(i);

            if (!stack.isEmpty() && (ignoreNBT ? ItemStack.isSameItem(stack, item) : ItemStack.matches(stack, item))) {
                int stackCount = stack.getCount();

                if (stackCount <= amountLeft) {
                    amountLeft -= stackCount;
                    inventory.setItem(i, ItemStack.EMPTY);
                } else {
                    stack.setCount(stackCount - amountLeft);
                    return true;
                }
            }
        }

        return amountLeft != amm;
    }
}
