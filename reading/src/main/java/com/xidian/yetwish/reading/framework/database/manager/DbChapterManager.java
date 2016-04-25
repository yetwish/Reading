package com.xidian.yetwish.reading.framework.database.manager;

import android.os.Handler;

import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.vo.reader.ChapterEntity;

import java.util.List;

/**
 *
 * Created by Yetwish on 2016/4/25 0025.
 */
public class DbChapterManager {

    public Handler mHandler;

    public DbChapterManager(Handler handler){
        this.mHandler = handler;
    }

    public void refresh(){

    }

    public List<ChapterEntity> query(){

        return null;
    }

    public void delete(){

    }

    public void deleteAll(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DatabaseManager.getInstance().getDaoSession().getChapterDao().deleteAll();
            }
        });
    }


}


