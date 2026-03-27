package com.ehansih.vulnscanner.data.db

import android.content.Context
import androidx.room.*
import com.ehansih.vulnscanner.data.models.CveRecord
import com.ehansih.vulnscanner.data.models.Severity

@Database(entities = [CveRecord::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CveDatabase : RoomDatabase() {
    abstract fun cveDao(): CveDao

    companion object {
        @Volatile private var INSTANCE: CveDatabase? = null

        fun getInstance(context: Context): CveDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CveDatabase::class.java,
                    "cve_cache.db"
                ).build().also { INSTANCE = it }
            }
    }
}

@Dao
interface CveDao {
    @Query("SELECT * FROM cve_cache WHERE affectedProduct = :keyword")
    suspend fun getByProduct(keyword: String): List<CveRecord>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cves: List<CveRecord>)

    @Query("DELETE FROM cve_cache WHERE affectedProduct = :keyword")
    suspend fun clearForProduct(keyword: String)

    @Query("DELETE FROM cve_cache")
    suspend fun clearAll()
}

class Converters {
    @TypeConverter fun fromSeverity(s: Severity): String = s.name
    @TypeConverter fun toSeverity(s: String): Severity = Severity.valueOf(s)
}
