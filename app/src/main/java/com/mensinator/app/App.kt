package com.mensinator.app

import android.app.AlarmManager
import android.app.Application
import com.mensinator.app.business.CalculationsHelper
import com.mensinator.app.business.ExportImport
import com.mensinator.app.business.ICalculationsHelper
import com.mensinator.app.business.IExportImport
import com.mensinator.app.business.IOvulationPrediction
import com.mensinator.app.business.IPeriodDatabaseHelper
import com.mensinator.app.business.IPeriodPrediction
import com.mensinator.app.business.OvulationPrediction
import com.mensinator.app.business.PeriodDatabaseHelper
import com.mensinator.app.business.PeriodPrediction
import com.mensinator.app.business.notifications.AndroidNotificationScheduler
import com.mensinator.app.business.notifications.IAndroidNotificationScheduler
import com.mensinator.app.business.notifications.INotificationScheduler
import com.mensinator.app.business.notifications.NotificationScheduler
import com.mensinator.app.calendar.CalendarViewModel
import com.mensinator.app.calendar.CalendarViewModelRefactored
import com.mensinator.app.core.data.repository.PeriodRepositoryImpl
import com.mensinator.app.core.data.repository.SettingsRepositoryImpl
import com.mensinator.app.core.di.appModule
import com.mensinator.app.core.domain.repository.IPeriodRepository
import com.mensinator.app.core.domain.repository.ISettingsRepository
import com.mensinator.app.settings.SettingsViewModel
import com.mensinator.app.statistics.StatisticsViewModel
import com.mensinator.app.symptoms.ManageSymptomsViewModel
import com.mensinator.app.utils.DefaultDispatcherProvider
import com.mensinator.app.utils.IDispatcherProvider
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class App : Application() {

    // Legacy business logic module (to be gradually migrated)
    private val legacyBusinessModule = module {
        singleOf(::PeriodDatabaseHelper) { bind<IPeriodDatabaseHelper>() }
        singleOf(::CalculationsHelper) { bind<ICalculationsHelper>() }
        singleOf(::OvulationPrediction) { bind<IOvulationPrediction>() }
        singleOf(::PeriodPrediction) { bind<IPeriodPrediction>() }
        singleOf(::ExportImport) { bind<IExportImport>() }
        singleOf(::NotificationScheduler) { bind<INotificationScheduler>() }
        singleOf(::DefaultDispatcherProvider) { bind<IDispatcherProvider>() }
        singleOf(::AndroidNotificationScheduler) { bind<IAndroidNotificationScheduler>() }
        single { androidContext().getSystemService(ALARM_SERVICE) as AlarmManager }

        // Legacy ViewModels (to be gradually replaced)
        viewModel { CalendarViewModel(get(), get(), get(), get()) }
        viewModel { ManageSymptomsViewModel(get()) }
        viewModel { SettingsViewModel(get(), get(), get(), get()) }
        viewModel { StatisticsViewModel(get(), get(), get(), get(), get()) }
    }

    // New refactored ViewModels and dependencies
    private val refactoredModule = module {
        // Register the temporary repository implementations
        single<IPeriodRepository> { PeriodRepositoryImpl() }
        single<ISettingsRepository> { SettingsRepositoryImpl() }
        
        // Register the new refactored ViewModel
        viewModel { CalendarViewModelRefactored(get(), get()) }
        
        // Note: More repository implementations will be added here as needed
    }

    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase (commented out as per original code)
        // if (FirebaseApp.getApps(this).isEmpty()) {
        //     FirebaseApp.initializeApp(this)
        // }

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                appModule,                    // New core architecture
                legacyBusinessModule,         // Legacy business logic (to be migrated)
                refactoredModule              // New refactored ViewModels
            )
        }
    }
}