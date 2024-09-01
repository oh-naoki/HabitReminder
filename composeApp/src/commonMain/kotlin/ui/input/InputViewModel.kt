package ui.input

import LocalNotificationManager
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.HabitRepository
import domain.model.HabitItem
import domain.model.ReminderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InputViewModel(
    savedStateHandle: SavedStateHandle,
    private val habitRepository: HabitRepository,
    private val localNotificationManager: LocalNotificationManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(InputUiState())
    val uiState: StateFlow<InputUiState> = _uiState.asStateFlow()

    init {
        val habitId = savedStateHandle.get<String>("id")
        println("habitId: $habitId")
        if (!habitId.isNullOrBlank()) {
            viewModelScope.launch {
                val habit = habitRepository.fetchHabitById(habitId.toInt())
                setHabit(habit)
            }
        }
    }

    fun onTitleChanged(title: String) {
        _uiState.update {
            it.copy(habitItem = it.habitItem.copy(title = title))
        }
    }

    fun onDescriptionChanged(description: String) {
        _uiState.update {
            it.copy(habitItem = it.habitItem.copy(description = description))
        }
    }

    fun setHabit(habitItem: HabitItem) {
        _uiState.update {
            it.copy(
                habitItem = habitItem,
                remindDayOfWeek = habitItem.reminder?.dayOfWeek?.let { dayOfWeekBits ->
                    (0..6).filter { dayOfWeek ->
                        dayOfWeekBits and (1.shl(dayOfWeek)) != 0
                    }.toSet()
                } ?: emptySet(),
            )
        }
    }

    fun onChangeRemindDayOfWeek(dayOfWeek: Int) {
        _uiState.update {
            val remindDayOfWeek = if (it.remindDayOfWeek.contains(dayOfWeek)) {
                it.remindDayOfWeek - dayOfWeek
            } else {
                it.remindDayOfWeek + dayOfWeek
            }
            it.copy(remindDayOfWeek = remindDayOfWeek)
        }
    }

    fun onRemindTimeChanged(hour: Int, minute: Int) {
        _uiState.update {
            it.copy(remindTime = hour to minute)
        }
    }

    fun saveHabit() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val dayOfWeekBits = _uiState.value.remindDayOfWeek.fold(0) { acc, dayOfWeek ->
                    acc or (1.shl(dayOfWeek))
                }

                val reminder = ReminderItem(
                    dayOfWeek = dayOfWeekBits, // 設定されたビットフィールドを渡す
                    hour = _uiState.value.remindTime.first,
                    minute = _uiState.value.remindTime.second,
                )

                _uiState.update {
                    it.copy(
                        habitItem = it.habitItem.copy(
                            reminder = reminder
                        )
                    )
                }
                habitRepository.addHabit(_uiState.value.habitItem)
                _uiState.value.remindDayOfWeek.forEach { dayOfWeek ->
                    localNotificationManager.scheduleNotification(
                        dayOfWeek,
                        _uiState.value.remindTime.first,
                        _uiState.value.remindTime.second,
                        _uiState.value.habitItem.title,
                        _uiState.value.habitItem.description
                    )
                }
            } catch (e: Throwable) {
                _uiState.update { it.copy(error = e) }
            } finally {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSaved = true
                    )
                }

            }
        }
    }

    fun deleteHabit(habitId: Int) {
        viewModelScope.launch {
            habitRepository.deleteHabit(habitId)
        }
    }

    fun confirmSaved() {
        _uiState.update { it.copy(isSaved = false) }
    }
}

data class InputUiState(
    val habitItem: HabitItem = HabitItem.empty(),
    val remindDayOfWeek: Set<Int> = emptySet(),
    val remindTime: Pair<Int, Int> = 0 to 0,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val isSaved: Boolean = false,
) {
    fun canDelete(): Boolean {
        return habitItem.id != null
    }
}
