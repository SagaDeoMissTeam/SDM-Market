package net.sixik.sdmmarket.data;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.StringConfig;

import java.util.ArrayList;
import java.util.List;

public class ClientData {

    public static String searchField = "";

    public static List<String> tags = new ArrayList<>();

    public static void config(ConfigGroup group){

        group.addList("tags", tags, new StringConfig(null), "");

    }

    public static boolean hawAnyTags(List<String> gTags){
        if(tags.isEmpty()) return true;

        for (String tag : tags) {
            if(gTags.contains(tag)) return true;
        }
        return false;
    }
}
