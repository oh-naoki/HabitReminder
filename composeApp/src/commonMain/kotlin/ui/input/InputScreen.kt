package ui.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.darkokoa.datetimewheelpicker.WheelTimePicker
import domain.model.HabitItem
import kotlinx.datetime.LocalTime
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.ParametersHolder
import org.koin.core.parameter.parametersOf
import ui.component.HabitColor
import ui.component.HabitReminderTopAppBar

object InputNavGraph {
    const val route = "input"
}

fun NavGraphBuilder.inputNavGraph(
    navController: NavController
) {
    composable(
        route = "${InputNavGraph.route}?id={id}",
        arguments = listOf(navArgument("id") {
            type = NavType.StringType
            defaultValue = ""
        })
    ) {
        InputScreen(
            onBack = navController::popBackStack,
            parametersHolder = parametersOf(SavedStateHandle.createHandle(null, it.arguments))
        )
    }
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun InputScreen(
    onBack: () -> Unit,
    parametersHolder: ParametersHolder,
    viewModel: InputViewModel = koinViewModel<InputViewModel>(parameters = { parametersHolder }),
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            viewModel.confirmSaved()
            onBack()
        }
    }

    InputContent(
        habitItem = uiState.habitItem,
        remindDayOfWeek = uiState.remindDayOfWeek,
        modifier = modifier,
        onTitleChanged = viewModel::onTitleChanged,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        onRemindDayOfWeekChanged = viewModel::onChangeRemindDayOfWeek,
        onRemindTimeChanged = viewModel::onRemindTimeChanged,
        onSaved = viewModel::saveHabit,
    )
}

@Composable
private fun InputContent(
    habitItem: HabitItem,
    remindDayOfWeek: Set<Int>,
    modifier: Modifier = Modifier,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onRemindDayOfWeekChanged: (Int) -> Unit,
    onRemindTimeChanged: (Int, Int) -> Unit,
    onSaved: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        backgroundColor = Color.Black,
        topBar = {
            HabitReminderTopAppBar(
                title = "Input Habit",
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            Text(
                text = "習慣名",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.White
                )
            )
            TextField(
                value = habitItem.title,
                onValueChange = onTitleChanged,
                placeholder = {
                    Text(
                        text = "ここにタイトルを入力してください",
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    )
                },
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    color = Color.White
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.White
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "説明",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.White
                )
            )
            TextField(
                value = habitItem.description,
                onValueChange = onDescriptionChanged,
                placeholder = {
                    Text(
                        text = "ここに説明を入力してください",
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    )
                },
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    color = Color.White
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.White
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "リマインダー",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.White
                )
            )
            ReminderSection(
                reminderTime = habitItem.reminder?.let { it.hour to it.minute },
                selectedRemindDayOfWeek = remindDayOfWeek,
                onRemindDayOfWeekChanged = onRemindDayOfWeekChanged,
                onRemindTimeChanged = onRemindTimeChanged,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onSaved,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = HabitColor.Blue,
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "保存",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = HabitColor.White,
                    )
                )
            }
        }
    }
}

@Composable
private fun ReminderSection(
    reminderTime: Pair<Int, Int>?,
    selectedRemindDayOfWeek: Set<Int>,
    onRemindDayOfWeekChanged: (Int) -> Unit,
    onRemindTimeChanged: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(
                color = Color(0xFF0A0A0A),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "曜日",
            style = TextStyle(
                fontSize = 12.sp,
                color = Color.White
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("月", "火", "水", "木", "金", "土", "日").forEachIndexed { index, day ->
                ReminderButton(
                    text = day,
                    selected = selectedRemindDayOfWeek.contains(index),
                    onClick = { onRemindDayOfWeekChanged(index) },
                )
            }
        }
        Text(
            text = "時間",
            style = TextStyle(
                fontSize = 12.sp,
                color = Color.White
            )
        )
        if (reminderTime != null) {
            WheelTimePicker(
                startTime = LocalTime(reminderTime.first, reminderTime.second),
                modifier = Modifier.fillMaxWidth(),
                textColor = HabitColor.White
            ) {
                onRemindTimeChanged(it.hour, it.minute)
            }
        } else {
            WheelTimePicker(
                modifier = Modifier.fillMaxWidth(),
                textColor = HabitColor.White
            ) {
                onRemindTimeChanged(it.hour, it.minute)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ReminderButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Button(
            onClick = onClick,
            modifier = modifier
                .size(40.dp)
                .defaultMinSize(24.dp),
            contentPadding = PaddingValues(4.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (selected) HabitColor.Blue else HabitColor.Gray,
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = HabitColor.White,
                )
            )
        }
    }
}
