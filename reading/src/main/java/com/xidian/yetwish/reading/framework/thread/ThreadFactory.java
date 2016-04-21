package com.xidian.yetwish.reading.framework.thread;

/**
 * thread factory
 * Created by Yetwish on 2016/4/19 0019.
 */
public class ThreadFactory {

    public static BingoThread createThread(){
        return new BingoThread();
    }
}
