package com.salvame.cardcontact.db.dao;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import com.salvame.cardcontact.ContactList;
import com.salvame.cardcontact.db.entity.ContactEntity;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contacts")
    public ContactEntity[] getContactEntity();

    @Query("SELECT * FROM contacts")
    List<ContactEntity> getContactEntityList();

    @Query("SELECT * FROM contacts WHERE C_id = :id")
    public ContactEntity[] getContactEntityById(int id);

    @RawQuery
    public ContactEntity[] queryDatabase(SupportSQLiteQuery query);

    @Insert
    void insert(ContactEntity... classes);

    @Update
    void update(ContactEntity... classes);

    @Delete
    void delete(ContactEntity... classes);
}

