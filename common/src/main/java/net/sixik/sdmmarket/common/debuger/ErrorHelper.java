package net.sixik.sdmmarket.common.debuger;

import net.sixik.sdmmarket.SDMMarket;

public class ErrorHelper {

    public static void errorCode(int i) {
        errorCode(i, new Object[0]);
    }

    public static void errorCode(int i, Object... additional) {
        if((i & ErrorCodes.E_ERROR_DELETE_ENTRY) != 0) {
            StringBuilder builder = new StringBuilder();
            for (Object s : additional) {
                builder.append(s);
            }


            SDMMarket.LOGGER.error("Error deleting entry: " + builder);
        }
    }
}
