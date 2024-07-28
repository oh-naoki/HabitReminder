package di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import data.AppDatabase
import domain.HabitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import repository.HabitRepositoryImpl
import ui.home.HomeViewModel
import ui.input.InputViewModel

fun appModule() = module {
    single<AppDatabase> {
        get<RoomDatabase.Builder<AppDatabase>>()
            .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = false)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    factory<HabitRepository> {
        HabitRepositoryImpl(
            habitDao = get<AppDatabase>().habitDao()
        )
    }
    viewModelOf(::HomeViewModel)
    viewModelOf(::InputViewModel)
}
