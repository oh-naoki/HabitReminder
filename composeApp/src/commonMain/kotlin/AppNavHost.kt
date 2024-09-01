import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ui.home.HomeNavGraph
import ui.home.homeNavGraph
import ui.input.InputNavGraph
import ui.input.inputNavGraph

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = HomeNavGraph.route
    ) {
        homeNavGraph(
            navigateInput = {
                navController.navigate(InputNavGraph.route + "?id=${it ?: ""}")
            }
        )
        inputNavGraph(
            navController = navController
        )
    }
}
