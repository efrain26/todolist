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

### **🏗️ [AI_CLEAN_ARCHITECTURE_GUIDE.md](./AI_CLEAN_ARCHITECTURE_GUIDE.md)**
**Guía de Clean Architecture** - Separación de capas, modelos de dominio y mejores prácticas.
- ✅ Principios fundamentales de Clean Architecture
- ✅ Violaciones identificadas y soluciones
- ✅ Separación de Domain Models vs Data Models
- ✅ Plan de implementación por fases

### **🎨 [AI_UI_PATTERNS_GUIDE.md](./AI_UI_PATTERNS_GUIDE.md)**
**Patrones de UI y Preview** - Organización de la capa de presentación y previews.
- ✅ Patrón de archivos de preview separados
- ✅ Separación de responsabilidades en UI
- ✅ Múltiples estados por componente
- ✅ Template para nuevos componentes

## 🎯 **Cómo usar esta documentación**

### **Para IAs nuevas en el proyecto:**
1. Leer **AI_CODE_COVERAGE_CONTEXT.md** completo
2. Consultar **AI_TESTING_PATTERNS.md** para patrones
3. Revisar **AI_UI_PATTERNS_GUIDE.md** para UI/previews
4. Usar **AI_QUICK_COMMANDS.md** para comandos frecuentes

### **Para IAs con contexto previo:**
1. Consultar **AI_QUICK_COMMANDS.md** directamente
2. Verificar fechas de actualización por cambios

### **Para debugging:**
1. Verificar comandos en **AI_QUICK_COMMANDS.md**
2. Consultar troubleshooting en **AI_CODE_COVERAGE_CONTEXT.md**
3. Revisar anti-patterns en **AI_TESTING_PATTERNS.md**
4. Verificar patrones UI en **AI_UI_PATTERNS_GUIDE.md**

## 🔄 **Estado Actual del Sistema**

✅ **Configurado y funcionando:**
- Kover plugin instalado y configurado
- Tests unitarios ejecutándose (45 tests pasando)
- Reportes HTML y XML generándose correctamente
- Tareas personalizadas disponibles
- **Clean Architecture implementada completamente** 🏗️
- **UI Preview Patterns implementados completamente** 🎨

📊 **Coverage actual:** 
- **Estado:** 45 tests ejecutándose exitosamente ✅
- **Arquitectura:** Separación limpia entre capas Data → Domain ← Presentation
- **Tests:** Todos adaptados a modelos de dominio
- **UI:** Previews organizados con separación de responsabilidades ✅

🎯 **Tests implementados y funcionando:**
- ✅ CheckAuthStateUseCase (4 tests) - **100% coverage** con modelos de dominio
- ✅ CheckUserExistsUseCase (6 tests) - **100% coverage** con modelos de dominio
- ✅ CreateShoppingListUseCase (8 tests) - **100% coverage** con modelos de dominio
- ✅ GetShoppingListsUseCase (4 tests) - **100% coverage** con modelos de dominio
- ✅ LoginUseCase (5 tests) - **100% coverage** con modelos de dominio
- ✅ LogoutUseCase (4 tests) - **100% coverage** con modelos de dominio
- ✅ RegisterUserUseCase (6 tests) - **100% coverage** con modelos de dominio
- ✅ CreateListViewModel (7 tests) - Usando modelos de dominio
- ✅ ComposeAppCommon (1 test)

🎯 **Total:** 45 tests ejecutándose exitosamente con arquitectura limpia ✅

📁 **Archivos de test actualizados y funcionando:**
- CheckAuthStateUseCaseTest.kt ✅
- CheckUserExistsUseCaseTest.kt ✅
- CreateShoppingListUseCaseTest.kt ✅
- GetShoppingListsUseCaseTest.kt ✅
- LoginUseCaseTest.kt ✅
- LogoutUseCaseTest.kt ✅
- RegisterUserUseCaseTest.kt ✅
- CreateListViewModelTest.kt ✅
- TestFakes.kt - Clases fake actualizadas para Clean Architecture ✅

�️ **Clean Architecture Completamente Implementada:**
- ✅ Domain Models independientes (DomainUser, DomainAuthData, etc.)
- ✅ Repository Interfaces en capa de dominio
- ✅ Use Cases usando únicamente modelos de dominio
- ✅ Mappers para conversión Data ↔ Domain
- ✅ Tests completamente migrados y funcionando
- ✅ ViewModels adaptados a modelos de dominio
- ✅ Zero violaciones arquitectónicas

🎯 **Estado:** ✅ **MIGRACIÓN ARQUITECTÓNICA COMPLETADA CON ÉXITO**

## 📅 **Mantenimiento**

**Actualizar cuando:**
- Se modifique configuración de Kover
- Se cambien comandos o tareas
- Se agreguen nuevos patrones de testing
- Se encuentren nuevos problemas/soluciones

**Última actualización completa:** 29 de Junio, 2025 - **CLEAN ARCHITECTURE + UI PATTERNS COMPLETED** ✅ 

**🎉 MIGRATION EXITOSA:**
✅ **Arquitectura Clean completamente implementada y funcionando**
✅ **45 tests pasando con modelos de dominio**
✅ **Zero violaciones arquitectónicas**
✅ **Separación limpia entre capas: Data → Domain ← Presentation**
✅ **Coverage mantenido al 100% en Use Cases**
✅ **UI Preview Patterns implementados completamente**

**📊 Estado final:**
- **Tests:** 45/45 pasando (100% success rate)
- **Domain Models:** Implementados y funcionando
- **Repository Pattern:** Interfaces en Domain, implementaciones en Data
- **Use Cases:** Solo modelos de dominio, zero dependencias de Data layer
- **Mappers:** Conversión automática Data ↔ Domain
- **Tests:** Completamente adaptados con TestFakes actualizados
- **UI Previews:** 16 previews en 7 archivos organizados por separación de responsabilidades

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
