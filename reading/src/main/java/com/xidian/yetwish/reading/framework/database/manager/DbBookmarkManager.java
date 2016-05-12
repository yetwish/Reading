package com.xidian.yetwish.reading.framework.database.manager;

import android.os.Handler;

import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.database.generator.Bookmark;
import com.xidian.yetwish.reading.framework.database.generator.BookmarkDao;
import com.xidian.yetwish.reading.framework.eventbus.EventBusWrapper;
import com.xidian.yetwish.reading.framework.eventbus.event.EventGetBookmarkList;
import com.xidian.yetwish.reading.framework.vo.reader.BookmarkVo;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.WhereCondition;

/**
 * Created by Yetwish on 2016/5/12 0012.
 */
public class DbBookmarkManager {

    public Handler mHandler;

    public DbBookmarkManager(Handler handler) {
        this.mHandler = handler;
    }


    public void refresh(final ImmutableList<BookmarkVo> uploadList) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null) {
                    List<Bookmark> dbList = new ArrayList<Bookmark>(uploadList.size());
                    for (BookmarkVo upload : uploadList) {
                        dbList.add(upload.convertToDb());
                    }
                    manager.getDaoSession().getBookmarkDao().insertOrReplaceInTx(dbList);
                }
            }
        });
    }

    public void refresh(final BookmarkVo upload) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null) {
                    manager.getDaoSession().getBookmarkDao().insertOrReplaceInTx(upload.convertToDb());
                }
            }
        });
    }

    public BookmarkVo query(final long bookId, final long lastCharPosition) {
        BookmarkVo bookmark = null;
        DatabaseManager manager = DatabaseManager.getsInstance();
        if (manager != null) {
            BookmarkDao dao = manager.getDaoSession().getBookmarkDao();
            WhereCondition condition = dao.queryBuilder().and(BookmarkDao.Properties.BookId.eq(bookId),
                    BookmarkDao.Properties.LastCharPosition.eq(lastCharPosition));
            Query<Bookmark> query = dao.queryBuilder().where(condition).build();
            List<Bookmark> dbList = query.list();
            if (dbList != null && dbList.size() > 0)
                bookmark = new BookmarkVo(dbList.get(0));
        }
        return bookmark;
    }


    public void AsyncQuery(final long bookId) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                List<BookmarkVo> bookmarkList = new ArrayList<>();
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null) {
                    BookmarkDao dao = manager.getDaoSession().getBookmarkDao();
                    Query<Bookmark> query = dao.queryBuilder().where(BookmarkDao.Properties.BookId.eq(bookId)).build();
                    List<Bookmark> dbList = query.list();
                    if (dbList != null && dbList.size() != 0) {
                        for (Bookmark dbEntity : dbList) {
                            bookmarkList.add(new BookmarkVo(dbEntity));
                        }
                        EventBusWrapper.getDefault().post(new EventGetBookmarkList(ImmutableList.copyOf(bookmarkList)));
                    }
                }
            }
        });
    }

    public ImmutableList<BookmarkVo> queryByBookId(long bookId) {
        List<BookmarkVo> bookmarkList = new ArrayList<>();
        DatabaseManager manager = DatabaseManager.getsInstance();
        if (manager != null) {
            BookmarkDao dao = manager.getDaoSession().getBookmarkDao();
            Query<Bookmark> query = dao.queryBuilder().where(BookmarkDao.Properties.BookId.eq(bookId)).build();
            List<Bookmark> dbList = query.list();
            if (dbList != null && dbList.size() != 0)
                for (Bookmark dbEntity : dbList) {
                    bookmarkList.add(new BookmarkVo(dbEntity));
                }
        }
        return ImmutableList.copyOf(bookmarkList);
    }

    public void delete(final long bookmarkId) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null) {
                    manager.getDaoSession().getBookmarkDao().deleteByKey(bookmarkId);
                }
            }
        });
    }

    public void deleteByBookAndPosition(final long bookId, final long lastCharPosition) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null) {
                    BookmarkDao dao = manager.getDaoSession().getBookmarkDao();
                    WhereCondition condition = dao.queryBuilder().and(BookmarkDao.Properties.BookId.eq(bookId),
                            BookmarkDao.Properties.LastCharPosition.eq(lastCharPosition));
                    dao.queryBuilder().where(condition).buildDelete().executeDeleteWithoutDetachingEntities();
                }
            }
        });
    }

    public void deleteByBookId(final long bookId) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null) {
                    BookmarkDao dao = manager.getDaoSession().getBookmarkDao();
                    dao.queryBuilder().where(BookmarkDao.Properties.BookId.eq(bookId))
                            .buildDelete().executeDeleteWithoutDetachingEntities();
                }
            }
        });
    }
}
