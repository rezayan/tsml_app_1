package ir.topcoders.pol.data.source.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;

import java.util.List;

import ir.topcoders.pol.data.model.Bridge;

@Dao
public interface RawDao {

    @RawQuery
    int rawQuery(SupportSQLiteQuery supportSQLiteQuery);
}
