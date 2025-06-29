# 🤖 AI Documentation Index

> **Entrada principal para IAs trabajando en este proyecto**

## 📚 **Archivos de Documentación AI**

### **🚀 [AI_QUICK_COMMANDS.md](./AI_QUICK_COMMANDS.md)**
**Para uso inmediato** - Comandos más utilizados, ubicaciones clave y soluciones rápidas.
```bash
# Comando principal
./gradlew :composeApp:testAndroidWithCoverage
```

### **📋 [AI_CODE_COVERAGE_CONTEXT.md](./AI_CODE_COVERAGE_CONTEXT.md)**  
**Contexto completo** - Configuración, decisiones técnicas, flujo TDD y troubleshooting.
- ✅ ¿Por qué Kover vs JaCoCo?
- ✅ Configuración completa
- ✅ Interpretación de reportes
- ✅ Flujo de trabajo TDD

### **🧪 [AI_TESTING_PATTERNS.md](./AI_TESTING_PATTERNS.md)**
**Patrones de testing** - Arquitectura, ejemplos y anti-patterns.
- ✅ Use Case testing patterns
- ✅ ViewModel testing strategies  
- ✅ Fake objects implementation
- ✅ Coverage strategies por capa

## 🎯 **Cómo usar esta documentación**

### **Para IAs nuevas en el proyecto:**
1. Leer **AI_CODE_COVERAGE_CONTEXT.md** completo
2. Consultar **AI_TESTING_PATTERNS.md** para patrones
3. Usar **AI_QUICK_COMMANDS.md** para comandos frecuentes

### **Para IAs con contexto previo:**
1. Consultar **AI_QUICK_COMMANDS.md** directamente
2. Verificar fechas de actualización por cambios

### **Para debugging:**
1. Verificar comandos en **AI_QUICK_COMMANDS.md**
2. Consultar troubleshooting en **AI_CODE_COVERAGE_CONTEXT.md**
3. Revisar anti-patterns en **AI_TESTING_PATTERNS.md**

## 🔄 **Estado Actual del Sistema**

✅ **Configurado y funcionando:**
- Kover plugin instalado y configurado
- Tests unitarios ejecutándose (45 tests pasando)
- Reportes HTML y XML generándose correctamente
- Tareas personalizadas disponibles

📊 **Coverage actual:** 
- **Líneas:** 10.72% (130/1213 líneas) - ⬆️ +4.72%
- **Branches:** 8.30% (24/289 branches) - ⬆️ +2.30%
- **Instrucciones:** 6.49% (764/11763 instrucciones) - ⬆️ +2.49%

🎯 **Tests implementados:**
- ✅ CheckAuthStateUseCase (4 tests) - **100% coverage**
- ✅ CheckUserExistsUseCase (6 tests) - **100% coverage**
- ✅ CreateShoppingListUseCase (8 tests) - **100% coverage**
- ✅ GetShoppingListsUseCase (4 tests) - **100% coverage** ⭐ NUEVO
- ✅ LoginUseCase (5 tests) - **100% coverage** ⭐ NUEVO
- ✅ LogoutUseCase (4 tests) - **100% coverage** ⭐ NUEVO
- ✅ RegisterUserUseCase (6 tests) - **100% coverage** ⭐ NUEVO
- ✅ CreateListViewModel (7 tests)
- ✅ ComposeAppCommon (1 test)

🎯 **Total:** 45 tests ejecutándose exitosamente (+19 tests nuevos)

📁 **Archivos de test creados/actualizados:**
- CheckAuthStateUseCaseTest.kt (NUEVO)
- CheckUserExistsUseCaseTest.kt (NUEVO)
- CreateShoppingListUseCaseTest.kt (MEJORADO)
- GetShoppingListsUseCaseTest.kt (NUEVO) ⭐
- LoginUseCaseTest.kt (NUEVO) ⭐
- LogoutUseCaseTest.kt (NUEVO) ⭐
- RegisterUserUseCaseTest.kt (NUEVO) ⭐
- TestFakes.kt (NUEVO) - Clases fake reutilizables ⭐

🎯 **Próximo objetivo:** Continuar agregando tests de ViewModels y Repository layers para incrementar coverage general

## 📅 **Mantenimiento**

**Actualizar cuando:**
- Se modifique configuración de Kover
- Se cambien comandos o tareas
- Se agreguen nuevos patrones de testing
- Se encuentren nuevos problemas/soluciones

**Última actualización completa:** 29 de Junio, 2025 - Segundo incremento significativo: +19 tests de Use Cases (GetShoppingListsUseCase, LoginUseCase, LogoutUseCase, RegisterUserUseCase)

---

## 🚀 **Quick Start para IAs**

```bash
# 1. Ejecutar tests y coverage
./gradlew :composeApp:testAndroidWithCoverage

# 2. Ver reportes
open composeApp/build/reports/kover/html/index.html

# 3. Verificar archivos de test
find composeApp/src -name "*Test.kt"
```

**Archivos de ejemplo para referencia:**
- `composeApp/src/commonTest/kotlin/org/efradev/todolist/domain/CreateShoppingListUseCaseTest.kt`
- `composeApp/src/commonTest/kotlin/org/efradev/todolist/viewmodel/CreateListViewModelTest.kt`
