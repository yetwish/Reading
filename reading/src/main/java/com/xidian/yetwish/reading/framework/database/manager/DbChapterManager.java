package com.xidian.yetwish.reading.framework.database.manager;

import android.os.Handler;

import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.framework.FWHelper;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.database.generator.Chapter;
import com.xidian.yetwish.reading.framework.database.generator.ChapterDao;
import com.xidian.yetwish.reading.framework.service.ApiCallback;
import com.xidian.yetwish.reading.framework.utils.LogUtils;
import com.xidian.yetwish.reading.framework.vo.reader.ChapterVo;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.WhereCondition;

/**
 * the manager of chapter db table. supply several methods to operate the db.
 * Created by Yetwish on 2016/4/25 0025.
 */
public class DbChapterManager {

    public Handler mHandler;

    public DbChapterManager(Handler handler) {
        this.mHandler = handler;
    }

    public void refresh(final ImmutableList<ChapterVo> uploadList) {
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

    public void queryByBookId(final long bookId, final ApiCallback<ImmutableList<ChapterVo>> callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final List<ChapterVo> list = new ArrayList<ChapterVo>();
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null) {
                    ChapterDao dao = manager.getDaoSession().getChapterDao();
                    Query<Chapter> query = dao.queryBuilder()
                            .where(ChapterDao.Properties.BookId.eq(bookId))
                            .orderAsc(ChapterDao.Properties.FirstCharPosition).build();
                    List<Chapter> dbList = query.list();
                    if (dbList != null && dbList.size() > 0) {
                        for (Chapter chapter : dbList) {
                            list.add(new ChapterVo(chapter));
                        }
                    }
                }
                FWHelper.getInstance().getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null)
                            callback.onDataReceived(ImmutableList.copyOf(list));
                    }
                });
            }
        });
    }

    public ChapterVo query(final long chapterId) {
        ChapterVo chapter = null;
        DatabaseManager manager = DatabaseManager.getsInstance();
        if (manager != null) {
            ChapterDao dao = manager.getDaoSession().getChapterDao();
            Chapter dbEntity = dao.load(chapterId);
            if (dbEntity != null)
                chapter = new ChapterVo(dbEntity);
        }
        return chapter;
    }

    public ChapterVo queryByPosition(long position) {
        ChapterVo chapter = null;
        DatabaseManager manager = DatabaseManager.getsInstance();
        if (manager != null) {
            ChapterDao dao = manager.getDaoSession().getChapterDao();
            WhereCondition condition = dao.queryBuilder().and(ChapterDao.Properties.FirstCharPosition.le(position),
                    ChapterDao.Properties.LastCharPosition.gt(position));
            Query<Chapter> query = dao.queryBuilder().where(condition).build();
            List<Chapter> dbList = query.list();
            if (dbList != null && dbList.size() != 0) {
//                LogUtils.w("chapter dbList size " + dbList.size());
                chapter = new ChapterVo(dbList.get(0));
            }
        }
        return chapter;

    }


    public void delete(final long bookId) {
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


