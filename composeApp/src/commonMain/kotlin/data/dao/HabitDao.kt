package data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import data.entity.Habit
import data.entity.HabitCompletion
import data.entity.HabitWithReminders
import data.entity.HabitWithRemindersAndCompletions
import data.entity.Reminder
import extensions.endOfDay
import extensions.startOfDay
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long

    @Update
    suspend fun updateHabit(habit: Habit)

    @Query("DELETE FROM Habit WHERE habitId = :habitId")
    suspend fun deleteHabit(habitId: Int)

    @Query("SELECT * FROM Habit WHERE habitId = :habitId")
    suspend fun getHabitById(habitId: Int): Habit?

    @Transaction
    @Query("SELECT * FROM Habit WHERE habitId = :habitId")
    suspend fun getHabitWithReminders(habitId: Int): HabitWithReminders

    @Transaction
    @Query("SELECT * FROM Habit")
    suspend fun getAllHabitsWithReminders(): List<HabitWithReminders>

    fun getAllHabitsWithRemindersAndCompletions(unixTimeSec: Long): Flow<List<HabitWithRemindersAndCompletions>> {
        val instant = Instant.fromEpochSeconds(unixTimeSec)
        val startOfDay = instant.startOfDay().epochSeconds
        val endOfDay = instant.endOfDay().epochSeconds
        return getAllHabitsWithRemindersAndCompletionsByDateRange(startOfDay, endOfDay)
    }

    @Query(
        """
        SELECT Habit.*, Reminder.*, HabitCompletion.*
        FROM Habit
        LEFT JOIN Reminder ON Habit.habitId = Reminder.habitId
        LEFT JOIN HabitCompletion ON Habit.habitId = HabitCompletion.habitId
        AND HabitCompletion.unixTime BETWEEN :startOfDay AND :endOfDay
        """
    )
    fun getAllHabitsWithRemindersAndCompletionsByDateRange(
        startOfDay: Long,
        endOfDay: Long
    ): Flow<List<HabitWithRemindersAndCompletions>>

    @Transaction
    suspend fun insertHabitWithReminders(habit: Habit, reminder: Reminder) {
        val habitId = insertHabit(habit)
        insertReminder(reminder.copy(habitId = habitId.toInt()))
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: Reminder): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCompletion(habitCompletion: HabitCompletion)
}
