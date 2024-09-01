package repository

import data.dao.HabitDao
import data.entity.Reminder
import domain.HabitRepository
import domain.model.HabitItem
import domain.model.ReminderItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class HabitRepositoryImpl(
    private val habitDao: HabitDao
) : HabitRepository {

    override fun fetchHabits(): Flow<List<HabitItem>> {
        return habitDao.getAllHabitsWithRemindersAndCompletions(Clock.System.now().epochSeconds)
            .map {
                it.map { habitWithRemindersAndCompletions ->
                    HabitItem(
                        id = habitWithRemindersAndCompletions.habit.habitId,
                        isComplete = habitWithRemindersAndCompletions.completions?.isCompleted == true,
                        title = habitWithRemindersAndCompletions.habit.habitName,
                        description = habitWithRemindersAndCompletions.habit.description,
                        reminder = ReminderItem(
                            dayOfWeek = habitWithRemindersAndCompletions.reminder.dayOfWeek,
                            hour = habitWithRemindersAndCompletions.reminder.hour,
                            minute = habitWithRemindersAndCompletions.reminder.minute,
                        )
                    )
                }
            }
    }

    override suspend fun fetchHabitById(id: Int): HabitItem {
        val habitWithRemindersAndCompletions = habitDao.getHabitWithReminders(id)
        return HabitItem(
            id = habitWithRemindersAndCompletions.habit.habitId,
            isComplete = false,
            title = habitWithRemindersAndCompletions.habit.habitName,
            description = habitWithRemindersAndCompletions.habit.description,
            reminder = ReminderItem(
                dayOfWeek = habitWithRemindersAndCompletions.reminder.dayOfWeek,
                hour = habitWithRemindersAndCompletions.reminder.hour,
                minute = habitWithRemindersAndCompletions.reminder.minute,
            )
        )
    }

    override suspend fun addHabit(habit: HabitItem) {
        habitDao.insertHabitWithReminders(habit = habit.toHabit(), reminder = habit.toReminders())
    }

    override suspend fun updateHabit(habit: HabitItem) {
        habitDao.updateHabit(habit = habit.toHabit())
    }

    override suspend fun updateCompletion(completion: data.entity.HabitCompletion) {
        habitDao.updateCompletion(completion)
    }

    override suspend fun deleteHabit(habitId: Int) {
        habitDao.deleteHabit(habitId)
    }

    private fun HabitItem.toHabit(): data.entity.Habit {
        return data.entity.Habit(
            habitId = id ?: 0,
            habitName = title,
            description = description
        )
    }

    private fun HabitItem.toReminders(): Reminder {
        return Reminder(
            dayOfWeek = reminder?.dayOfWeek ?: 0,
            hour = reminder?.hour ?: 0,
            minute = reminder?.minute ?: 0
        )
    }
}
