package com.xidian.yetwish.reading.framework.database.generator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table GROUP_MEMBER.
*/
public class GroupMemberDao extends AbstractDao<GroupMember, Long> {

    public static final String TABLENAME = "GROUP_MEMBER";

    /**
     * Properties of entity GroupMember.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Cid = new Property(1, Long.class, "cid", false, "CID");
        public final static Property Uid = new Property(2, String.class, "uid", false, "UID");
        public final static Property UserName = new Property(3, String.class, "userName", false, "USER_NAME");
    };


    public GroupMemberDao(DaoConfig config) {
        super(config);
    }
    
    public GroupMemberDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'GROUP_MEMBER' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'CID' INTEGER," + // 1: cid
                "'UID' TEXT," + // 2: uid
                "'USER_NAME' TEXT);"); // 3: userName
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_GROUP_MEMBER_UID_CID ON GROUP_MEMBER" +
                " (UID,CID);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'GROUP_MEMBER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, GroupMember entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long cid = entity.getCid();
        if (cid != null) {
            stmt.bindLong(2, cid);
        }
 
        String uid = entity.getUid();
        if (uid != null) {
            stmt.bindString(3, uid);
        }
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(4, userName);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public GroupMember readEntity(Cursor cursor, int offset) {
        GroupMember entity = new GroupMember( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // cid
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // uid
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // userName
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, GroupMember entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCid(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setUid(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUserName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(GroupMember entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(GroupMember entity) {
        if(entity != null) {
            return entity.getId();
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
