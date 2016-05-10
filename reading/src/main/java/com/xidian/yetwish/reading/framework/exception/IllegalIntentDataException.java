package com.xidian.yetwish.reading.framework.exception;

/**
 * when send illegal data to intent will cause this exception
 * Created by Yetwish on 2016/5/10 0010.
 */
public class IllegalIntentDataException extends IllegalArgumentException {

    public IllegalIntentDataException(Class<?> reqClass, Class<?> actualClass) {
        super("IllegalArgument! request: " + reqClass.getName() + " ; actual: " + actualClass.getName());
    }

    public IllegalIntentDataException(Class<?> occurClass) {
        super("IllegalArgument! plz check the code of " + occurClass.getName());
    }

}
