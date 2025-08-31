# Mensinator App - MVVM Architecture Documentation

## Overview

This document outlines the refactored MVVM (Model-View-ViewModel) architecture for the Mensinator app, following clean architecture principles and Android best practices.

## Architecture Layers

### 1. Presentation Layer (UI)
- **Components**: Jetpack Compose UI components
- **ViewModels**: Handle UI logic and state management
- **Screens**: Main UI screens and navigation

### 2. Domain Layer
- **Use Cases**: Business logic and operations
- **Repository Interfaces**: Data access contracts
- **Domain Models**: Core business entities

### 3. Data Layer
- **Repository Implementations**: Data access logic
- **Data Sources**: Local database, remote APIs
- **Data Models**: Data transfer objects

## Package Structure

```
app/src/main/java/com/mensinator/app/
├── core/                           # Core functionality
│   ├── di/                        # Dependency injection
│   ├── domain/                    # Domain layer
│   │   ├── model/                 # Domain models
│   │   ├── repository/            # Repository interfaces
│   │   └── usecase/               # Use cases
│   ├── ui/                        # UI components
│   │   ├── components/            # Reusable UI components
│   │   └── theme/                 # Material 3 theming
│   └── util/                      # Utility classes
├── calendar/                       # Calendar feature
├── settings/                       # Settings feature
├── statistics/                     # Statistics feature
├── symptoms/                       # Symptoms feature
└── questionnaire/                  # Questionnaire feature
```

## Key Components

### Domain Models

#### Period
```kotlin
data class Period(
    val id: Long,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val flow: FlowIntensity,
    val symptoms: List<Symptom>,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
```

#### Symptom
```kotlin
data class Symptom(
    val id: Long,
    val name: String,
    val isActive: Boolean,
    val color: String,
    val category: SymptomCategory
)
```

### Repository Pattern

All data access goes through repository interfaces:

```kotlin
interface IPeriodRepository {
    suspend fun getAllPeriods(): Result<List<Period>>
    suspend fun getPeriodById(id: Long): Result<Period?>
    suspend fun savePeriod(period: Period): Result<Long>
    suspend fun deletePeriod(id: Long): Result<Boolean>
    // ... more methods
}
```

### Use Cases

Business logic is encapsulated in use cases:

```kotlin
class GetPeriodsUseCase @Inject constructor(
    private val periodRepository: IPeriodRepository
) {
    suspend operator fun invoke(): Result<List<Period>> {
        return periodRepository.getAllPeriods()
    }
}
```

### Result Wrapper

Consistent error handling with a Result wrapper:

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
```

## UI Architecture

### Material 3 Theming

The app uses Material 3 design system with:
- Comprehensive color schemes for light/dark themes
- Dynamic color support for Android 12+
- Consistent typography and shapes
- Responsive design patterns

### Responsive Components

```kotlin
@Composable
fun ResponsiveLayout(
    content: @Composable (ResponsiveLayoutInfo) -> Unit
) {
    val configuration = LocalConfiguration.current
    val layoutInfo = ResponsiveLayoutInfo(
        screenWidth = configuration.screenWidthDp.dp,
        isTablet = configuration.screenWidthDp >= 600.dp,
        isLargeScreen = configuration.screenWidthDp >= 840.dp
    )
    content(layoutInfo)
}
```

### Reusable Components

Common UI components are centralized:
- `MensinatorButton`: Consistent button styling
- `MensinatorCard`: Card component with elevation
- `LoadingIndicator`: Loading state display
- `ErrorDisplay`: Error state handling
- `EmptyState`: Empty state management

## State Management

### ViewModel State

ViewModels manage UI state using StateFlow:

```kotlin
data class CalendarUiState(
    val isLoading: Boolean = false,
    val periods: List<Period> = emptyList(),
    val selectedDates: List<LocalDate> = emptyList(),
    val error: String? = null
)
```

### Event Handling

Events are handled separately from state:

```kotlin
sealed class CalendarEvent {
    data class PeriodSaved(val period: Period) : CalendarEvent()
    data class PeriodDeleted(val periodId: Long) : CalendarEvent()
    data class Error(val message: String) : CalendarEvent()
}
```

## Error Handling

### Centralized Error Handling

```kotlin
object ErrorHandler {
    fun handleError(throwable: Throwable): String {
        return when (throwable) {
            is NetworkException -> "Network error: Please check your connection"
            is DatabaseException -> "Database error: Unable to save data"
            is ValidationException -> "Invalid data provided"
            else -> "An unexpected error occurred"
        }
    }
}
```

### Custom Exceptions

```kotlin
sealed class AppException(message: String, cause: Throwable? = null) : Exception(message, cause)
class NetworkException(message: String, cause: Throwable? = null) : AppException(message, cause)
class DatabaseException(message: String, cause: Throwable? = null) : AppException(message, cause)
```

## Dependency Injection

### Koin Configuration

```kotlin
val coreModule = module {
    single<IDispatcherProvider> { DefaultDispatcherProvider() }
    single { GetPeriodsUseCase(get()) }
    single { SavePeriodUseCase(get()) }
}
```

## Testing Strategy

### Unit Testing
- ViewModels: Test business logic and state management
- Use Cases: Test business operations
- Repositories: Test data access logic

### UI Testing
- Compose UI tests for critical user flows
- Integration tests for feature workflows

## Best Practices

### 1. Separation of Concerns
- UI logic stays in ViewModels
- Business logic goes in Use Cases
- Data access is handled by Repositories

### 2. Single Responsibility
- Each class has one clear purpose
- Methods are focused and concise
- Dependencies are minimal and explicit

### 3. Error Handling
- All errors are wrapped in Result types
- User-friendly error messages
- Proper error logging and analytics

### 4. Performance
- Lazy loading where appropriate
- Efficient state management
- Background processing for heavy operations

### 5. Accessibility
- Content descriptions for screen readers
- Proper contrast ratios
- Scalable text sizes

## Migration Guide

### From Old Architecture

1. **Move business logic** from ViewModels to Use Cases
2. **Update data models** to use domain models
3. **Refactor UI components** to use new theming system
4. **Update dependency injection** to use new modules
5. **Migrate error handling** to use Result wrapper

### Testing New Components

1. **Unit tests** for Use Cases and ViewModels
2. **Integration tests** for Repository implementations
3. **UI tests** for Compose components
4. **End-to-end tests** for critical user flows

## Future Enhancements

### Planned Improvements
- **Offline support** with Room database
- **Data synchronization** with cloud services
- **Advanced analytics** and insights
- **Machine learning** for cycle prediction
- **Multi-language support** with proper localization

### Scalability Considerations
- **Modular architecture** for feature development
- **Plugin system** for extensibility
- **Performance monitoring** and optimization
- **A/B testing** framework for UI improvements

## Conclusion

This refactored architecture provides:
- **Clean separation** of concerns
- **Testable code** structure
- **Maintainable** and **scalable** design
- **Modern Android** development practices
- **Material 3** design system integration
- **Responsive design** for all screen sizes

The architecture follows SOLID principles and provides a solid foundation for future development and maintenance.
