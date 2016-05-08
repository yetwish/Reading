package com.xidian.yetwish.reading.framework.database.generator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.xidian.yetwish.reading.framework.database.generator.Page;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PAGE.
*/
public class PageDao extends AbstractDao<Page, Long> {

    public static final String TABLENAME = "PAGE";

    /**
     * Properties of entity Page.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property PageId = new Property(0, Long.class, "pageId", true, "PAGE_ID");
        public final static Property ChapterId = new Property(1, Long.class, "chapterId", false, "CHAPTER_ID");
        public final static Property BookId = new Property(2, Long.class, "bookId", false, "BOOK_ID");
        public final static Property FirstCharPosition = new Property(3, Long.class, "firstCharPosition", false, "FIRST_CHAR_POSITION");
        public final static Property LastCharPosition = new Property(4, Long.class, "lastCharPosition", false, "LAST_CHAR_POSITION");
        public final static Property Path = new Property(5, String.class, "path", false, "PATH");
        public final static Property Content = new Property(6, String.class, "content", false, "CONTENT");
    };


    public PageDao(DaoConfig config) {
        super(config);
    }
    
    public PageDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PAGE' (" + //
                "'PAGE_ID' INTEGER PRIMARY KEY ," + // 0: pageId
                "'CHAPTER_ID' INTEGER," + // 1: chapterId
                "'BOOK_ID' INTEGER," + // 2: bookId
                "'FIRST_CHAR_POSITION' INTEGER," + // 3: firstCharPosition
                "'LAST_CHAR_POSITION' INTEGER," + // 4: lastCharPosition
                "'PATH' TEXT," + // 5: path
                "'CONTENT' TEXT);"); // 6: content
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PAGE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Page entity) {
        stmt.clearBindings();
 
        Long pageId = entity.getPageId();
        if (pageId != null) {
            stmt.bindLong(1, pageId);
        }
 
        Long chapterId = entity.getChapterId();
        if (chapterId != null) {
            stmt.bindLong(2, chapterId);
        }
 
        Long bookId = entity.getBookId();
        if (bookId != null) {
            stmt.bindLong(3, bookId);
        }
 
        Long firstCharPosition = entity.getFirstCharPosition();
        if (firstCharPosition != null) {
            stmt.bindLong(4, firstCharPosition);
        }
 
        Long lastCharPosition = entity.getLastCharPosition();
        if (lastCharPosition != null) {
            stmt.bindLong(5, lastCharPosition);
        }
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(6, path);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(7, content);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Page readEntity(Cursor cursor, int offset) {
        Page entity = new Page( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // pageId
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // chapterId
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // bookId
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // firstCharPosition
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // lastCharPosition
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // path
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // content
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Page entity, int offset) {
        entity.setPageId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setChapterId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setBookId(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setFirstCharPosition(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setLastCharPosition(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setPath(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setContent(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Page entity, long rowId) {
        entity.setPageId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Page entity) {
        if(entity != null) {
            return entity.getPageId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
