package ui.input

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

object InputNavGraph {
    const val route = "input"
}

fun NavGraphBuilder.inputNavGraph() {
    composable(InputNavGraph.route) {
        InputScreen()
    }
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun InputScreen(
    viewModel: InputViewModel = koinViewModel<InputViewModel>(),
    modifier: Modifier = Modifier,
) {
    InputContent()
}

@Composable
private fun InputContent() {
    Text("Input Screen")
}
