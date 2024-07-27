package domain

import data.entity.HabitCompletion
import domain.model.HabitItem
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun fetchHabits(): Flow<List<HabitItem>>
    suspend fun addHabit(habit: HabitItem)
    suspend fun updateHabit(habit: HabitItem)
    suspend fun updateCompletion(completion: HabitCompletion)
}
