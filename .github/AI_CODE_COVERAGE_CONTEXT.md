# 🤖 AI Code Coverage & TDD Context Guide

> **Para IAs trabajando en este proyecto**: Este archivo contiene todo el contexto necesario para entender y trabajar con el sistema de Code Coverage y TDD implementado.

## 📋 **Resumen del Sistema**

Este proyecto **Kotlin Multiplatform (KMP) con Compose** usa **Kover** para Code Coverage en un flujo de trabajo **Test-Driven Development (TDD)**.

### **Decisión técnica: ¿Por qué Kover vs JaCoCo?**

✅ **Kover elegido** por:
- **Soporte nativo Kotlin**: Entiende coroutines, inline functions, etc.
- **KMP friendly**: Diseñado para proyectos multiplataforma
- **Compose compatible**: Maneja código generado de Compose
- **Configuración simple**: Menos setup que JaCoCo
- **JetBrains**: Alineado con ecosistema Kotlin

❌ **JaCoCo descartado** por:
- Limitaciones con características específicas de Kotlin  
- Problemas con proyectos KMP
- Configuración compleja para multiplataforma

## 🏗️ **Configuración Actual**

### **Ubicación de archivos clave:**
```
├── composeApp/build.gradle.kts          # Plugin Kover y tareas
├── gradle/libs.versions.toml            # Versiones de dependencias  
├── composeApp/src/commonTest/           # Tests unitarios comunes
├── composeApp/build/reports/kover/      # Reportes generados
│   ├── html/index.html                  # Reporte visual HTML
│   └── report.xml                       # Reporte datos XML
```

### **Plugin Kover configurado en `composeApp/build.gradle.kts`:**
```kotlin
plugins {
    // ...otros plugins...
    alias(libs.plugins.kover)
}

kover {
    reports {
        total {
            html { onCheck = true }
            xml { onCheck = true }
        }
    }
    
    currentProject {
        sources {
            excludedSourceSets.addAll("iosMain", "iosTest")
        }
        
        instrumentation {
            excludedClasses.addAll(
                "*.BuildConfig",
                "*.*\$\$serializer.*",
                "*.di.*",
                "*.*Test*.*",
                "android.*",
                "androidx.*"
            )
        }
    }
}
```

### **Dependencias de testing en `gradle/libs.versions.toml`:**
```toml
[versions]
kover = "0.9.0-RC"
mockk = "1.13.14"
kotlinx-coroutines-test = "1.9.0"

[libraries]
kotlin-test = { module = "org.jetbrains.kotlin:kotlin-test", version.ref = "kotlin" }
mockk = { module = "io.mockk:mockk", version.ref = "mockk" }
kotlinx-coroutines-test = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-test", version.ref = "kotlinx-coroutines-test" }
koin-test = { module = "io.insert-koin:koin-test", version.ref = "koin" }

[plugins]
kover = { id = "org.jetbrains.kotlinx.kover", version.ref = "kover" }
```

## 🔧 **Comandos Disponibles**

### **Tareas personalizadas configuradas:**
```bash
# 🚀 Comando principal: Tests + Coverage (recomendado)
./gradlew :composeApp:testAndroidWithCoverage

# 📊 Solo generar reportes (sin ejecutar tests)
./gradlew :composeApp:coverageReport

# 🧪 Solo ejecutar tests
./gradlew :composeApp:testDebugUnitTest

# 📈 Reportes específicos
./gradlew :composeApp:koverHtmlReport    # Solo HTML
./gradlew :composeApp:koverXmlReport     # Solo XML
```

### **Comandos de troubleshooting:**
```bash
# Limpiar antes de ejecutar
./gradlew clean

# Ver todas las tareas de Kover disponibles
./gradlew tasks --group=kover

# Ejecutar con más información
./gradlew :composeApp:testAndroidWithCoverage --info
```

## 📊 **Interpretación de Reportes**

### **Ubicaciones de reportes:**
- **HTML Visual**: `file:///[PATH]/composeApp/build/reports/kover/html/index.html`
- **XML Datos**: `composeApp/build/reports/kover/report.xml`

### **Métricas principales:**
| Métrica | Descripción | Valor objetivo |
|---------|-------------|----------------|
| **INSTRUCTION** | Bytecode cubierto | >80% |
| **LINE** | Líneas ejecutadas | >80% |
| **METHOD** | Métodos probados | >70% |
| **BRANCH** | Ramas condicionales | >70% |

### **Cómo leer el XML:**
```xml
<counter type="INSTRUCTION" missed="11537" covered="224"/>
<!-- Coverage = covered / (missed + covered) * 100 -->
<!-- Ejemplo: 224 / (11537 + 224) * 100 = ~1.9% -->
```

## 🔄 **Flujo de Trabajo TDD**

### **Ciclo Red-Green-Refactor:**
1. **🔴 RED**: Escribir test que falle
2. **🟢 GREEN**: Implementar código mínimo para pasar
3. **📊 COVERAGE**: Ejecutar `./gradlew :composeApp:testAndroidWithCoverage`
4. **🔵 REFACTOR**: Mejorar código manteniendo tests verdes
5. **🔁 REPEAT**: Volver al paso 1

### **Estructura de tests recomendada:**
```kotlin
@Test
fun `should do something when condition met`() = runTest {
    // Given - Preparar datos y mocks
    val input = "test input"
    fakeRepository.nextResult = Result.success(expectedOutput)
    
    // When - Ejecutar acción
    val result = useCase(input)
    
    // Then - Verificar resultado  
    assertTrue(result.isSuccess)
    assertEquals(expectedOutput, result.getOrNull())
}
```

## 🧪 **Patrones de Testing Implementados**

### **Use Case Testing:**
- **Ubicación**: `composeApp/src/commonTest/kotlin/org/efradev/todolist/domain/`
- **Patrón**: Fake Repository con resultado configurable
- **Ejemplo**: `CreateShoppingListUseCaseTest.kt`

### **ViewModel Testing:**
- **Ubicación**: `composeApp/src/commonTest/kotlin/org/efradev/todolist/viewmodel/`
- **Patrón**: TestableViewModel + Fake UseCase
- **Coroutines**: Usar `runTest` y `StandardTestDispatcher`

### **Fake Objects Pattern:**
```kotlin
class FakeRepository : Repository {
    var nextResult: Result<Data> = Result.success(defaultData)
    var lastInvokedParams: Params? = null
    
    override suspend fun operation(params: Params): Result<Data> {
        lastInvokedParams = params
        return nextResult
    }
}
```

## 🚨 **Problemas Conocidos y Soluciones**

### **Error: iOS compilation fails**
```
ERROR: 'actual fun getPreferencesRepository()' has no corresponding expected declaration
```
**Solución**: Usar tareas específicas de Android:
```bash ./gradlew :composeApp:testAndroidWithCoverage  # En lugar de allTests ```

### **Error: Low coverage percentage**
**Causa**: Normal en fases iniciales de proyecto
**Solución**: 
- Enfocar en coverage de lógica de negocio (Use Cases, ViewModels)
- UI tests requieren configuración adicional (no incluidos aún)

### **Error: Tests not running**
**Debug**:
```bash
./gradlew :composeApp:testDebugUnitTest --info
# Revisar logs para errores específicos
```

## 📈 **Métricas Actuales del Proyecto**

**Último reporte generado:**
| Métrica | Total | Cubierto | Coverage % |
|---------|-------|----------|------------|
| Instrucciones | 11,761 | 224 | ~1.9% |
| Líneas | 1,213 | 28 | ~2.3% |
| Métodos | 331 | 10 | ~3.0% |
| Clases | 204 | 8 | ~3.9% |

**Estado**: ✅ Sistema configurado, tests ejecutándose correctamente

## 🎯 **Próximos Pasos Recomendados**

### **Para mejorar coverage:**
1. **Use Cases**: Testear todos los casos de dominio
2. **ViewModels**: Agregar tests para todos los ViewModels de producción  
3. **Repository**: Tests con mocks de cliente HTTP
4. **Integration**: Tests de componentes Compose (requiere setup adicional)

### **Para mantener calidad:**
1. **Gate de CI**: Agregar threshold mínimo de coverage
2. **Pre-commit**: Ejecutar tests antes de commits
3. **Monitoreo**: Trackear evolución de coverage en el tiempo

## 🔍 **Comandos de Investigación para IA**

### **Para entender el estado actual:**
```bash
# Ver estructura de tests
find composeApp/src -name "*Test.kt" -type f

# Ver configuración de Kover
grep -r "kover" composeApp/build.gradle.kts

# Ver último reporte de coverage
cat composeApp/build/reports/kover/report.xml | tail -20

# Ver qué tests están disponibles
./gradlew :composeApp:tasks --group=verification
```

### **Para diagnosticar problemas:**
```bash
# Ver logs detallados
./gradlew :composeApp:testDebugUnitTest --info --stacktrace

# Verificar dependencias
./gradlew :composeApp:dependencies --configuration testImplementation

# Ver configuración de Kover
./gradlew :composeApp:koverPrintConfig
```

---

## 📚 **Referencias y Documentación**

- **Kover**: https://kotlin.github.io/kotlinx-kover/
- **Kotlin Test**: https://kotlinlang.org/api/latest/kotlin.test/
- **Coroutines Test**: https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/

---

**📝 Nota para IAs**: Este archivo debe actualizarse cuando se modifique la configuración de coverage o se agreguen nuevos patrones de testing. Siempre verificar la fecha de última actualización del archivo para asegurar vigencia de la información.

**Última actualización**: 28 de Junio, 2025
