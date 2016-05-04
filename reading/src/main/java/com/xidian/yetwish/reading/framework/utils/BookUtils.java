package com.xidian.yetwish.reading.framework.utils;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Yetwish on 2016/4/23 0023.
 */
public class BookUtils {

    private final static Format DATE_FORMAT = new SimpleDateFormat("MMddHHmmssS");

    private final static FieldPosition HELPER_POSITION = new FieldPosition(0);

    private final static NumberFormat NUMBER_FORMAT = new DecimalFormat("0000");

    private static int seq = 0;

    private static final int MAX = 9999;

    public static synchronized String generateSequenceId() {

        Calendar currentTime = Calendar.getInstance();

        StringBuffer sb = new StringBuffer();

        DATE_FORMAT.format(currentTime.getTime(), sb, HELPER_POSITION);

        NUMBER_FORMAT.format(seq, sb, HELPER_POSITION);

        if(seq == MAX){
            seq = 0 ;
        }else {
            seq ++;
        }
        return sb.toString();
    }


}
