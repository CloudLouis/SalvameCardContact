package com.salvame.cardcontact.db.dao;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import com.salvame.cardcontact.db.entity.ContactEntity;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class ContactDao_Impl implements ContactDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfContactEntity;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfContactEntity;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfContactEntity;

  public ContactDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfContactEntity = new EntityInsertionAdapter<ContactEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `contacts`(`c_id`,`name`,`email`,`phone_number`,`whatsapp`,`line`,`company`,`webpage`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ContactEntity value) {
        stmt.bindLong(1, value.getC_id());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getEmail() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getEmail());
        }
        if (value.getPhone_number() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getPhone_number());
        }
        if (value.getWhatsapp() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getWhatsapp());
        }
        if (value.getLine() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getLine());
        }
        if (value.getCompany() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getCompany());
        }
        if (value.getWebpage() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getWebpage());
        }
      }
    };
    this.__deletionAdapterOfContactEntity = new EntityDeletionOrUpdateAdapter<ContactEntity>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `contacts` WHERE `c_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ContactEntity value) {
        stmt.bindLong(1, value.getC_id());
      }
    };
    this.__updateAdapterOfContactEntity = new EntityDeletionOrUpdateAdapter<ContactEntity>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `contacts` SET `c_id` = ?,`name` = ?,`email` = ?,`phone_number` = ?,`whatsapp` = ?,`line` = ?,`company` = ?,`webpage` = ? WHERE `c_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ContactEntity value) {
        stmt.bindLong(1, value.getC_id());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getEmail() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getEmail());
        }
        if (value.getPhone_number() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getPhone_number());
        }
        if (value.getWhatsapp() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getWhatsapp());
        }
        if (value.getLine() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getLine());
        }
        if (value.getCompany() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getCompany());
        }
        if (value.getWebpage() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getWebpage());
        }
        stmt.bindLong(9, value.getC_id());
      }
    };
  }

  @Override
  public void insert(ContactEntity... classes) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfContactEntity.insert(classes);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(ContactEntity... classes) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfContactEntity.handleMultiple(classes);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(ContactEntity... classes) {
    __db.beginTransaction();
    try {
      __updateAdapterOfContactEntity.handleMultiple(classes);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public ContactEntity[] getContactEntity() {
    final String _sql = "SELECT * FROM contacts";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfCId = _cursor.getColumnIndexOrThrow("c_id");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfEmail = _cursor.getColumnIndexOrThrow("email");
      final int _cursorIndexOfPhoneNumber = _cursor.getColumnIndexOrThrow("phone_number");
      final int _cursorIndexOfWhatsapp = _cursor.getColumnIndexOrThrow("whatsapp");
      final int _cursorIndexOfLine = _cursor.getColumnIndexOrThrow("line");
      final int _cursorIndexOfCompany = _cursor.getColumnIndexOrThrow("company");
      final int _cursorIndexOfWebpage = _cursor.getColumnIndexOrThrow("webpage");
      final ContactEntity[] _result = new ContactEntity[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final ContactEntity _item;
        _item = new ContactEntity();
        final int _tmpC_id;
        _tmpC_id = _cursor.getInt(_cursorIndexOfCId);
        _item.setC_id(_tmpC_id);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item.setName(_tmpName);
        final String _tmpEmail;
        _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        _item.setEmail(_tmpEmail);
        final String _tmpPhone_number;
        _tmpPhone_number = _cursor.getString(_cursorIndexOfPhoneNumber);
        _item.setPhone_number(_tmpPhone_number);
        final String _tmpWhatsapp;
        _tmpWhatsapp = _cursor.getString(_cursorIndexOfWhatsapp);
        _item.setWhatsapp(_tmpWhatsapp);
        final String _tmpLine;
        _tmpLine = _cursor.getString(_cursorIndexOfLine);
        _item.setLine(_tmpLine);
        final String _tmpCompany;
        _tmpCompany = _cursor.getString(_cursorIndexOfCompany);
        _item.setCompany(_tmpCompany);
        final String _tmpWebpage;
        _tmpWebpage = _cursor.getString(_cursorIndexOfWebpage);
        _item.setWebpage(_tmpWebpage);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ContactEntity> getContactEntityList() {
    final String _sql = "SELECT * FROM contacts";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfCId = _cursor.getColumnIndexOrThrow("c_id");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfEmail = _cursor.getColumnIndexOrThrow("email");
      final int _cursorIndexOfPhoneNumber = _cursor.getColumnIndexOrThrow("phone_number");
      final int _cursorIndexOfWhatsapp = _cursor.getColumnIndexOrThrow("whatsapp");
      final int _cursorIndexOfLine = _cursor.getColumnIndexOrThrow("line");
      final int _cursorIndexOfCompany = _cursor.getColumnIndexOrThrow("company");
      final int _cursorIndexOfWebpage = _cursor.getColumnIndexOrThrow("webpage");
      final List<ContactEntity> _result = new ArrayList<ContactEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ContactEntity _item;
        _item = new ContactEntity();
        final int _tmpC_id;
        _tmpC_id = _cursor.getInt(_cursorIndexOfCId);
        _item.setC_id(_tmpC_id);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item.setName(_tmpName);
        final String _tmpEmail;
        _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        _item.setEmail(_tmpEmail);
        final String _tmpPhone_number;
        _tmpPhone_number = _cursor.getString(_cursorIndexOfPhoneNumber);
        _item.setPhone_number(_tmpPhone_number);
        final String _tmpWhatsapp;
        _tmpWhatsapp = _cursor.getString(_cursorIndexOfWhatsapp);
        _item.setWhatsapp(_tmpWhatsapp);
        final String _tmpLine;
        _tmpLine = _cursor.getString(_cursorIndexOfLine);
        _item.setLine(_tmpLine);
        final String _tmpCompany;
        _tmpCompany = _cursor.getString(_cursorIndexOfCompany);
        _item.setCompany(_tmpCompany);
        final String _tmpWebpage;
        _tmpWebpage = _cursor.getString(_cursorIndexOfWebpage);
        _item.setWebpage(_tmpWebpage);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public ContactEntity[] getContactEntityById(int id) {
    final String _sql = "SELECT * FROM contacts WHERE C_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfCId = _cursor.getColumnIndexOrThrow("c_id");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfEmail = _cursor.getColumnIndexOrThrow("email");
      final int _cursorIndexOfPhoneNumber = _cursor.getColumnIndexOrThrow("phone_number");
      final int _cursorIndexOfWhatsapp = _cursor.getColumnIndexOrThrow("whatsapp");
      final int _cursorIndexOfLine = _cursor.getColumnIndexOrThrow("line");
      final int _cursorIndexOfCompany = _cursor.getColumnIndexOrThrow("company");
      final int _cursorIndexOfWebpage = _cursor.getColumnIndexOrThrow("webpage");
      final ContactEntity[] _result = new ContactEntity[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final ContactEntity _item;
        _item = new ContactEntity();
        final int _tmpC_id;
        _tmpC_id = _cursor.getInt(_cursorIndexOfCId);
        _item.setC_id(_tmpC_id);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item.setName(_tmpName);
        final String _tmpEmail;
        _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        _item.setEmail(_tmpEmail);
        final String _tmpPhone_number;
        _tmpPhone_number = _cursor.getString(_cursorIndexOfPhoneNumber);
        _item.setPhone_number(_tmpPhone_number);
        final String _tmpWhatsapp;
        _tmpWhatsapp = _cursor.getString(_cursorIndexOfWhatsapp);
        _item.setWhatsapp(_tmpWhatsapp);
        final String _tmpLine;
        _tmpLine = _cursor.getString(_cursorIndexOfLine);
        _item.setLine(_tmpLine);
        final String _tmpCompany;
        _tmpCompany = _cursor.getString(_cursorIndexOfCompany);
        _item.setCompany(_tmpCompany);
        final String _tmpWebpage;
        _tmpWebpage = _cursor.getString(_cursorIndexOfWebpage);
        _item.setWebpage(_tmpWebpage);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public ContactEntity[] queryDatabase(SupportSQLiteQuery query) {
    final SupportSQLiteQuery _internalQuery = query;
    final Cursor _cursor = __db.query(_internalQuery);
    try {
      final ContactEntity[] _result = new ContactEntity[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final ContactEntity _item;
        _item = __entityCursorConverter_comSalvameCardcontactDbEntityContactEntity(_cursor);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
    }
  }

  private ContactEntity __entityCursorConverter_comSalvameCardcontactDbEntityContactEntity(Cursor cursor) {
    final ContactEntity _entity;
    final int _cursorIndexOfCId = cursor.getColumnIndex("c_id");
    final int _cursorIndexOfName = cursor.getColumnIndex("name");
    final int _cursorIndexOfEmail = cursor.getColumnIndex("email");
    final int _cursorIndexOfPhoneNumber = cursor.getColumnIndex("phone_number");
    final int _cursorIndexOfWhatsapp = cursor.getColumnIndex("whatsapp");
    final int _cursorIndexOfLine = cursor.getColumnIndex("line");
    final int _cursorIndexOfCompany = cursor.getColumnIndex("company");
    final int _cursorIndexOfWebpage = cursor.getColumnIndex("webpage");
    _entity = new ContactEntity();
    if (_cursorIndexOfCId != -1) {
      final int _tmpC_id;
      _tmpC_id = cursor.getInt(_cursorIndexOfCId);
      _entity.setC_id(_tmpC_id);
    }
    if (_cursorIndexOfName != -1) {
      final String _tmpName;
      _tmpName = cursor.getString(_cursorIndexOfName);
      _entity.setName(_tmpName);
    }
    if (_cursorIndexOfEmail != -1) {
      final String _tmpEmail;
      _tmpEmail = cursor.getString(_cursorIndexOfEmail);
      _entity.setEmail(_tmpEmail);
    }
    if (_cursorIndexOfPhoneNumber != -1) {
      final String _tmpPhone_number;
      _tmpPhone_number = cursor.getString(_cursorIndexOfPhoneNumber);
      _entity.setPhone_number(_tmpPhone_number);
    }
    if (_cursorIndexOfWhatsapp != -1) {
      final String _tmpWhatsapp;
      _tmpWhatsapp = cursor.getString(_cursorIndexOfWhatsapp);
      _entity.setWhatsapp(_tmpWhatsapp);
    }
    if (_cursorIndexOfLine != -1) {
      final String _tmpLine;
      _tmpLine = cursor.getString(_cursorIndexOfLine);
      _entity.setLine(_tmpLine);
    }
    if (_cursorIndexOfCompany != -1) {
      final String _tmpCompany;
      _tmpCompany = cursor.getString(_cursorIndexOfCompany);
      _entity.setCompany(_tmpCompany);
    }
    if (_cursorIndexOfWebpage != -1) {
      final String _tmpWebpage;
      _tmpWebpage = cursor.getString(_cursorIndexOfWebpage);
      _entity.setWebpage(_tmpWebpage);
    }
    return _entity;
  }
}
