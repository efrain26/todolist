##This is a Kotlin Multiplatform project targeting Android, iOS.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…

## Repository Best Practices

### Interface-First Approach
We follow the interface-first approach for our repositories. This means:
- Define an interface that declares all repository operations
- Create an implementation class that implements the interface
- Use dependency injection to provide the concrete implementation

Example:
```kotlin
interface UserRepository {
    suspend fun checkUser(email: String): Result<UserCheckResult>
    suspend fun registerUser(request: RegisterRequest): Result<RegisterResponse>
}

class UserRepositoryImpl(private val client: HttpClient) : UserRepository {
    // Implementation details
}
```

### Error Handling with Result
We use Kotlin's Result type to handle operations that can fail:
- Wrap repository responses in Result to handle success and failure cases explicitly
- Use Result.success() for successful operations
- Use Result.failure() for unexpected exceptions
- Map specific error responses to domain-specific error types

Benefits:
- Type-safe error handling
- Clear separation between success and failure paths
- Easy to chain operations with map/flatMap
- Consistent error handling across the application

### Additional Best Practices
1. **Single Responsibility**: Each repository should handle operations for a single domain entity or closely related group of entities.

2. **Abstraction Layer**: Repositories should abstract away the data source details from the rest of the application.
   - The rest of the app shouldn't know if data comes from a network, database, or both
   - Data source implementation details should be hidden behind the interface

3. **Domain Model Mapping**: 
   - Convert between data transfer objects (DTOs) and domain models
   - Keep network/database models separate from domain models
   - Handle data transformation within the repository

4. **Coroutines for Async Operations**:
   - Use suspend functions for asynchronous operations
   - Handle threading and concurrency at the repository level
   - Properly scope coroutines for lifecycle management

5. **Testing**:
   - Interfaces make it easy to create test doubles (mocks, fakes)
   - Use dependency injection to swap implementations in tests
   - Create separate test implementations for offline testing

6. **Error States**:
   - Define clear error states using sealed classes/interfaces
   - Provide meaningful error messages and types
   - Handle both expected and unexpected errors gracefully

7. **Caching Strategy** (when applicable):
   - Implement caching logic within the repository
   - Clear separation between cache and network data sources
   - Define clear cache invalidation rules

8. **Clean API**:
   - Keep repository methods focused and concise
   - Use clear, descriptive method names
   - Document complex operations or business rules

These practices help create maintainable, testable, and robust repository implementations that serve as a reliable data layer for the application.

## Use Case Best Practices

### Single Responsibility
Each use case should represent a single action or business operation. For example:
- `RegisterUserUseCase`: Handles user registration
- `CheckUserExistsUseCase`: Validates if a user exists

### Result Wrapping
Use cases should wrap their results in Kotlin's Result type for better error handling:
```kotlin
class RegisterUserUseCase(
    private val repository: UserRepository,
    private val stringResProvider: StringResProvider
) {
    suspend operator fun invoke(...): Result<RegisterResult>
}
```

### Sealed Classes for Domain Results
Use sealed classes/interfaces to represent domain-specific results:
```kotlin
sealed interface RegisterResult {
    data class Success(val message: String) : RegisterResult
    data class Error(val message: String) : RegisterResult
}
```

Benefits:
- Type-safe handling of all possible outcomes
- Clear representation of domain states
- Forces exhaustive handling in when expressions
- Makes impossible states impossible

### Dependency Injection
- Inject dependencies through constructor
- Keep dependencies minimal and focused
- Use interfaces instead of concrete implementations
- Makes testing easier through dependency substitution

Example:
```kotlin
class CheckUserExistsUseCase(
    private val repository: UserRepository,
    private val stringRes: StringResProvider
)
```

### Use Case Structure
1. **Single Public Method**: Usually named `invoke` to make the use case callable like a function
2. **Suspend Function**: For asynchronous operations
3. **Parameter Validation**: If needed, validate inputs before processing
4. **Business Logic**: Implement the core business logic
5. **Result Transformation**: Map data layer results to domain-specific results

### Error Handling
Use a combination of:
- Result type for operation success/failure
- Sealed classes for domain-specific results
- String resources for user-facing messages

Example:
```kotlin
return repository.someOperation().map { response ->
    when (response) {
        is Success -> DomainResult.Success(stringRes("success_message"))
        is Error -> DomainResult.Error(stringRes("error_message"))
    }
}
```

### Clean Architecture Principles
1. **Independence from Frameworks**: Use cases shouldn't depend on UI or external frameworks
2. **Testability**: Easy to test without external dependencies
3. **Domain-Centric**: Focus on business rules rather than technical details
4. **Data Flow**: Input → Use Case → Result

### Communication with ViewModels
1. Use cases return domain-specific sealed classes/interfaces
2. ViewModels map these to UI states
3. Clear separation between domain logic and UI logic

Example flow:
```kotlin
// Domain Result (Use Case)
sealed interface UserCheckResultWithMessage {
    val message: String
    data class Registered(override val message: String) : UserCheckResultWithMessage
    data class NotRegistered(override val message: String) : UserCheckResultWithMessage
    data class Error(override val message: String) : UserCheckResultWithMessage
}

// UI State (ViewModel)
sealed class EmailCheckState {
    object Idle : EmailCheckState()
    object Loading : EmailCheckState()
    data class Success(val message: String, val isRegistered: Boolean) : EmailCheckState()
    data class Error(val message: String) : EmailCheckState()
}
```

### Testing Considerations
1. Use interfaces for dependencies to easily mock them
2. Test all possible outcomes defined in sealed classes
3. Verify business rules are correctly applied
4. Test error handling and edge cases

These practices help create maintainable, testable, and robust use cases that properly encapsulate business logic while maintaining clean architecture principles.
