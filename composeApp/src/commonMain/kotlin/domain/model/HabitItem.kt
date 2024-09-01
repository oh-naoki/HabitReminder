package domain.model

data class HabitItem(
    val id: Int? = null,
    val isComplete: Boolean,
    val title: String,
    val description: String,
    val reminder: ReminderItem?
) {
    companion object {
        fun empty() = HabitItem(
            id = null,
            isComplete = false,
            title = "",
            description = "",
            reminder = null
        )
    }
}

data class ReminderItem(
    val dayOfWeek: Int,
    val hour: Int,
    val minute: Int,
)
