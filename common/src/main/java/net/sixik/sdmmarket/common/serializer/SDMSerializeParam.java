package net.sixik.sdmmarket.common.serializer;

public class SDMSerializeParam {

    public static final int SERIALIZE_STAGE_1 = 16;
    public static final int SERIALIZE_STAGE_2 = 17;

    public static final int SERIALIZE_ALL_ENTRIES = SERIALIZE_STAGE_1 | SERIALIZE_STAGE_2;


    public static final int SERIALIZE_PARAMS = 1;
    public static final int SERIALIZE_LIMIT = 2;
    public static final int SERIALIZE_CONDITIONS = 3;

    public static final int SERIALIZE_ENTRIES = 4;

    public static final int SERIALIZE_WITHOUT_ENTRIES = SERIALIZE_PARAMS | SERIALIZE_CONDITIONS | SERIALIZE_LIMIT;
    public static final int SERIALIZE_ALL = SERIALIZE_PARAMS | SERIALIZE_CONDITIONS | SERIALIZE_ENTRIES | SERIALIZE_LIMIT;
}
