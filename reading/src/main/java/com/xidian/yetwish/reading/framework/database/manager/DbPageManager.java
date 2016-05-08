package com.xidian.yetwish.reading.framework.database.manager;

import android.os.Handler;

import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.framework.FWHelper;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.database.generator.Page;
import com.xidian.yetwish.reading.framework.database.generator.PageDao;
import com.xidian.yetwish.reading.framework.service.ApiCallback;
import com.xidian.yetwish.reading.framework.utils.LogUtils;
import com.xidian.yetwish.reading.framework.vo.reader.PageVo;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;

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

    public void queryByChapter(final long chapterId, final ApiCallback<ImmutableList<PageVo>> callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final List<PageVo> pageList = new ArrayList<>();
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null) {
                    PageDao dao = manager.getDaoSession().getPageDao();
                    Query<Page> query = dao.queryBuilder().where(PageDao.Properties.ChapterId.eq(chapterId))
                            .orderAsc(PageDao.Properties.FirstCharPosition).build();
                    List<Page> dbList = query.list();
                    if (dbList != null && dbList.size() != 0) {
                        for (Page page : dbList) {
                            pageList.add(new PageVo(page));
                        }
                    }
                }
                FWHelper.getInstance().getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onDataReceived(ImmutableList.copyOf(pageList));
                        }
                    }
                });
            }
        });
    }

    public void delete(final long bookId) {
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
