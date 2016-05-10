package com.xidian.yetwish.reading.framework.utils;

/**
 * all constants are define in this class.
 * Created by Yetwish on 2016/4/24 0024.
 */
public class SharedPreferencesUtils {

    private SharedPreferencesUtils() {
        /* can not be instantiated */
        throw new UnsupportedOperationException("can not be instantiated");
    }

    public static final String EXTRA_BOOK = "extraBook";
    public static final String EXTRA_NOTEBOOK = "extraNoteBook";
    public static final String EXTRA_NOTEBOOK_ID = "extraNoteBookId";
    public static final String EXTRA_NOTE = "extraNote";


    public static final int REQUEST_CODE_NOTE_EDIT = 0x01;

}
