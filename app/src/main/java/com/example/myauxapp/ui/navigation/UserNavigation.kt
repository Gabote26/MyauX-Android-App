package com.example.myauxapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myauxapp.ui.screens.*

@Composable
fun UserNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // Shared message list state (in-memory)
    val messages = remember {
        mutableStateListOf(
            NoticeItem(
                sender = "Dirección Académica",
                subject = "Cambio de horario - Programación",
                time = "10:30 AM",
                preview = "Se informa que la clase de Programación del día jueves se moverá al salón 204...",
                isRead = false
            ),
            NoticeItem(
                sender = "Servicios Escolares",
                subject = "Recordatorio: Entrega de documentos",
                time = "Ayer",
                preview = "Les recordamos que la fecha límite para entregar documentos de inscripción es el viernes...",
                isRead = true
            ),
            NoticeItem(
                sender = "Coordinación de Especialidad",
                subject = "Proyecto final - Lineamientos",
                time = "15 Feb",
                preview = "Se adjuntan los lineamientos para el proyecto final del semestre. Favor de revisar...",
                isRead = true
            ),
            NoticeItem(
                sender = "Biblioteca",
                subject = "Nuevos recursos disponibles",
                time = "14 Feb",
                preview = "La biblioteca cuenta con nuevos libros de programación y electrónica...",
                isRead = true
            ),
            NoticeItem(
                sender = "Tutorías",
                subject = "Sesión de tutoría grupal",
                time = "13 Feb",
                preview = "Se convoca a todos los estudiantes a la sesión de tutoría grupal del próximo lunes...",
                isRead = true
            )
        )
    }

    NavHost(
        navController = navController,
        startDestination = NavScreen.HomeScreen.name,
        modifier = modifier
    ) {
        composable(NavScreen.HomeScreen.name) { HomeScreen() }
        composable(NavScreen.Horarios.name) { Horarios() }
        composable(NavScreen.Avisos.name) { 
            AvisosWithNavigation(
                messages = messages,
                onMessageClick = { index ->
                    navController.navigate("${NavScreen.MessageDetail.name}/$index")
                },
                onComposeClick = {
                    navController.navigate(NavScreen.ComposeMessage.name)
                }
            )
        }
        composable(NavScreen.Estudiante.name) { Estudiante() }
        composable(NavScreen.Config.name) { Config() }
        
        // Message Detail Screen
        composable(
            route = "${NavScreen.MessageDetail.name}/{messageIndex}",
            arguments = listOf(navArgument("messageIndex") { type = NavType.IntType })
        ) { backStackEntry ->
            val messageIndex = backStackEntry.arguments?.getInt("messageIndex") ?: 0
            if (messageIndex < messages.size) {
                val message = messages[messageIndex]
                MessageDetailScreen(
                    sender = message.sender,
                    subject = message.subject,
                    time = message.time,
                    content = message.preview + "\n\nEste es el contenido completo del mensaje. Aquí se mostraría el texto completo del aviso o mensaje enviado por ${message.sender}.",
                    onBack = { navController.popBackStack() }
                )
            }
        }
        
        // Compose Message Screen
        composable(NavScreen.ComposeMessage.name) {
            ComposeMessageScreen(
                onBack = { navController.popBackStack() },
                onSend = { recipient, subject, messageText ->
                    // Add new message to the list
                    messages.add(
                        0,
                        NoticeItem(
                            sender = "Tú",
                            subject = subject,
                            time = "Ahora",
                            preview = messageText,
                            isRead = true
                        )
                    )
                    navController.popBackStack()
                }
            )
        }
    }
}