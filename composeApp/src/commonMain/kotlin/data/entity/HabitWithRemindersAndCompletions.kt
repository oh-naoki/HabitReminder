package data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithRemindersAndCompletions(
    @Embedded val habit: Habit,
    @Relation(
        parentColumn = "habitId",
        entityColumn = "habitId"
    )
    val reminder: Reminder,
    @Relation(
        parentColumn = "habitId",
        entityColumn = "habitId"
    )
    val completions: HabitCompletion?
)
