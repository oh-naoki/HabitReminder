package data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import data.entity.Habit
import data.entity.HabitWithReminders
import data.entity.Reminder

@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM Habit WHERE habitId = :habitId")
    suspend fun getHabitById(habitId: Int): Habit?

    @Transaction
    @Query("SELECT * FROM Habit WHERE habitId = :habitId")
    suspend fun getHabitWithReminders(habitId: Int): List<HabitWithReminders>

    @Transaction
    @Query("SELECT * FROM Habit")
    suspend fun getAllHabitsWithReminders(): List<HabitWithReminders>

    @Transaction
    suspend fun insertHabitWithReminders(habit: Habit, reminders: List<Reminder>) {
        val habitId = insertHabit(habit)
        reminders.forEach { reminder ->
            insertReminder(reminder.copy(habitId = habitId.toInt()))
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: Reminder): Long
}
