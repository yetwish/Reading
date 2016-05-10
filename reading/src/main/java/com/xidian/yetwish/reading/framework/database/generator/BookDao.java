package com.xidian.yetwish.reading.framework.database.generator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.xidian.yetwish.reading.framework.database.generator.Book;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table BOOK.
*/
public class BookDao extends AbstractDao<Book, Long> {

    public static final String TABLENAME = "BOOK";

    /**
     * Properties of entity Book.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property BookId = new Property(0, Long.class, "bookId", true, "BOOK_ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Author = new Property(2, String.class, "author", false, "AUTHOR");
        public final static Property Language = new Property(3, String.class, "language", false, "LANGUAGE");
        public final static Property Progress = new Property(4, Float.class, "progress", false, "PROGRESS");
        public final static Property IconPath = new Property(5, String.class, "iconPath", false, "ICON_PATH");
        public final static Property IconResId = new Property(6, Integer.class, "iconResId", false, "ICON_RES_ID");
        public final static Property Path = new Property(7, String.class, "path", false, "PATH");
        public final static Property CharNumber = new Property(8, Long.class, "charNumber", false, "CHAR_NUMBER");
    };


    public BookDao(DaoConfig config) {
        super(config);
    }
    
    public BookDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'BOOK' (" + //
                "'BOOK_ID' INTEGER PRIMARY KEY ," + // 0: bookId
                "'NAME' TEXT," + // 1: name
                "'AUTHOR' TEXT," + // 2: author
                "'LANGUAGE' TEXT," + // 3: language
                "'PROGRESS' REAL," + // 4: progress
                "'ICON_PATH' TEXT," + // 5: iconPath
                "'ICON_RES_ID' INTEGER," + // 6: iconResId
                "'PATH' TEXT," + // 7: path
                "'CHAR_NUMBER' INTEGER);"); // 8: charNumber
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'BOOK'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Book entity) {
        stmt.clearBindings();
 
        Long bookId = entity.getBookId();
        if (bookId != null) {
            stmt.bindLong(1, bookId);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String author = entity.getAuthor();
        if (author != null) {
            stmt.bindString(3, author);
        }
 
        String language = entity.getLanguage();
        if (language != null) {
            stmt.bindString(4, language);
        }
 
        Float progress = entity.getProgress();
        if (progress != null) {
            stmt.bindDouble(5, progress);
        }
 
        String iconPath = entity.getIconPath();
        if (iconPath != null) {
            stmt.bindString(6, iconPath);
        }
 
        Integer iconResId = entity.getIconResId();
        if (iconResId != null) {
            stmt.bindLong(7, iconResId);
        }
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(8, path);
        }
 
        Long charNumber = entity.getCharNumber();
        if (charNumber != null) {
            stmt.bindLong(9, charNumber);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Book readEntity(Cursor cursor, int offset) {
        Book entity = new Book( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // bookId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // author
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // language
            cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4), // progress
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // iconPath
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // iconResId
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // path
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8) // charNumber
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Book entity, int offset) {
        entity.setBookId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAuthor(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setLanguage(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setProgress(cursor.isNull(offset + 4) ? null : cursor.getFloat(offset + 4));
        entity.setIconPath(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setIconResId(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setPath(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setCharNumber(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Book entity, long rowId) {
        entity.setBookId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Book entity) {
        if(entity != null) {
            return entity.getBookId();
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
