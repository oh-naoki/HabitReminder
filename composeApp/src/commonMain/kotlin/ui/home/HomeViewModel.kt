package ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.model.HabitItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // TODO: APIからデータを取得
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                )
            }
            delay(2.seconds)
            _uiState.update {
                // ダミーデータを流す
                it.copy(
                    habitItems = listOf(
                        HabitItem(
                            isComplete = false,
                            title = "テスト1",
                        )
                    ),
                    isLoading = false,
                )
            }
        }
    }
}

data class HomeUiState(
    val habitItems: List<HabitItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null,
)
