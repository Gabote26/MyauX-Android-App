package com.myaux.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myaux.app.data.model.Usuario
import com.myaux.app.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    usuario: Usuario,
    unreadMessages: Int,
    onNavigateCalificaciones: () -> Unit,
    onNavigateAsistencias: () -> Unit,
    onNavigateHorarios: () -> Unit,
    onNavigateMensajes: () -> Unit,
    onNavigateConfig: () -> Unit,
    onNavigateAsistenciasProfesor: () -> Unit = {},
    onNavigateCalificacionesProfesor: () -> Unit = {},
    onNavigateMensajesProfesor: () -> Unit = {},
    onLogout: () -> Unit
) {
    val isProfesor = usuario.role.equals("profesor", ignoreCase = true)

    // Staggered entry animations
    val items = mutableListOf(
        Triple("📬 Mis Mensajes", AccentBlueBright, onNavigateMensajes),
    )

    if (!isProfesor) {
        items.add(Triple("📝 Mis Calificaciones", AccentPurple, onNavigateCalificaciones))
        items.add(Triple("📊 Mis Asistencias", AccentGreen, onNavigateAsistencias))
    }

    items.add(Triple("🗓️ Horarios", Color(0xFFBEBEBE), onNavigateHorarios))

    // Add teacher-specific items
    if (isProfesor) {
        items.add(Triple("📋 Gestionar Asistencias", Color(0xFF27AE60), onNavigateAsistenciasProfesor))
        items.add(Triple("📝 Gestionar Calificaciones", Color(0xFF8E44AD), onNavigateCalificacionesProfesor))
        items.add(Triple("✉️ Enviar Mensajes", Color(0xFF2980B9), onNavigateMensajesProfesor))
    }

    items.add(Triple("⚙️ Configuración", Color(0xFF7F8C8D), onNavigateConfig))

    val visibleStates = remember(items.size) { items.map { mutableStateOf(false) } }

    LaunchedEffect(Unit) {
        visibleStates.forEachIndexed { index, state ->
            delay(index * 120L)
            state.value = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isProfesor) "Panel del Profesor" else "Panel del Estudiante",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkTopBar,
                    titleContentColor = Color.White,
                ),
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Cerrar Sesión", tint = DangerRed)
                    }
                }
            )
        },
        containerColor = DarkSurface
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Welcome card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCard)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(PurpleGradientStart, PurpleGradientEnd)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = usuario.nombre.take(1).uppercase(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "¡Bienvenid@!",
                            fontSize = 13.sp,
                            color = TextGray
                        )
                        Text(
                            text = "${usuario.nombre} ${usuario.apellido}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (isProfesor) {
                            Text(
                                text = "Rol: Profesor",
                                fontSize = 12.sp,
                                color = PurplePrimary
                            )
                        } else {
                            Text(
                                text = "No. Control: ${usuario.numControl}",
                                fontSize = 12.sp,
                                color = TextSubtle
                            )
                            Text(
                                text = "Grupo: ${usuario.grupoNombre}",
                                fontSize = 12.sp,
                                color = TextSubtle
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Navigation buttons (grid)
            items.forEachIndexed { index, (label, color, onClick) ->
                AnimatedVisibility(
                    visible = visibleStates[index].value,
                    enter = slideInHorizontally(
                        initialOffsetX = { if (index % 2 == 0) -it else it },
                        animationSpec = tween(500, easing = FastOutSlowInEasing)
                    ) + fadeIn(tween(500))
                ) {
                    DashboardButton(
                        label = label,
                        color = color,
                        badge = if (index == 0 && unreadMessages > 0) unreadMessages else null,
                        onClick = onClick
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Logout button
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = DangerRed
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(listOf(DangerRed, DangerRed))
                )
            ) {
                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardButton(
    label: String,
    color: Color,
    badge: Int? = null,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            if (badge != null) {
                Badge(
                    modifier = Modifier
                        .align(Alignment.CenterEnd),
                    containerColor = AccentRed,
                    contentColor = Color.White
                ) {
                    Text(badge.toString(), fontWeight = FontWeight.Bold)
                }
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd),
                tint = Color.White.copy(alpha = if (badge != null) 0f else 0.7f)
            )
        }
    }
}
