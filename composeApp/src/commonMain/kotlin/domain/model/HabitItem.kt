package domain.model

import data.entity.Reminder

data class HabitItem(
    val id: Int? = null,
    val isComplete: Boolean,
    val title: String,
    val reminder: List<Reminder>
)

data class ReminderItem(
    val dayOfWeek: Int,
    val hour: Int,
    val minute: Int,
)
