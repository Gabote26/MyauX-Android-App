package com.myaux.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.myaux.app.ui.AppState
import com.myaux.app.ui.MainViewModel
import com.myaux.app.ui.screens.*
import com.myaux.app.ui.theme.MyauXTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyauXTheme {
                MyauXApp()
            }
        }
    }
}

@Composable
fun MyauXApp(viewModel: MainViewModel = viewModel()) {
    val appState by viewModel.appState.collectAsState()
    val usuario by viewModel.usuario.collectAsState()
    val loginState by viewModel.loginState.collectAsState()
    val isLoginLoading by viewModel.isLoginLoading.collectAsState()
    val calificaciones by viewModel.calificaciones.collectAsState()
    val calificacionesLoading by viewModel.calificacionesLoading.collectAsState()
    val asistencias by viewModel.asistencias.collectAsState()
    val estadisticasAsistencia by viewModel.estadisticasAsistencia.collectAsState()
    val asistenciasLoading by viewModel.asistenciasLoading.collectAsState()
    val selectedMateria by viewModel.selectedMateria.collectAsState()
    val mensajes by viewModel.mensajes.collectAsState()
    val mensajesLoading by viewModel.mensajesLoading.collectAsState()
    val unreadCount by viewModel.unreadCount.collectAsState()

    // Profesor state
    val materiasProfesor by viewModel.materiasProfesor.collectAsState()
    val gruposProfesor by viewModel.gruposProfesor.collectAsState()
    val alumnosAsistencia by viewModel.alumnosAsistencia.collectAsState()
    val alumnosCalificacion by viewModel.alumnosCalificacion.collectAsState()
    val profesorLoading by viewModel.profesorLoading.collectAsState()
    val alumnosParaMensaje by viewModel.alumnosParaMensaje.collectAsState()

    // Current screen state
    var currentScreen by remember { mutableStateOf("main") }
    var showSplash by remember { mutableStateOf(true) }

    // Back handler - navigate to main instead of closing app
    BackHandler(enabled = currentScreen != "main" && appState is AppState.Dashboard) {
        currentScreen = "main"
    }

    if (showSplash && appState is AppState.Login) {
        SplashScreen(onFinished = { showSplash = false })
        return
    }

    AnimatedContent(
        targetState = appState,
        transitionSpec = {
            fadeIn(tween(400)) togetherWith fadeOut(tween(400))
        },
        modifier = Modifier.fillMaxSize(),
        label = "app_state_transition"
    ) { state ->
        when (state) {
            is AppState.Loading -> {
                SplashScreen(onFinished = {})
            }

            is AppState.Splash -> {
                SplashScreen(onFinished = { showSplash = false })
            }

            is AppState.Login -> {
                LoginScreen(
                    loginState = loginState,
                    onLogin = { email, password -> viewModel.login(email, password) },
                    isLoading = isLoginLoading
                )
            }

            is AppState.Dashboard -> {
                val user = usuario ?: return@AnimatedContent
                val isProfesor = user.role.equals("profesor", ignoreCase = true)

                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = {
                        if (targetState == "main") {
                            slideInHorizontally { -it } + fadeIn() togetherWith slideOutHorizontally { it } + fadeOut()
                        } else {
                            slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                        }
                    },
                    label = "screen_navigation"
                ) { screen ->
                    when (screen) {
                        "main" -> {
                            DashboardScreen(
                                usuario = user,
                                unreadMessages = unreadCount,
                                onNavigateCalificaciones = {
                                    viewModel.loadCalificaciones()
                                    currentScreen = "calificaciones"
                                },
                                onNavigateAsistencias = {
                                    viewModel.loadAsistencias()
                                    currentScreen = "asistencias"
                                },
                                onNavigateHorarios = { currentScreen = "horarios" },
                                onNavigateMensajes = {
                                    viewModel.loadMensajes()
                                    currentScreen = "mensajes"
                                },
                                onNavigateConfig = { currentScreen = "config" },
                                onNavigateAsistenciasProfesor = {
                                    viewModel.loadProfesorData()
                                    currentScreen = "asistencias_profesor"
                                },
                                onNavigateCalificacionesProfesor = {
                                    viewModel.loadProfesorData()
                                    currentScreen = "calificaciones_profesor"
                                },
                                onNavigateMensajesProfesor = {
                                    viewModel.loadMensajes()
                                    viewModel.loadProfesorData()
                                    currentScreen = "mensajes_profesor"
                                },
                                onLogout = { viewModel.logout() }
                            )
                        }

                        "calificaciones" -> {
                            CalificacionesScreen(
                                calificaciones = calificaciones,
                                nombre = user.nombre,
                                apellido = user.apellido,
                                numControl = user.numControl,
                                isLoading = calificacionesLoading,
                                onRefresh = { viewModel.loadCalificaciones() },
                                onBack = { currentScreen = "main" }
                            )
                        }

                        "asistencias" -> {
                            AsistenciasScreen(
                                asistencias = asistencias,
                                estadisticas = estadisticasAsistencia,
                                nombreCompleto = "${user.apellido} ${user.nombre}",
                                numControl = user.numControl,
                                isLoading = asistenciasLoading,
                                selectedMateria = selectedMateria,
                                materias = viewModel.materias,
                                onMateriaSelected = { viewModel.onMateriaSelected(it) },
                                onRefresh = { viewModel.loadAsistencias() },
                                onBack = { currentScreen = "main" }
                            )
                        }

                        "horarios" -> {
                            HorariosScreen(onBack = { currentScreen = "main" })
                        }

                        "mensajes" -> {
                            MensajesScreen(
                                mensajes = mensajes,
                                unreadCount = unreadCount,
                                isLoading = mensajesLoading,
                                isProfesor = isProfesor,
                                grupos = gruposProfesor,
                                alumnosParaMensaje = alumnosParaMensaje,
                                onMarkRead = { viewModel.markMessageRead(it) },
                                onRefresh = { viewModel.loadMensajes() },
                                onBack = { currentScreen = "main" },
                                onLoadAlumnosGrupo = { viewModel.loadAlumnosParaMensaje(it) },
                                onEnviarMensaje = { dest, asunto, contenido ->
                                    viewModel.enviarMensajeProfesor(dest, asunto, contenido)
                                }
                            )
                        }

                        "config" -> {
                            ConfigScreen(onBack = { currentScreen = "main" })
                        }

                        "asistencias_profesor" -> {
                            AsistenciasProfesorScreen(
                                materias = materiasProfesor,
                                alumnos = alumnosAsistencia,
                                isLoading = profesorLoading,
                                onSelectGrupo = { grupoNombre, classDate -> 
                                    viewModel.loadAlumnosAsistencia(grupoNombre, classDate)
                                },
                                onSaveAsistencias = { alumnos, materia, classDate ->
                                    viewModel.saveAsistencias(alumnos, materia, classDate)
                                },
                                onBack = { currentScreen = "main" }
                            )
                        }

                        "calificaciones_profesor" -> {
                            var selectedMateriaProf by remember { mutableStateOf<String?>(null) }
                            var selectedGrupoNombre by remember { mutableStateOf<String?>(null) }

                            CalificacionesProfesorScreen(
                                materias = materiasProfesor,
                                alumnos = alumnosCalificacion,
                                isLoading = profesorLoading,
                                onSelectMateria = { selectedMateriaProf = it },
                                onSelectGrupo = { grupoNombre ->
                                    selectedGrupoNombre = grupoNombre
                                    if (selectedMateriaProf != null) {
                                        viewModel.loadAlumnosCalificacion(grupoNombre, selectedMateriaProf!!)
                                    }
                                },
                                onSaveCalificaciones = { alumnos, materia ->
                                    viewModel.saveCalificaciones(alumnos, materia)
                                },
                                onBack = { currentScreen = "main" }
                            )
                        }

                        "mensajes_profesor" -> {
                            MensajesScreen(
                                mensajes = mensajes,
                                unreadCount = unreadCount,
                                isLoading = mensajesLoading,
                                isProfesor = true,
                                grupos = gruposProfesor,
                                alumnosParaMensaje = alumnosParaMensaje,
                                onMarkRead = { viewModel.markMessageRead(it) },
                                onRefresh = { viewModel.loadMensajes() },
                                onBack = { currentScreen = "main" },
                                onLoadAlumnosGrupo = { viewModel.loadAlumnosParaMensaje(it) },
                                onEnviarMensaje = { dest, asunto, contenido ->
                                    viewModel.enviarMensajeProfesor(dest, asunto, contenido)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
