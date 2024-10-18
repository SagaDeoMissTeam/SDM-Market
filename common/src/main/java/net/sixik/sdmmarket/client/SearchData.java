package net.sixik.sdmmarket.client;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SearchData {

    public static String name = "";
    public static long priceFrom = 0;
    public static long priceTo = 0;
    public static int countFrom = 0;
    public static int countTo = 0;
    public static List<UUID> selectedCategories = new ArrayList<UUID>();
    public static boolean isEncantable = false;
    public static boolean isNoDamaged = false;




    public static void reset(){
        name = "";
        priceFrom = 0;
        priceTo = 0;
        countFrom = 0;
        countTo = 0;
        isEncantable = false;
        isNoDamaged = false;
        selectedCategories.clear();
    }

}
