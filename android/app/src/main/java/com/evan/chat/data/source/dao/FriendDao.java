package com.evan.chat.data.source.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.evan.chat.data.source.Friend.model.Friend;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "FRIEND".
*/
public class FriendDao extends AbstractDao<Friend, Long> {

    public static final String TABLENAME = "FRIEND";

    /**
     * Properties of entity Friend.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Account = new Property(1, String.class, "account", false, "ACCOUNT");
        public final static Property Nickname = new Property(2, String.class, "nickname", false, "NICKNAME");
        public final static Property Email = new Property(3, String.class, "email", false, "EMAIL");
        public final static Property Relationship = new Property(4, String.class, "relationship", false, "RELATIONSHIP");
        public final static Property Classification = new Property(5, String.class, "classification", false, "CLASSIFICATION");
        public final static Property Profile = new Property(6, String.class, "profile", false, "PROFILE");
    }


    public FriendDao(DaoConfig config) {
        super(config);
    }
    
    public FriendDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"FRIEND\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"ACCOUNT\" TEXT," + // 1: account
                "\"NICKNAME\" TEXT," + // 2: nickname
                "\"EMAIL\" TEXT," + // 3: email
                "\"RELATIONSHIP\" TEXT," + // 4: relationship
                "\"CLASSIFICATION\" TEXT," + // 5: classification
                "\"PROFILE\" TEXT);"); // 6: profile
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"FRIEND\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Friend entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String account = entity.getAccount();
        if (account != null) {
            stmt.bindString(2, account);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(3, nickname);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(4, email);
        }
 
        String relationship = entity.getRelationship();
        if (relationship != null) {
            stmt.bindString(5, relationship);
        }
 
        String classification = entity.getClassification();
        if (classification != null) {
            stmt.bindString(6, classification);
        }
 
        String profile = entity.getProfile();
        if (profile != null) {
            stmt.bindString(7, profile);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Friend entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String account = entity.getAccount();
        if (account != null) {
            stmt.bindString(2, account);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(3, nickname);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(4, email);
        }
 
        String relationship = entity.getRelationship();
        if (relationship != null) {
            stmt.bindString(5, relationship);
        }
 
        String classification = entity.getClassification();
        if (classification != null) {
            stmt.bindString(6, classification);
        }
 
        String profile = entity.getProfile();
        if (profile != null) {
            stmt.bindString(7, profile);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Friend readEntity(Cursor cursor, int offset) {
        Friend entity = new Friend( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // account
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // nickname
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // email
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // relationship
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // classification
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // profile
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Friend entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAccount(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNickname(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setEmail(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setRelationship(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setClassification(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setProfile(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Friend entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Friend entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Friend entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
