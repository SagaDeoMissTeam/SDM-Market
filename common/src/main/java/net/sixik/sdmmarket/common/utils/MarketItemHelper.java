package net.sixik.sdmmarket.common.utils;

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


    public static boolean sellItem(Player p, int amm, ItemStack item) {

        int totalamm = 0; //общее количество вещей в инвентаре
        for (int a = 0; a<p.getInventory().getContainerSize(); a++) { //считаем эти вещи
            if (isEquals(p.getInventory().getItem(a), item)) {
                totalamm += p.getInventory().getItem(a).getCount();
            }
        }
        if (totalamm==0) {
            return false;
        }
        if (totalamm<amm) {
            return false;
        }
        int ammountleft =amm;
        for (int a = 0; a<p.getInventory().getContainerSize(); a++) {
            if (ammountleft==0){return true;}
            if (p.getInventory().getItem(a)==null) continue;

            if(isEquals(p.getInventory().getItem(a), item)) {
                if (p.getInventory().getItem(a).getCount()<ammountleft) {
                    ammountleft-=p.getInventory().getItem(a).getCount();
                    p.getInventory().setItem(a, ItemStack.EMPTY);
                }
                if (p.getInventory().getItem(a)!=null&&p.getInventory().getItem(a).getCount()==ammountleft) {
                    p.getInventory().setItem(a, ItemStack.EMPTY);
                    return true;
                }

                if (p.getInventory().getItem(a).getCount()>ammountleft&&p.getInventory().getItem(a)!=null) {
                    p.getInventory().getItem(a).setCount(p.getInventory().getItem(a).getCount()-ammountleft);
                    return true;
                }
            }
        }
        return false;
    }
}
