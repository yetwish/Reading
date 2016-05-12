package com.xidian.yetwish.reading.framework.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;

import com.xidian.yetwish.reading.BaseApplication;
import com.xidian.yetwish.reading.framework.database.generator.DaoMaster;
import com.xidian.yetwish.reading.framework.database.generator.DaoSession;
import com.xidian.yetwish.reading.framework.database.manager.DbBookManager;
import com.xidian.yetwish.reading.framework.database.manager.DbBookmarkManager;
import com.xidian.yetwish.reading.framework.database.manager.DbChapterManager;
import com.xidian.yetwish.reading.framework.database.manager.DbNoteBookManager;
import com.xidian.yetwish.reading.framework.database.manager.DbNoteManager;
import com.xidian.yetwish.reading.framework.database.manager.DbPageManager;
import com.xidian.yetwish.reading.framework.utils.LogUtils;

/**
 * Created by Yetwish on 2016/4/21 0021.
 */
public class DatabaseManager {

    public static final String DATABASE_NAME = "Reading.db";

    private static DatabaseManager sInstance;

    private DbBookManager mDbBookManager;
    private DbNoteManager mDbNoteManager;
    private DbNoteBookManager mDbNoteBookManager;
    private DbChapterManager mDbChapterManager;
    private DbPageManager mDbPageManager;
    private DbBookmarkManager mDbBookmarkManager;

    private Handler mDBHandler;
    private HandlerThread mDBThread;

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private Context mContext;

    private DatabaseManager() {

    }

    public static DatabaseManager getsInstance() {
        if (sInstance == null) {
            synchronized (DatabaseManager.class) {
                if (sInstance == null)
                    sInstance = new DatabaseManager();
            }
        }
        return sInstance;
    }

    public DbBookManager getBookManager() {
        checkThread();
        if (mDbBookManager == null) {
            synchronized (DbBookManager.class) {
                if (mDbBookManager == null)
                    mDbBookManager = new DbBookManager(mDBHandler);
            }
        }
        return mDbBookManager;
    }


    public DbBookmarkManager getBookmarkManager() {
        checkThread();
        if (mDbBookmarkManager == null) {
            synchronized (DbBookmarkManager.class) {
                if (mDbBookmarkManager== null)
                    mDbBookmarkManager= new DbBookmarkManager(mDBHandler);
            }
        }
        return mDbBookmarkManager;
    }


    public DbNoteManager getNoteManager() {
        checkThread();
        if (mDbNoteManager == null) {
            synchronized (DbNoteManager.class) {
                if (mDbNoteManager == null)
                    mDbNoteManager = new DbNoteManager(mDBHandler);
            }
        }
        return mDbNoteManager;
    }

    public DbNoteBookManager getNoteBookManager() {
        checkThread();
        if (mDbNoteBookManager == null) {
            synchronized (DbNoteBookManager.class) {
                if (mDbNoteBookManager == null)
                    mDbNoteBookManager = new DbNoteBookManager(mDBHandler);
            }
        }
        return mDbNoteBookManager;
    }

    public DbChapterManager getChapterManager() {
        checkThread();
        if (mDbChapterManager == null) {
            synchronized (DbChapterManager.class) {
                if (mDbChapterManager == null)
                    mDbChapterManager = new DbChapterManager(mDBHandler);
            }
        }
        return mDbChapterManager;
    }

    public DbPageManager getPageManager() {
        checkThread();
        if (mDbPageManager == null) {
            synchronized (DbPageManager.class) {
                if (mDbPageManager == null)
                    mDbPageManager = new DbPageManager(mDBHandler);
            }
        }
        return mDbPageManager;
    }

    private void checkThread() {
        if (mDBThread == null) {
            mDBThread = new HandlerThread("db-Thread");
            mDBThread.start();
            mDBHandler = new Handler(mDBThread.getLooper());
        }
    }

    public DaoMaster getDaoMaster() {
        if (mDaoMaster == null) {
            DaoMaster.OpenHelper helper;
            helper = new DatabaseOpenHelper(mContext, DATABASE_NAME, null);
            SQLiteDatabase db = helper.getWritableDatabase();
            mDaoMaster = new DaoMaster(db);
        }
        return mDaoMaster;
    }

    public DaoSession getDaoSession() {
        if (mDaoSession == null) {
            mDaoSession = getDaoMaster().newSession();
        }
        return mDaoSession;
    }


    public void init(Context context) {
        mContext = context.getApplicationContext();
        getDaoMaster();
    }

    public void recycle() {
        if (mDaoSession != null) {
            mDaoSession.clear();
            mDaoSession = null;
        }
        if (mDaoMaster != null) {
            mDaoMaster.getDatabase().close();
            mDaoMaster = null;
        }
        if (mDBThread != null) {
            try {
                mDBThread.quit();
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            mDBHandler = null;
            mDBThread = null;
        }
        mDbBookManager = null;
        mDbNoteManager = null;
        mDbNoteBookManager = null;
        mDbChapterManager = null;
        mDbPageManager = null;
        mDbBookmarkManager = null;
    }

}
