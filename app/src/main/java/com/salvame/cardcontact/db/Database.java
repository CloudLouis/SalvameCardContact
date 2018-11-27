package com.salvame.cardcontact.db;
import android.arch.persistence.room.RoomDatabase;

import com.salvame.cardcontact.db.dao.ContactDao;
import com.salvame.cardcontact.db.entity.ContactEntity;

@android.arch.persistence.room.Database(entities = {ContactEntity.class}, version = 3, exportSchema = false)
public abstract class Database extends RoomDatabase{
    private static Database INSTANCE;
    public abstract ContactDao getContactDao();
}
