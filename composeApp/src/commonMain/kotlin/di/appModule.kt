package di

import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ui.home.HomeViewModel


fun appModule() = module {
    viewModelOf(::HomeViewModel)
}
