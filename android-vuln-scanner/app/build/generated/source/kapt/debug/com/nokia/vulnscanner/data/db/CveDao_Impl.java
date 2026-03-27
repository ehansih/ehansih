package com.nokia.vulnscanner.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.nokia.vulnscanner.data.models.CveRecord;
import com.nokia.vulnscanner.data.models.Severity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CveDao_Impl implements CveDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CveRecord> __insertionAdapterOfCveRecord;

  private final Converters __converters = new Converters();

  private final SharedSQLiteStatement __preparedStmtOfClearForProduct;

  private final SharedSQLiteStatement __preparedStmtOfClearAll;

  public CveDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCveRecord = new EntityInsertionAdapter<CveRecord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `cve_cache` (`cveId`,`description`,`cvssScore`,`severity`,`publishedDate`,`affectedProduct`,`references`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CveRecord entity) {
        if (entity.getCveId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getCveId());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getDescription());
        }
        statement.bindDouble(3, entity.getCvssScore());
        final String _tmp = __converters.fromSeverity(entity.getSeverity());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        if (entity.getPublishedDate() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPublishedDate());
        }
        if (entity.getAffectedProduct() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getAffectedProduct());
        }
        if (entity.getReferences() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getReferences());
        }
      }
    };
    this.__preparedStmtOfClearForProduct = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM cve_cache WHERE affectedProduct = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM cve_cache";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<CveRecord> cves,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCveRecord.insert(cves);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearForProduct(final String keyword,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearForProduct.acquire();
        int _argIndex = 1;
        if (keyword == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, keyword);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearForProduct.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getByProduct(final String keyword,
      final Continuation<? super List<CveRecord>> $completion) {
    final String _sql = "SELECT * FROM cve_cache WHERE affectedProduct = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (keyword == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, keyword);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CveRecord>>() {
      @Override
      @NonNull
      public List<CveRecord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCveId = CursorUtil.getColumnIndexOrThrow(_cursor, "cveId");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCvssScore = CursorUtil.getColumnIndexOrThrow(_cursor, "cvssScore");
          final int _cursorIndexOfSeverity = CursorUtil.getColumnIndexOrThrow(_cursor, "severity");
          final int _cursorIndexOfPublishedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "publishedDate");
          final int _cursorIndexOfAffectedProduct = CursorUtil.getColumnIndexOrThrow(_cursor, "affectedProduct");
          final int _cursorIndexOfReferences = CursorUtil.getColumnIndexOrThrow(_cursor, "references");
          final List<CveRecord> _result = new ArrayList<CveRecord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CveRecord _item;
            final String _tmpCveId;
            if (_cursor.isNull(_cursorIndexOfCveId)) {
              _tmpCveId = null;
            } else {
              _tmpCveId = _cursor.getString(_cursorIndexOfCveId);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final double _tmpCvssScore;
            _tmpCvssScore = _cursor.getDouble(_cursorIndexOfCvssScore);
            final Severity _tmpSeverity;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfSeverity)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfSeverity);
            }
            _tmpSeverity = __converters.toSeverity(_tmp);
            final String _tmpPublishedDate;
            if (_cursor.isNull(_cursorIndexOfPublishedDate)) {
              _tmpPublishedDate = null;
            } else {
              _tmpPublishedDate = _cursor.getString(_cursorIndexOfPublishedDate);
            }
            final String _tmpAffectedProduct;
            if (_cursor.isNull(_cursorIndexOfAffectedProduct)) {
              _tmpAffectedProduct = null;
            } else {
              _tmpAffectedProduct = _cursor.getString(_cursorIndexOfAffectedProduct);
            }
            final String _tmpReferences;
            if (_cursor.isNull(_cursorIndexOfReferences)) {
              _tmpReferences = null;
            } else {
              _tmpReferences = _cursor.getString(_cursorIndexOfReferences);
            }
            _item = new CveRecord(_tmpCveId,_tmpDescription,_tmpCvssScore,_tmpSeverity,_tmpPublishedDate,_tmpAffectedProduct,_tmpReferences);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
