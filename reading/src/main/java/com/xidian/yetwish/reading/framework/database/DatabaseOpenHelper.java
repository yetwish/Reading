package com.xidian.yetwish.reading.framework.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.xidian.yetwish.reading.framework.database.generator.DaoMaster;
import com.xidian.yetwish.reading.framework.utils.LogUtils;

/**
 * database open helper
 * Created by Yetwish on 2016/4/25 0025.
 */
public class DatabaseOpenHelper extends DaoMaster.OpenHelper {

    public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtils.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
        DaoMaster.dropAllTables(db, true);
        onCreate(db);
    }
}
