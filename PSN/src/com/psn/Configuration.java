package com.psn;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface Configuration {

    public static final String APPLICATION_NAME = "PSN";

    public static final String VERSION = "1.0.0";

    public static final String VENDOR_ID = "1234";

    public static final String PRODUCT_ID = "ffff";

    public static final String FILE_ENCODED_EXT = "encx";

    public static final String ALGORITHM = "5050";

    public static final String UI_STAGE_TITLE_LABEL = "File da processare";

    public static final String UI_MAIN_LABEL_TEXT = "Trascina qui i files";

    public static final String UI_TABLE_COLUMN_FILE = "File";

    public static final String UI_TABLE_COLUMN_STATUS = "Stato";

    public static final String UI_DELETE_BUTTON = "Rimuovi";

    public static final String UI_ENCODE_BUTTON = "Codifica";

    public static final String UI_DECODE_BUTTON = "Decodifica";
    
    public static final Set<Integer> INDEX_SET = new HashSet<>(Arrays.asList(1, 3, 5, 7, 9));

}
