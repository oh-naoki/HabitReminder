package data

import androidx.room.Database
import androidx.room.RoomDatabase
import data.dao.HabitDao
import data.entity.Habit
import data.entity.Reminder

@Database(entities = [Habit::class, Reminder::class], version = 1)
abstract class AppDatabase : RoomDatabase(), DB {
    abstract fun habitDao(): HabitDao
    override fun clearAllTables() {
        super.clearAllTables()
    }
}

// FIXME: Added a hack to resolve below issue:
// Class 'AppDatabase_Impl' is not abstract and does not implement abstract base class member 'clearAllTables'.
interface DB {
    fun clearAllTables() {}
}
