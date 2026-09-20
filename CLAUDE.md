# CLAUDE.md

Este archivo da contexto a Claude Code (claude.ai/code) para trabajar en este repositorio.

## Proyecto

LigorTravel: app Android nativa (Kotlin + Jetpack Compose + Material 3), sin layouts XML. Es una sola `Activity` manejada por Navigation Compose. Paquete raíz: `com.example.ligortravel`, mismo `applicationId`.

En `.claude/skills/` hay una skill específica del proyecto, `desarrollo-app-movil`, que se aplica automáticamente a tareas de desarrollo/depuración/explicación — define la persona (dev Android senior + tutor para un estudiante de Ingeniería de Sistemas), el flujo de "explicar antes de cambios grandes" y el idioma (responder en español). Léela antes de hacer trabajo de desarrollo sustancial en este repo; este archivo solo cubre comandos y arquitectura.

## Comandos

Build (Git Bash/WSL): `./gradlew <tarea>` — en PowerShell/cmd de Windows usar `.\gradlew.bat <tarea>`.

- `assembleDebug` — compila el APK de debug
- `installDebug` — compila e instala el APK de debug en un dispositivo/emulador conectado
- `test` — corre los tests unitarios de JVM (`app/src/test`)
- `test --tests "com.example.ligortravel.NombreDelTest"` — corre una sola clase de test unitario
- `connectedAndroidTest` — corre los tests instrumentados (`app/src/androidTest`); requiere un dispositivo/emulador conectado
- `lint` — corre Android Lint
- `clean` — limpia los outputs del build

Por ahora solo hay tests de plantilla (`ExampleUnitTest`, `ExampleInstrumentedTest`) — todavía no existen tests reales para los ViewModels ni las pantallas.

## Arquitectura

**Punto de entrada / navegación**: `MainActivity` envuelve todo en `LigorTravelTheme` y renderiza `LigorNavGraph` (`ui/navigation/LigorNavGraph.kt`). Por ahora hay dos rutas, definidas en `object Rutas`: `AUTH` (destino inicial) y `PERFIL`. Ambas transiciones (`Auth → Perfil` y `Perfil → Auth`) sacan la ruta anterior del back stack (`popUpTo(..., inclusive = true)`), así que no hay navegación hacia atrás entre el estado autenticado y el no autenticado.

**Patrón de pantalla (MVVM, adoptado progresivamente)**: cada pantalla tiene un `ViewModel` con un único `data class ...UiState` detrás de un `MutableStateFlow` (expuesto como `StateFlow` de solo lectura). Los ViewModels construyen su propio `UserRepository()` por defecto en vez de recibirlo por inyección de dependencias (ej. `LoginViewModel(private val repository: UserRepository = UserRepository())`). Los cambios de campo pasan por métodos `onXChange(valor)` que actualizan el estado y limpian `error`; las acciones de envío (`iniciarSesion()`, `registrar()`, `guardarCambios()`) validan de forma síncrona y luego lanzan una corrutina en `viewModelScope` que llama al repositorio y traduce `Result.onSuccess/onFailure` a los campos `cargando`/`error`/`...Exitoso`. Sigue exactamente esta forma al agregar nuevas pantallas/ViewModels — ver `ui/login/LoginViewModel.kt`, `ui/registro/RegistroViewModel.kt`, `ui/perfil/PerfilViewModel.kt`.

**`AuthScreen` (`ui/auth/AuthScreen.kt`)** es una sola pantalla que combina login y registro con tabs (no son dos rutas separadas) — internamente maneja un `LoginViewModel` y, presumiblemente, un flujo de registro. Los botones de invitado, "olvidé mi contraseña", Google y Apple son solo visuales, sin funcionalidad conectada (así fue decidido a propósito — ver memoria del proyecto).

**Capa de datos (`data/`)**: la app migró de un backend mock (mockapi.io) a **Firebase Auth + Firestore**. El flujo actual:
- `AuthRepository` — envuelve `FirebaseAuth` (`crearCuenta`/`iniciarSesion` sobre `createUserWithEmailAndPassword`/`signInWithEmailAndPassword`) en `Result<String>`, devolviendo el `uid` de Firebase. La contraseña ya no se maneja ni se guarda en la app; la valida Firebase Auth del lado del servidor.
- `UserRepository` — ya **no usa Retrofit**; envuelve `FirebaseFirestore` (colección `usuarios`) en `Result<RegistroUsuario>` (`guardarPerfil`, `obtenerPerfil(id)`). El `id` documento normalmente es el `uid` que devuelve `AuthRepository`.
- `RegistroUsuario` — modelo de perfil (`id` con `@DocumentId` de Firestore, `nombre`, `email`, `telefono`); ya **no tiene el campo `password`**.
- El flujo típico de login/registro es: `AuthRepository` (obtiene `uid`) → `UserRepository.obtenerPerfil`/`guardarPerfil` (lee/escribe el perfil en Firestore) → `SessionManager.iniciarSesion(usuario)`. Ver `ui/login/LoginViewModel.kt` para el patrón exacto.
- `ApiService` y `RetrofitClient` (el cliente Retrofit contra mockapi.io) fueron **eliminados** por no tener ya ningún uso tras la migración a Firebase. No reintroducir llamadas Retrofit/mockapi en pantallas nuevas.

**Sesión (`session/SessionManager.kt`)**: singleton `object` en memoria que guarda `usuarioActual: RegistroUsuario?`. No hay persistencia (sin DataStore/SharedPreferences) — la sesión se pierde al matar el proceso o reiniciar la app, y no existe el concepto de token/expiración.

**Theming (`ui/theme/`)**: hay dos esquemas de color en competencia. `Theme.kt` todavía define el esquema morado por defecto de la plantilla de Compose (`DarkColorScheme`/`LightColorScheme`, con dynamic color de Material You en API 31+) y es lo que `LigorTravelTheme` aplica globalmente en realidad. `LigorDarkPalette.kt` define el sistema de diseño real elegido (`LigorDarkColorScheme`: fondo oscuro, acento `primary` naranja) que coincide con el boceto de diseño aprobado, pero **todavía no está conectado a `Theme.kt`** — solo las pantallas más nuevas lo referencian directamente. Al tocar theming o crear pantallas nuevas, preferir `LigorDarkColorScheme`/`LigorDarkPalette.kt` y señalar la inconsistencia de `Theme.kt` en vez de volver silenciosamente a los valores morados por defecto.

**Notas de configuración de build**: Gradle con Kotlin DSL y catálogo de versiones (`gradle/libs.versions.toml`); AGP 9.3.2, Kotlin 2.2.10, `compileSdk`/`targetSdk` 37, `minSdk` 31. El build type `release` tiene `optimization.enable = false` (R8/minify apagado), a diferencia del valor por defecto de AGP. Firebase está conectado vía `google-services.json` y ya se usa activamente: `firebase-analytics`, **Firebase Auth** (login/registro, ver `AuthRepository`) y **Firestore** (perfil de usuario, ver `UserRepository`).
