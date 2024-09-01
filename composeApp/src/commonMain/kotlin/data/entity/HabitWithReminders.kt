package data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithReminders(
    @Embedded val habit: Habit,
    @Relation(
        parentColumn = "habitId",
        entityColumn = "habitId"
    )
    val reminder: Reminder
)
