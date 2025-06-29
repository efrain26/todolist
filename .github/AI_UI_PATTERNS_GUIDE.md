# 🎨 AI UI Patterns Guide

> **Patrones de diseño UI para Compose Multiplatform**

## 📋 **Resumen**
Este documento define los patrones de UI establecidos para el proyecto TodoList, enfocándose en la separación de responsabilidades y la consistencia en la capa de presentación.

## 🔧 **Patrones de Preview**

### **🎯 Patrón: Archivos de Preview Separados**

**Ubicación:** `composeApp/src/commonMain/kotlin/org/efradev/todolist/ui/preview/`

**Convención de nombres:**
- `{ComponentName}Preview.kt` para componentes
- `{ScreenName}Preview.kt` para pantallas

**Estructura estándar:**
```kotlin
package org.efradev.todolist.ui.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.efradev.todolist.ui.screens.{ScreenName}
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun {ScreenName}Preview() {
    MaterialTheme {
        {ScreenName}(
            // Parámetros mock apropiados
        )
    }
}

@Preview
@Composable
fun {ScreenName}VariantPreview() {
    MaterialTheme {
        {ScreenName}(
            // Parámetros para casos específicos
        )
    }
}
```

### **✅ Archivos de Preview Implementados**

#### **Screens:**
- `LoginFormPreview.kt` - Estados de login normal y con email largo
- `RegisterFormPreview.kt` - Estados de registro normal y con email largo
- `EmailFormPreview.kt` - Formulario de email vacío y con datos
- `ShoppingListsScreenPreview.kt` - Lista vacía, con elementos y estado único
- `SplashScreenPreview.kt` - Pantalla de carga

#### **Components:**
- `CreateListBottomSheetPreview.kt` - Estados idle, loading y error
- `CategoryChipCarouselPreview.kt` - Diferentes configuraciones de chips

### **🎨 Principios de Design**

#### **1. Múltiples Estados por Componente**
Cada archivo de preview debe incluir:
- Estado normal/ideal
- Estados de error o vacío
- Casos extremos (textos largos, datos abundantes)

#### **2. Datos Mock Realistas**
```kotlin
// ✅ Buena práctica - Datos realistas
val mockShoppingLists = listOf(
    DomainShoppingList(
        id = "1",
        name = "Compras de la semana",
        createdAt = "2025-06-24T10:00:00",
        userId = "user123",
        type = "simple",
        items = listOf(
            DomainShoppingItem(
                name = "Frutas",
                status = "pendiente",
                type = "simple"
            )
        )
    )
)

// ❌ Mala práctica - Datos genéricos
val mockData = listOf("Item 1", "Item 2")
```

#### **3. Wrapping con MaterialTheme**
```kotlin
// ✅ Siempre usar MaterialTheme
@Preview
@Composable
fun ComponentPreview() {
    MaterialTheme {
        Component(...)
    }
}
```

### **🔄 Migración de Previews Existentes**

#### **Pasos realizados:**
1. ✅ Crear archivos de preview separados en `ui/preview/`
2. ✅ Migrar todos los previews existentes
3. ✅ Eliminar previews de archivos originales
4. ✅ Limpiar imports innecesarios (@Preview, initKoin, etc.)

#### **Archivos limpiados:**
- `LoginForm.kt` - Removido preview y imports
- `RegisterForm.kt` - Removido preview y imports  
- `EmailForm.kt` - Removido preview y imports
- `ShoppingListsScreen.kt` - Removido preview y imports

### **📁 Estructura Final**

```
composeApp/src/commonMain/kotlin/org/efradev/todolist/ui/
├── screens/
│   ├── LoginForm.kt               # Sin previews
│   ├── RegisterForm.kt            # Sin previews
│   ├── EmailForm.kt               # Sin previews
│   ├── ShoppingListsScreen.kt     # Sin previews
│   └── SplashScreen.kt            # Sin previews
├── components/
│   ├── CreateListBottomSheet.kt   # Sin previews
│   └── CategoryChipCarousel.kt    # Sin previews
└── preview/
    ├── LoginFormPreview.kt        # Previews de LoginForm
    ├── RegisterFormPreview.kt     # Previews de RegisterForm
    ├── EmailFormPreview.kt        # Previews de EmailForm
    ├── ShoppingListsScreenPreview.kt  # Previews de ShoppingListsScreen
    ├── SplashScreenPreview.kt     # Previews de SplashScreen
    ├── CreateListBottomSheetPreview.kt  # Previews de CreateListBottomSheet
    └── CategoryChipCarouselPreview.kt   # Previews de CategoryChipCarousel
```

## 🎯 **Ventajas del Patrón**

### **🔍 Separación de Responsabilidades**
- Componentes de UI limpios sin código de preview
- Previews organizados y fáciles de encontrar
- Imports reducidos en archivos de producción

### **🎨 Mejor Experiencia de Desarrollo**
- Múltiples estados visualizables por componente
- Datos mock realistas para mejor comprensión
- Navegación clara entre componentes y sus previews

### **📊 Mantenibilidad**
- Cambios en previews no afectan código de producción
- Fácil agregar nuevos estados sin cluttering
- Consistencia en la estructura de todos los previews

## 📝 **Guías para Nuevos Componentes**

### **Al crear un nuevo componente:**

1. **Crear el componente** en `ui/screens/` o `ui/components/`
2. **Crear archivo de preview** en `ui/preview/`
3. **Implementar múltiples estados:**
   - Estado normal
   - Estado de error (si aplica)
   - Estado vacío (si aplica)
   - Casos extremos (textos largos, muchos datos)

### **Template para nuevos previews:**

```kotlin
package org.efradev.todolist.ui.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.efradev.todolist.ui.{screens/components}.{ComponentName}
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun {ComponentName}Preview() {
    MaterialTheme {
        {ComponentName}(
            // Parámetros normales
        )
    }
}

@Preview
@Composable
fun {ComponentName}ErrorPreview() {
    MaterialTheme {
        {ComponentName}(
            // Parámetros de error
        )
    }
}

@Preview
@Composable
fun {ComponentName}EmptyPreview() {
    MaterialTheme {
        {ComponentName}(
            // Estado vacío
        )
    }
}
```

## 🔄 **Mantenimiento**

**Actualizar cuando:**
- Se agreguen nuevos componentes o pantallas
- Se modifiquen estados existentes
- Se identifiquen nuevos casos extremos

**Última actualización:** 29 de Junio, 2025 - **UI PREVIEW PATTERNS IMPLEMENTED** ✅

---

## 🚀 **Estado Actual**

✅ **Implementado completamente:**
- 7 archivos de preview creados
- Todos los previews migrados exitosamente
- Archivos originales limpiados
- Imports optimizados
- Estructura consistente establecida

✅ **Archivos de preview funcionando:**
- LoginFormPreview.kt (2 estados)
- RegisterFormPreview.kt (2 estados)
- EmailFormPreview.kt (2 estados)
- ShoppingListsScreenPreview.kt (3 estados)
- SplashScreenPreview.kt (1 estado)
- CreateListBottomSheetPreview.kt (3 estados)
- CategoryChipCarouselPreview.kt (3 estados)

🎯 **Total:** 16 previews organizados en 7 archivos especializados

**📊 Beneficios obtenidos:**
- Separación clara de responsabilidades
- Código de producción más limpio
- Múltiples estados por componente
- Estructura mantenible y escalable
