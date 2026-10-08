package com.shivani.fieldops.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "work_orders")
data class WorkOrderEntity(
    @PrimaryKey val id: String,
    val title: String,
    val site: String,
    val priority: String,
    val status: String,
    val updatedAt: String,
)

@Dao
interface WorkOrderDao {
    @Query("SELECT * FROM work_orders ORDER BY CASE priority WHEN 'HIGH' THEN 0 WHEN 'MEDIUM' THEN 1 ELSE 2 END, updatedAt DESC")
    fun observeAll(): Flow<List<WorkOrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<WorkOrderEntity>)
}

@Database(entities = [WorkOrderEntity::class], version = 1, exportSchema = true)
abstract class FieldOpsDatabase : RoomDatabase() {
    abstract fun workOrderDao(): WorkOrderDao

    companion object {
        fun create(context: Context): FieldOpsDatabase = Room.databaseBuilder(
            context,
            FieldOpsDatabase::class.java,
            "field-ops.db",
        ).build()
    }
}
