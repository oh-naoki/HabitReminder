package data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Habit::class,
        parentColumns = ["habitId"],
        childColumns = ["habitId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Reminder(
    @PrimaryKey(autoGenerate = true) val reminderId: Int = 0,
    val habitId: Int = -1,// TODO: どうにかしたい
    val dayOfWeek: Int,
    val hour: Int,
    val minute: Int
)
