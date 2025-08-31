package com.mensinator.app.core.di

import com.mensinator.app.core.domain.repository.IPeriodRepository
import com.mensinator.app.core.domain.repository.ISettingsRepository
import com.mensinator.app.core.domain.usecase.GetPeriodsUseCase
import com.mensinator.app.core.domain.usecase.SavePeriodUseCase
import com.mensinator.app.core.util.DefaultDispatcherProvider
import com.mensinator.app.core.util.IDispatcherProvider
import org.koin.dsl.module

/**
 * Core dependency injection module for the app
 */
val coreModule = module {
    
    // Dispatcher provider
    single<IDispatcherProvider> { DefaultDispatcherProvider() }
    
    // Use cases
    single { GetPeriodsUseCase(get()) }
    single { SavePeriodUseCase(get()) }
    
    // Note: Repository implementations will be provided by data layer modules
    // This module only defines the interfaces and use cases
}

/**
 * Feature-specific modules can extend this core module
 */
val appModule = module {
    includes(coreModule)
    
    // Add feature-specific dependencies here
    // For example:
    // single<ICalendarRepository> { CalendarRepositoryImpl(get()) }
    // single<ISettingsRepository> { SettingsRepositoryImpl(get()) }
}
