# 🧪 Testing Patterns & Architecture

> **Para IAs**: Patrones de testing implementados en este proyecto KMP con ejemplos concretos

## 🏗️ **Arquitectura de Testing**

```
composeApp/src/commonTest/kotlin/org/efradev/todolist/
├── domain/
│   ├── CreateShoppingListUseCaseTest.kt     # ✅ Use Case testing
│   ├── CheckAuthStateUseCaseTest.kt         # ✅ Auth state testing
│   ├── CheckUserExistsUseCaseTest.kt        # ✅ User validation testing
│   ├── GetShoppingListsUseCaseTest.kt       # ✅ List retrieval testing ⭐ NUEVO
│   ├── LoginUseCaseTest.kt                  # ✅ Login flow testing ⭐ NUEVO
│   ├── LogoutUseCaseTest.kt                 # ✅ Logout flow testing ⭐ NUEVO
│   ├── RegisterUserUseCaseTest.kt           # ✅ Registration testing ⭐ NUEVO
│   └── TestFakes.kt                         # ✅ Shared fake objects ⭐ NUEVO
├── viewmodel/
│   └── CreateListViewModelTest.kt           # ✅ ViewModel testing  
└── data/
    └── [Repository tests - pendientes]       # 🔄 Para implementar
```

## 🎯 **Patrón: Use Case Testing**

### **Estructura base:**
```kotlin
class MyUseCaseTest {
    private val fakeRepository = FakeRepository()
    private val useCase = MyUseCase(fakeRepository)
    
    @Test
    fun `should do X when Y condition`() = runTest {
        // Given
        fakeRepository.nextResult = Result.success(expectedData)
        
        // When  
        val result = useCase(inputParams)
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedData, result.getOrNull())
    }
}
```

### **Fake Repository Pattern:**
```kotlin
class FakeRepository : Repository {
    var nextResult: Result<Data> = Result.success(defaultData)
    var lastInvokedParams: Params? = null
    
    override suspend fun operation(params: Params): Result<Data> {
        lastInvokedParams = params  // Para verificar params
        return nextResult           // Resultado configurable
    }
}
```

## 🎮 **Patrón: ViewModel Testing**

### **Problema**: ViewModels usan coroutines y son `final`
### **Solución**: TestableViewModel wrapper

```kotlin
class TestableViewModel(
    private val fakeUseCase: FakeUseCase
) {
    var uiState: UiState by mutableStateOf(UiState.Idle)
        private set

    suspend fun action(params: Params) {
        uiState = UiState.Loading
        // ... lógica similar al ViewModel real
    }
}
```

### **Test estructura:**
```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val fakeUseCase = FakeUseCase()
    private val viewModel = TestableViewModel(fakeUseCase)

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `should update state when action executed`() = runTest {
        // Given
        fakeUseCase.nextResult = Result.success(data)
        
        // When
        viewModel.action(params)
        
        // Then  
        assertTrue(viewModel.uiState is UiState.Success)
    }
}
```

## 📊 **Coverage Strategies por Capa**

### **Domain Layer (Use Cases)** - 🎯 Target: >90%
- ✅ Casos exitosos
- ✅ Casos de error (validación, network, etc.)  
- ✅ Edge cases (input vacío, nulo, etc.)
- ✅ Transformación de datos

### **Presentation Layer (ViewModels)** - 🎯 Target: >90%
- ✅ Estados UI (Loading, Success, Error)
- ✅ Navegación entre estados
- ✅ Manejo de errores
- ⚠️ Composición UI (requiere integration tests)

### **Data Layer (Repositories)** - 🎯 Target: >90%
- 🔄 Network layer mocking
- 🔄 Local storage mocking  
- 🔄 Cache strategies
- 🔄 Error mapping

## 🔧 **Testing Utilities Implementadas**

### **Coroutines Testing:**
```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
private val testDispatcher = StandardTestDispatcher()

@BeforeTest
fun setup() {
    Dispatchers.setMain(testDispatcher)
}
```

### **Result Testing:**
```kotlin
// Para Result<Success>
assertTrue(result.isSuccess)
assertEquals(expected, result.getOrNull())

// Para Result<Failure>  
assertTrue(result.isFailure)
assertTrue(result.exceptionOrNull()?.message?.contains("error") == true)
```

### **State Testing:**
```kotlin
// Estados sealed class
assertTrue(state is UiState.Loading)
assertTrue(state is UiState.Success)
val successState = state as UiState.Success
assertEquals(expectedData, successState.data)
```

## 📋 **Checklist para Nuevos Tests**

### **Al agregar un nuevo Use Case:**
- [ ] Test caso exitoso principal
- [ ] Test validación de parámetros
- [ ] Test error de repositorio  
- [ ] Test edge cases
- [ ] Verificar parámetros pasados al repositorio

### **Al agregar un nuevo ViewModel:**
- [ ] Test estado inicial
- [ ] Test transiciones de estado
- [ ] Test manejo de errores
- [ ] Test cleanup/reset
- [ ] Crear TestableViewModel wrapper

### **Al agregar un nuevo Repository:**
- [ ] Mock cliente HTTP/base de datos
- [ ] Test casos exitosos
- [ ] Test errores de red
- [ ] Test mapeo de respuestas  
- [ ] Test cache si aplica

## 🚨 **Anti-Patterns a Evitar**

❌ **No hacer:**
```kotlin
// Usar runBlocking en tests de UI
runBlocking { /* código async */ }

// Tests que dependen de orden de ejecución
@Test fun test1() { /* modifica estado global */ }
@Test fun test2() { /* depende de test1 */ }

// Mocks complejos que replican toda la lógica
MockRepository().apply {
    // 50 líneas de setup complejo
}
```

✅ **Hacer:**
```kotlin
// Usar runTest para coroutines
@Test fun `test name`() = runTest { /* código async */ }

// Tests independientes con setup claro
@BeforeTest fun setup() { /* estado limpio */ }

// Fakes simples y configurables
fakeRepo.nextResult = Result.success(data)
```

## 🔍 **Debugging Tests**

### **Test no ejecuta:**
```kotlin
// Verificar que está en commonTest, no androidTest
// Verificar @Test annotation
// Verificar runTest para suspend functions
```

### **Coverage no refleja:**
```kotlin
// Verificar exclusiones en build.gradle.kts
// Verificar que el código está en commonMain  
// Ejecutar clean antes de coverage
```

### **Flaky tests:**
```kotlin
// Usar testDispatcher.scheduler.advanceUntilIdle()
// Verificar estado inicial limpio en @BeforeTest
// Evitar delays o sleeps
```

---

## 📖 **Ejemplos de Tests Completos**

Ver archivos de referencia:
- `CreateShoppingListUseCaseTest.kt` - Patrón Use Case completo
- `CreateListViewModelTest.kt` - Patrón ViewModel completo

---

## 🆕 **Patrones Implementados Recientemente**

### **✅ Authentication State Testing**
```kotlin
class CheckAuthStateUseCaseTest {
    private val fakePreferencesRepository = FakePreferencesRepository()
    private val useCase = CheckAuthStateUseCase(fakePreferencesRepository)

    @Test
    fun `should return authenticated state when user is logged in and has auth data`() = runTest {
        // Given
        val expectedUser = AuthResponse(user = testUser, token = testToken)
        fakePreferencesRepository.isLoggedIn = true
        fakePreferencesRepository.authData = expectedUser
        
        // When & Then
        val result = useCase()
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull() is AuthState.Authenticated)
    }
}
```

### **✅ User Validation with Message Localization**
```kotlin  
class CheckUserExistsUseCaseTest {
    private val fakeUserRepository = FakeUserRepository()
    private val fakeStringRes = FakeStringResProvider()
    private val useCase = CheckUserExistsUseCase(fakeUserRepository, fakeStringRes::getString)

    @Test
    fun `should return registered result when user exists`() = runTest {
        // Given
        fakeUserRepository.nextResult = Result.success(UserCheckResult.Registered)
        fakeStringRes.strings["user_registered"] = "Usuario registrado"
        
        // When & Then
        val result = useCase("test@example.com")
        assertTrue(result.getOrNull() is UserCheckResultWithMessage.Registered)
    }
}
```

### **✅ Enhanced Shopping List Creation Testing**
```kotlin
// Casos adicionales implementados:
@Test 
fun `should use default type when type is not provided`() = runTest {
    val result = useCase("Mi Lista") // Sin especificar tipo
    assertEquals("simple", fakeRepository.lastCreateParams?.type)
}

@Test
fun `should handle unexpected exception during execution`() = runTest {
    fakeRepository.shouldThrowException = true
    val result = useCase("name", "type")
    assertTrue(result.isFailure)
}
```

### **✅ Repository Abstraction with Shared Fakes**
```kotlin
// TestFakes.kt - Clases fake centralizadas para evitar duplicación
class FakeUserRepositoryForTests : UserRepository {
    var nextLoginResult: Result<AuthResponse> = Result.success(defaultAuth)
    var nextRegisterResult: Result<RegisterResponse> = Result.success(defaultResponse)
    var lastLoginRequest: LoginRequest? = null
    
    override suspend fun login(request: LoginRequest): Result<AuthResponse> {
        lastLoginRequest = request  // Capturar para verificación
        return nextLoginResult
    }
}
```

### **✅ Login/Logout Flow Testing**
```kotlin
class LoginUseCaseTest {
    @Test
    fun `should save auth data when login succeeds`() = runTest {
        // Given
        fakeUserRepository.nextLoginResult = Result.success(expectedAuth)
        
        // When
        val result = useCase(email, password)
        
        // Then
        assertTrue(fakePreferencesRepository.saveAuthDataWasCalled)
        assertEquals(expectedAuth, fakePreferencesRepository.authData)
    }
}
```

### **✅ Registration with Nullable Response Handling**
```kotlin
class RegisterUserUseCaseTest {
    @Test
    fun `should use default message when response username is null`() = runTest {
        // Given
        val responseWithNull = RegisterResponse(id = 123, username = null)
        fakeUserRepository.nextRegisterResult = Result.success(responseWithNull)
        fakeStringRes.strings["register_success"] = "Registro exitoso"
        
        // When & Then
        val result = useCase(params...)
        assertEquals("Registro exitoso", result.getOrNull()?.message)
    }
}
```

### **✅ Repository Error Propagation Testing**
```kotlin
@Test
fun `should propagate repository failure when operation fails`() = runTest {
    // Given
    val exception = RuntimeException("Network error")
    fakeRepository.nextResult = Result.failure(exception)
    
    // When
    val result = useCase(params)
    
    // Then
    assertTrue(result.isFailure)
    assertEquals(exception, result.exceptionOrNull())
}
```

### **✅ Data Model Consistency Testing**
```kotlin
@Test 
fun `should create correct request with all provided parameters`() = runTest {
    // When
    val result = useCase(param1, param2, param3, param4, param5, param6)
    
    // Then
    val capturedRequest = fakeRepository.lastRequest
    assertEquals(param1, capturedRequest?.field1)
    assertEquals(param2, capturedRequest?.field2)
    // ... verificar todos los parámetros
}
```

---

## 📊 **Métricas de Coverage Alcanzadas**

### **Domain Layer Coverage:**
- ✅ **CheckAuthStateUseCase:** 100%
- ✅ **CheckUserExistsUseCase:** 100%  
- ✅ **CreateShoppingListUseCase:** 100%
- ✅ **GetShoppingListsUseCase:** 100%
- ✅ **LoginUseCase:** 100%
- ✅ **LogoutUseCase:** 100%
- ✅ **RegisterUserUseCase:** 100%

### **Coverage General del Proyecto:**
- **Líneas:** 10.72% (⬆️ +4.72% desde última iteración)
- **Branches:** 8.30% (⬆️ +2.30%)
- **Instrucciones:** 6.49% (⬆️ +2.49%)
- **Tests:** 45 tests total (+19 tests nuevos)

---

**Última actualización**: 29 de Junio, 2025
