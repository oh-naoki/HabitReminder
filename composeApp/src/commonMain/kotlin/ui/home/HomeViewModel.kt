package ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.HabitRepository
import domain.model.HabitItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class HomeViewModel(
    private val habitRepository: HabitRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                )
            }
            habitRepository.addHabit(
                HabitItem(
                    isComplete = false,
                    title = "Read a book",
                    reminder = emptyList()
                )
            )
        }
        habitRepository.fetchHabits().onEach { habits ->
            _uiState.update {
                it.copy(
                    habitItems = habits,
                    isLoading = false,
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onClickCompletion(habit: HabitItem) {
        viewModelScope.launch {
            habitRepository.updateCompletion(
                data.entity.HabitCompletion(
                    habitId = checkNotNull(habit.id),
                    isCompleted = !habit.isComplete,
                    unixTime = Clock.System.now().epochSeconds
                )
            )
        }
    }
}

data class HomeUiState(
    val habitItems: List<HabitItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null,
)
