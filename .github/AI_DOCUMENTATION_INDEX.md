# 🤖 AI Documentation Index

> **Entrada principal para IAs trabajando en este proyecto**

## Importante para todas las IA's leer este documentacion antes de realizar cualquier cambio, y solo actualizar este documento si es necesario agregar documentacion importante como arquitectura, buenas practicas, patrones de diseño, etc. (No usar este archivo como un CHANGELOG)

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

### **Para todas las IAs que quieran realizar cualquier cambio en el proyecto:**
1. Leer **AI_CODE_COVERAGE_CONTEXT.md** completo
2. Consultar **AI_TESTING_PATTERNS.md** para patrones
3. Revisar **AI_UI_PATTERNS_GUIDE.md** para UI/previews
4. Usar **AI_QUICK_COMMANDS.md** para comandos frecuentes

### **Para debugging:**
1. Verificar comandos en **AI_QUICK_COMMANDS.md**
2. Consultar troubleshooting en **AI_CODE_COVERAGE_CONTEXT.md**
3. Revisar anti-patterns en **AI_TESTING_PATTERNS.md**
4. Verificar patrones UI en **AI_UI_PATTERNS_GUIDE.md**


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
