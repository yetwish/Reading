package com.xidian.yetwish.reading.framework.database.manager;

import android.os.Handler;

import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.database.generator.Page;
import com.xidian.yetwish.reading.framework.database.generator.PageDao;
import com.xidian.yetwish.reading.framework.service.ApiCallback;
import com.xidian.yetwish.reading.framework.vo.reader.PageVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yetwish on 2016/4/25 0025.
 */
public class DbPageManager {

    public Handler mHandler;

    public DbPageManager(Handler handler) {
        this.mHandler = handler;
    }

    public void refresh(final List<PageVo> uploadList) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null && uploadList.size() > 0) {
                    List<Page> dbList = new ArrayList<Page>();
                    for (PageVo upload : uploadList) {
                        dbList.add(upload.convertToDb());
                    }
                    manager.getDaoSession().getPageDao().insertOrReplaceInTx(dbList);
                }
            }
        });
    }

    public void query(String ChapterId, ApiCallback<List<PageVo>> callback){

    }

    public void query(long position,ApiCallback<PageVo> callback){

    }


    public void delete(final String bookId) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null) {
                    manager.getDaoSession().getPageDao().queryBuilder()
                            .where(PageDao.Properties.BookId.eq(bookId)).buildDelete()
                            .executeDeleteWithoutDetachingEntities();
                }
            }
        });
    }

    public void deleteAll() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null)
                    manager.getDaoSession().getPageDao().deleteAll();
            }
        });
    }

}
