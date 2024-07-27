package data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "HabitCompletion",
    foreignKeys = [ForeignKey(
        entity = Habit::class,
        parentColumns = ["habitId"],
        childColumns = ["habitId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class HabitCompletion(
    @PrimaryKey val habitId: Int,
    val unixTime: Long,
    val isCompleted: Boolean
)
