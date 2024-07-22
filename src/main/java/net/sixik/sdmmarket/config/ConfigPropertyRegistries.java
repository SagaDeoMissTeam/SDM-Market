package net.sixik.sdmmarket.config;

import net.sixik.sdmmarket.config.property.AbstractConfigProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface ConfigPropertyRegistries {

    HashMap<String, AbstractConfigProperty> PROPERTY_HASH_MAP = new HashMap<>();
    HashMap<String, List<String>> CAN_BE_USE = new HashMap<>();

    static AbstractConfigProperty register(AbstractConfigProperty configProperty, String... canBeUse) {
        AbstractConfigProperty e = register(configProperty);

        List<String> stringList = CAN_BE_USE.get(e.id);
        if(stringList == null) {
            stringList = new ArrayList<>();
        }
        stringList.addAll(List.of(canBeUse));

        CAN_BE_USE.put(e.id, stringList);
        return e;
    }

    static AbstractConfigProperty register(AbstractConfigProperty configProperty){
        if(!PROPERTY_HASH_MAP.containsKey(configProperty.id)) {
            PROPERTY_HASH_MAP.put(configProperty.id, configProperty);
            return configProperty;
        }

        throw new RuntimeException(configProperty.id + " already registered !");
    }
}
