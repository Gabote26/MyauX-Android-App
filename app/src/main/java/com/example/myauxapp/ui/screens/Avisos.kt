package com.example.myauxapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class NoticeItem(
    val sender: String,
    val subject: String,
    val time: String,
    val preview: String,
    val isRead: Boolean = false
)

@Composable
fun AvisosWithNavigation(
    messages: List<NoticeItem>,
    onMessageClick: (Int) -> Unit,
    onComposeClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onComposeClick,
                containerColor = Color(0xFF6A1B9A),
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp
                )
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Nuevo mensaje",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF5F5F5),
                            Color(0xFFEDE7F6)
                        )
                    )
                )
                .padding(paddingValues)
        ) {
            // Header with gradient
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF6A1B9A),
                                    Color(0xFF4A148C)
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Avisos y Mensajes",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                }
            }

            // Messages List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                itemsIndexed(messages) { index, notice ->
                    NoticeRow(notice, onClick = { onMessageClick(index) })
                }
            }
        }
    }
}

@Composable
fun Avisos() {
    val notices = listOf(
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

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Open compose message screen */ },
                containerColor = Color(0xFF6A1B9A),
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp
                )
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Nuevo mensaje",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF5F5F5),
                            Color(0xFFEDE7F6)
                        )
                    )
                )
                .padding(paddingValues)
        ) {
            // Header with gradient
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF6A1B9A),
                                    Color(0xFF4A148C)
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Avisos y Mensajes",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                }
            }

            // Messages List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(notices) { notice ->
                    NoticeRow(notice)
                }
            }
        }
    }
}

@Composable
fun NoticeRow(notice: NoticeItem, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (notice.isRead) Color.White else Color(0xFFF3E5F5)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notice.sender,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (notice.isRead) FontWeight.Normal else FontWeight.Bold,
                        color = Color(0xFF212121),
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 15.sp
                    )
                    Text(
                        text = notice.time,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF757575),
                        fontSize = 12.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                
                Text(
                    text = notice.subject,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (notice.isRead) FontWeight.Normal else FontWeight.Bold,
                    color = Color(0xFF424242),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = notice.preview,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF757575),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}