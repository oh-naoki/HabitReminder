package data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Habit")
data class Habit(
    @PrimaryKey(autoGenerate = true) val habitId: Int = 0,
    val habitName: String,
    val description: String
)

