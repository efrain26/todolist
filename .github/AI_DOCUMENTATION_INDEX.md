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
- Tests unitarios ejecutándose (12 tests pasando)
- Reportes HTML y XML generándose correctamente
- Tareas personalizadas disponibles

📊 **Coverage actual:** ~2-3% (normal en fases iniciales)

🎯 **Próximo objetivo:** Incrementar coverage agregando más tests de Use Cases y ViewModels

## 📅 **Mantenimiento**

**Actualizar cuando:**
- Se modifique configuración de Kover
- Se cambien comandos o tareas
- Se agreguen nuevos patrones de testing
- Se encuentren nuevos problemas/soluciones

**Última actualización completa:** 28 de Junio, 2025

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
