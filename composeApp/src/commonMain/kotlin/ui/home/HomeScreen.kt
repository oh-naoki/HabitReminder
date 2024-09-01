package ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.component.HabitReminderTopAppBar
import ui.component.RemindItem

object HomeNavGraph {
    const val route = "home"
}

fun NavGraphBuilder.homeNavGraph(
    navigateInput: (habitId: Int?) -> Unit
) {
    composable(HomeNavGraph.route) {
        HomeScreen(navigateInput)
    }
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(
    navigateInput: (habitId: Int?) -> Unit,
    viewModel: HomeViewModel = koinViewModel<HomeViewModel>(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        modifier = modifier,
        backgroundColor = Color.Black,
        topBar = {
            HabitReminderTopAppBar(
                title = "Habit Reminder",
                action = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        tint = Color.White,
                        contentDescription = "Add",
                        modifier = Modifier.clickable {
                            navigateInput(null)
                        }
                            .size(24.dp)
                    )
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(
                    color = Color(0xFF0A0A0A),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
        ) {
            Spacer(modifier = Modifier.size(16.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(uiState.habitItems) {
                    RemindItem(
                        isComplete = it.isComplete,
                        title = it.title,
                        icon = Icons.Default.Check,
                        onClickHabit = { navigateInput(it.id) },
                        onClickComplete = { viewModel.onClickCompletion(it) }
                    )
                }
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
