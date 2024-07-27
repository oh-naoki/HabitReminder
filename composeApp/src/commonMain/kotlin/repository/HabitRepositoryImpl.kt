package repository

import data.dao.HabitDao
import data.entity.Reminder
import domain.HabitRepository
import domain.model.HabitItem
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
                        reminder = habitWithRemindersAndCompletions.reminders.map { reminder ->
                            Reminder(
                                dayOfWeek = reminder.dayOfWeek,
                                hour = reminder.hour,
                                minute = reminder.minute,
                            )
                        }
                    )
                }
            }
    }

    override suspend fun addHabit(habit: HabitItem) {
        habitDao.insertHabitWithReminders(habit = habit.toHabit(), reminders = habit.toReminders())
    }

    override suspend fun updateHabit(habit: HabitItem) {
        habitDao.updateHabit(habit = habit.toHabit())
    }

    override suspend fun updateCompletion(completion: data.entity.HabitCompletion) {
        habitDao.updateCompletion(completion)
    }

    private fun HabitItem.toHabit(): data.entity.Habit {
        return data.entity.Habit(
            habitName = title,
            description = ""
        )
    }

    private fun HabitItem.toReminders(): List<Reminder> {
        return reminder.map { reminderItem ->
            Reminder(
                dayOfWeek = reminderItem.dayOfWeek,
                hour = reminderItem.hour,
                minute = reminderItem.minute,
            )
        }
    }
}
