package di

import data.getDatabaseBuilder
import getNotificationManager
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module {
    return module {
        single { getDatabaseBuilder() }
        single { getNotificationManager() }
    }
}
