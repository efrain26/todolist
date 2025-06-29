# 🚀 AI Quick Commands Reference

> **Para uso inmediato de IA**: Comandos más utilizados para trabajar con Code Coverage y TDD

## ⚡ **Comandos Más Usados**

```bash
# 🎯 COMANDO PRINCIPAL - Tests + Coverage
./gradlew :composeApp:testAndroidWithCoverage

# 🧪 Solo ejecutar tests  
./gradlew :composeApp:testDebugUnitTest

# 📊 Solo generar reportes
./gradlew :composeApp:coverageReport

# 🧹 Limpiar proyecto
./gradlew clean
```

## 📍 **Ubicaciones Clave**

```
📁 Reportes:        composeApp/build/reports/kover/html/index.html
📁 Tests:           composeApp/src/commonTest/kotlin/
📁 Configuración:   composeApp/build.gradle.kts
📁 Dependencias:    gradle/libs.versions.toml
```

## 🔍 **Verificación Rápida**

```bash
# ✅ Ver último coverage
tail -10 composeApp/build/reports/kover/report.xml

# ✅ Listar tests disponibles  
find composeApp/src -name "*Test.kt"

# ✅ Contar tests totales
find composeApp/src -name "*Test.kt" -exec grep -c "@Test" {} \; | awk '{sum+=$1} END {print "Total tests:", sum}'

# ✅ Estado de build
./gradlew :composeApp:build --dry-run
```

## 🚨 **Solución de Problemas Comunes**

| Problema | Comando Solución |
|----------|------------------|
| iOS compilation error | Usar `testAndroidWithCoverage` en lugar de `allTests` |
| Tests no se ejecutan | `./gradlew clean` + `./gradlew :composeApp:testDebugUnitTest --info` |
| Coverage 0% | Verificar que tests pasen primero |
| Reporte no se genera | `./gradlew :composeApp:koverHtmlReport --stacktrace` |

## 📊 **Interpretar Coverage Rápido**

```xml
<!-- En report.xml, buscar las líneas finales: -->
<counter type="INSTRUCTION" missed="X" covered="Y"/>
<!-- Coverage % = Y / (X + Y) * 100 -->
```

## 🎯 **Flujo TDD en 3 Pasos**

1. **Escribir test** → Debe fallar (🔴 RED)
2. **Implementar código** → Test pasa (🟢 GREEN) 
3. **Ejecutar coverage** → `./gradlew :composeApp:testAndroidWithCoverage`

---
**Última actualización**: 28 de Junio, 2025
