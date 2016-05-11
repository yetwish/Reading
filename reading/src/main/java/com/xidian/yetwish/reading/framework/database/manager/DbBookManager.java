package com.xidian.yetwish.reading.framework.database.manager;

import android.os.Handler;

import com.google.common.collect.ImmutableList;
import com.xidian.yetwish.reading.framework.database.DatabaseManager;
import com.xidian.yetwish.reading.framework.database.generator.Book;
import com.xidian.yetwish.reading.framework.database.generator.BookDao;
import com.xidian.yetwish.reading.framework.eventbus.EventBusWrapper;
import com.xidian.yetwish.reading.framework.service.ApiCallback;
import com.xidian.yetwish.reading.framework.utils.LogUtils;
import com.xidian.yetwish.reading.framework.vo.BookVo;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;

/**
 * Created by Yetwish on 2016/4/25 0025.
 */
public class DbBookManager {

    public Handler mHandler;

    public DbBookManager(Handler handler) {
        this.mHandler = handler;
    }


    public void refresh(final ImmutableList<BookVo> uploadList) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null) {
                    List<Book> dbList = new ArrayList<Book>(uploadList.size());
                    for (BookVo upload : uploadList) {
                        dbList.add(upload.convertToDb());
                    }
                    manager.getDaoSession().getBookDao().insertOrReplaceInTx(dbList);
                }
            }
        });
    }

    public void refresh(final BookVo upload) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null) {
                    manager.getDaoSession().getBookDao().insertOrReplaceInTx(upload.convertToDb());
                }
            }
        });
    }

    public BookVo query(final long bookId) {
        BookVo book = null;
        DatabaseManager manager = DatabaseManager.getsInstance();
        if (manager != null) {
            BookDao dao = manager.getDaoSession().getBookDao();
            Book dbEntity = dao.load(bookId);
            if (dbEntity != null) {
                book = new BookVo(dbEntity);
            }
        }
        return book;
    }


    public ImmutableList<BookVo> queryAll() {
        List<BookVo> bookList = new ArrayList<>();
        DatabaseManager manager = DatabaseManager.getsInstance();
        if (manager != null) {
            BookDao dao = manager.getDaoSession().getBookDao();
            List<Book> dbList = dao.loadAll();
            for (Book dbEntity : dbList) {
                bookList.add(new BookVo(dbEntity));
            }
        }
        return ImmutableList.copyOf(bookList);
    }

    /**
     * 查询在读列表
     *
     * @return
     */
    public ImmutableList<BookVo> queryReading() {
        List<BookVo> bookList = new ArrayList<>();
        DatabaseManager manager = DatabaseManager.getsInstance();
        if (manager != null) {
            BookDao dao = manager.getDaoSession().getBookDao();
            Query<Book> query = dao.queryBuilder().where(BookDao.Properties.Progress.notEq(100)).build();
            List<Book> dbList = query.list();
            for (Book dbEntity : dbList) {
                bookList.add(new BookVo(dbEntity));
            }
        }
        return ImmutableList.copyOf(bookList);
    }

    /**
     * 查询 已读列表
     *
     * @return
     */
    public ImmutableList<BookVo> queryRead() {
        List<BookVo> bookList = new ArrayList<>();
        DatabaseManager manager = DatabaseManager.getsInstance();
        if (manager != null) {
            BookDao dao = manager.getDaoSession().getBookDao();
            Query<Book> query = dao.queryBuilder().where(BookDao.Properties.Progress.eq(100)).build();
            List<Book> dbList = query.list();
            for (Book dbEntity : dbList) {
                bookList.add(new BookVo(dbEntity));
            }
        }
        return ImmutableList.copyOf(bookList);
    }


    public void delete(final long bookId) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null) {
                    manager.getDaoSession().getBookDao().deleteByKey(bookId);
                }
            }
        });
    }

    public void deleteAll() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                DatabaseManager manager = DatabaseManager.getsInstance();
                if (manager != null) {
                    manager.getDaoSession().getBookDao().deleteAll();
                }
            }
        });
    }

}
