# 🧪 Testing Patterns & Architecture

> **Para IAs**: Patrones de testing implementados en este proyecto KMP con ejemplos concretos

## 🏗️ **Arquitectura de Testing**

```
composeApp/src/commonTest/kotlin/org/efradev/todolist/
├── domain/
│   └── CreateShoppingListUseCaseTest.kt     # ✅ Use Case testing
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

### **Presentation Layer (ViewModels)** - 🎯 Target: >80%
- ✅ Estados UI (Loading, Success, Error)
- ✅ Navegación entre estados
- ✅ Manejo de errores
- ⚠️ Composición UI (requiere integration tests)

### **Data Layer (Repositories)** - 🎯 Target: >70%
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

**Última actualización**: 28 de Junio, 2025
