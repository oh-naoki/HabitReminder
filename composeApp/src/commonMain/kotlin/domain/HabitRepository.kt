package domain

import data.entity.HabitCompletion
import domain.model.HabitItem
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun fetchHabits(): Flow<List<HabitItem>>
    suspend fun fetchHabitById(id: Int): HabitItem
    suspend fun addHabit(habit: HabitItem)
    suspend fun updateHabit(habit: HabitItem)
    suspend fun updateCompletion(completion: HabitCompletion)
    suspend fun deleteHabit(habitId: Int)
}
