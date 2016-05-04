package com.xidian.yetwish.reading.framework.database.manager;

import android.os.Handler;

import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.framework.FWHelper;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.database.generator.Chapter;
import com.xidian.yetwish.reading.framework.database.generator.ChapterDao;
import com.xidian.yetwish.reading.framework.service.ApiCallback;
import com.xidian.yetwish.reading.framework.vo.reader.ChapterVo;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;

/**
 * the manager of chapter db table. supply several methods to operate the db.
 * Created by Yetwish on 2016/4/25 0025.
 */
public class DbChapterManager {

    public Handler mHandler;

    public DbChapterManager(Handler handler) {
        this.mHandler = handler;
    }

    public void refresh(final List<ChapterVo> uploadList) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null && uploadList.size() > 0) {
                    List<Chapter> dbList = new ArrayList<Chapter>();
                    for (ChapterVo upload : uploadList) {
                        dbList.add(upload.convertToDb());
                    }
                    manager.getDaoSession().getChapterDao().insertOrReplaceInTx(dbList);
                }
            }
        });
    }

    public void refresh(final ChapterVo upload) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null) {
                    manager.getDaoSession().getChapterDao().insertOrReplace(upload.convertToDb());
                }
            }
        });
    }

    public void query(String bookId, final ApiCallback<ImmutableList<ChapterVo>> callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                List<ChapterVo> list = null;
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null) {
                    ChapterDao dao = manager.getDaoSession().getChapterDao();
                    Query<Chapter> query = dao.queryBuilder()
                            .orderAsc(ChapterDao.Properties.FirstCharPosition).build();
                    List<Chapter> dbList = query.list();
                    if (dbList != null && dbList.size() > 0) {
                        list = new ArrayList<ChapterVo>();
                        for (Chapter chapter : dbList) {
                            list.add(new ChapterVo(chapter));
                        }
                    }
                }
                final ImmutableList<ChapterVo> chapterList = ImmutableList.copyOf(list);
                FWHelper.getInstance().getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDataReceived(chapterList);
                    }
                });
            }
        });
    }

    public void query(final long position,ApiCallback<ChapterVo> callback){

    }


    public void delete(final String bookId) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null) {
                    manager.getDaoSession().getChapterDao().queryBuilder()
                            .where(ChapterDao.Properties.BookId.eq(bookId)).buildDelete()
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
                    manager.getsInstance().getDaoSession().getChapterDao().deleteAll();
            }
        });
    }


}


